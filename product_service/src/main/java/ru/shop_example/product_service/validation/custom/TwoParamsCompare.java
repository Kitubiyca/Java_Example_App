package ru.shop_example.product_service.validation.custom;

import jakarta.validation.Payload;

public @interface TwoParamsCompare {

    String message() default "TwoParamsCompare validation constraint violation";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String smallerParam();
    String biggerParam();
    boolean allowEqual() default false;
}
