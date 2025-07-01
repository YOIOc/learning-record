package com.itheima.validation;

import com.itheima.annotation.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateValidaion implements ConstraintValidator<State, String> {
    /**
     *
     * @param value 将来要校验的数据
     * @param constraintValidatorContext
     * @return false-校验失败 true-校验成功
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value.equals("已发布") || value.equals("草稿")) {
            return true;
        }
        return false;
    }
}
