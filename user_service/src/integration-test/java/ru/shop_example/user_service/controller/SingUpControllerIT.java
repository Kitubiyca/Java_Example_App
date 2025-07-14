package ru.shop_example.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.shop_example.user_service.common.AbstractIT;
import ru.shop_example.user_service.dto.RequestEmailDto;
import ru.shop_example.user_service.dto.RequestOTPDto;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.ResponseOTPIdDto;
import ru.shop_example.user_service.dto.kafka.KafkaOTPDto;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.Intent;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.repository.OTPRepository;
import ru.shop_example.user_service.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class SingUpControllerIT implements AbstractIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OTPRepository otpRepository;

    @Test
    @DisplayName("Должен проводить полную регистрацию нового пользователя")
    void ShouldSuccessfullySignUp() throws Exception {

        //Arrange
        RequestSignUpDto requestSignUpDto = createRequestSignUpDto();
        RequestEmailDto requestEmailDto = new RequestEmailDto(createRequestSignUpDto().getEmail());

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(KAFKA.getBootstrapServers(), "testGroup", "true");
        JsonDeserializer<KafkaOTPDto> valueDeserializer = new JsonDeserializer<>(KafkaOTPDto.class).trustedPackages("ru.shop_example.user_service.dto.kafka.KafkaOTPDto");
        DefaultKafkaConsumerFactory<String, KafkaOTPDto> cf = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), valueDeserializer);
        Consumer<String, KafkaOTPDto> consumer = cf.createConsumer();
        consumer.subscribe(Collections.singleton("email-otp-topic"));

        //Act
        //Sign up request
        MvcResult signUpRequestResult = mockMvc.perform(post("/sign-up/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestSignUpDto)))
                .andExpect(status().isOk())
                .andReturn();
        ResponseOTPIdDto responseOTPIdDtoFirst = objectMapper.readValue(signUpRequestResult.getResponse().getContentAsString(), ResponseOTPIdDto.class);

        //Send regenerate code request
        MvcResult resendConfirmationCodeRequestResult = mockMvc.perform(post("/sign-up/resend-confirmation-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestEmailDto)))
                .andExpect(status().isOk())
                .andReturn();
        ResponseOTPIdDto responseOTPIdDtoSecond = objectMapper.readValue(resendConfirmationCodeRequestResult.getResponse().getContentAsString(), ResponseOTPIdDto.class);

        //Send confirmation request
        ConsumerRecords<String, KafkaOTPDto> consumerRecords = KafkaTestUtils.getRecords(consumer, Duration.of(5, ChronoUnit.SECONDS));
        List<ConsumerRecord<String, KafkaOTPDto>> consumerRecordList = new ArrayList<>();
        consumerRecords.records("email-otp-topic").forEach(consumerRecordList::add);
        KafkaOTPDto kafkaOTPDto = consumerRecordList.get(consumerRecordList.size() - 1).value();

        RequestOTPDto requestOTPDto = new RequestOTPDto();
        requestOTPDto.setId(responseOTPIdDtoSecond.getValue());
        requestOTPDto.setValue(kafkaOTPDto.getCode());

        mockMvc.perform(post("/sign-up/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestOTPDto)))
                .andExpect(status().isCreated());

        //Verify
        //Check redis
        assertThat(otpRepository.getByIntentAndId(Intent.signUp, responseOTPIdDtoSecond.getValue())).isEmpty();

        //check postgres
        Optional<User> user = userRepository.findUserByEmail(requestSignUpDto.getEmail());
        assertThat(user).isNotEmpty();
        assertThat(user.get().getStatus()).isEqualTo(UserStatus.active);
    }

    private RequestSignUpDto createRequestSignUpDto() {
        return RequestSignUpDto.builder()
                .email("example@example.com")
                .password("1234")
                .firstname("Иван")
                .lastname("Иванов")
                .patronymic("Иванович")
                .phoneNumber("123456789012")
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
    }
}
