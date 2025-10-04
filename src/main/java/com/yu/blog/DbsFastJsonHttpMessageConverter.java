package com.yu.blog;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class DbsFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    @Override
    protected boolean supports(Class<?> clazz) {
        if (clazz.equals(byte[].class)) {
            return false;
        }
        return true;
    }

}