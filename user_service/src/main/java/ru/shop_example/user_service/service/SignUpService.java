package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.ResponseOTPIdDto;
import ru.shop_example.user_service.dto.RequestEmailDto;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.RequestOTPDto;
import ru.shop_example.user_service.controller.SignUpController;
import ru.shop_example.user_service.exception.custom.*;

/**
 * Сервис для обработки регистрации пользователя в приложении.
 *
 * @see SignUpController
 */
public interface SignUpService {

    /**
     * Создаёт запись о новом пользователе в базе данных.
     * Отправляет код подтверждения на почту пользователя.
     *
     * @param requestSignUpDto дто с данными о новом пользователе
     *
     * @return дто с id для подтверждения регистрации
     *
     * @throws UserAlreadyExistsException пользователь с таким номер телефона или почтой уже зарегистрирован
     * @throws InternalServerLogicException не найдена роль по умолчанию (не должно появлятся при нормальных условиях)
     */
    ResponseOTPIdDto registerUser(RequestSignUpDto requestSignUpDto);

    /**
     * Повторяет отправку кода подтверждения на почту пользователя.
     *
     * @param requestEmailDto электронная почта пользователя
     *
     * @return дто с id для подтверждения регистрации
     *
     * @throws UserNotFoundException пользователь с указанной почтой не найден
     * @throws RequestDeniedException неверный статус пользователя
     */
    ResponseOTPIdDto resendOTPWithEmail(RequestEmailDto requestEmailDto);

    /**
     * Подтверждает регистрацию пользователя.
     *
     * @param RequestOTPDto дто с кодом подтверждения и связанным id
     *
     * @throws OTPTimedOutException этого кода нет в списке действительный, либо истёк его срок жизни, либо его не существовало
     * @throws UserNotFoundException пользователь, привязанный к этому коду не существует (не должно появлятся при нормальных условиях)
     * @throws RequestDeniedException неверный статус пользователя
     * @throws OTPException указан неверный код подтверждения
     */
    void confirmRegistration(RequestOTPDto RequestOTPDto);
}
