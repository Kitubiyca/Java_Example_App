package ru.shop_example.product_service.validation.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class TwoParamsCompareValidation implements ConstraintValidator<TwoParamsCompare, Object> {

    private String smallerParamName;
    private String biggerParamName;
    private boolean allowEqual;
    private String message;

    @Override
    public void initialize(TwoParamsCompare constraintAnnotation) {
        smallerParamName = constraintAnnotation.smallerParam();
        biggerParamName = constraintAnnotation.biggerParam();
        allowEqual = constraintAnnotation.allowEqual();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(o);
        Comparable smallerParam = (Comparable) beanWrapper.getPropertyValue(smallerParamName);
        Comparable biggerParam = (Comparable) beanWrapper.getPropertyValue(biggerParamName);

        if (smallerParam == null || biggerParam == null) return true;

        int compareResult = smallerParam.compareTo(biggerParam);

        if((compareResult == 0 && !allowEqual)||(compareResult > 0)){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(smallerParamName)
                    .addPropertyNode(biggerParamName)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
