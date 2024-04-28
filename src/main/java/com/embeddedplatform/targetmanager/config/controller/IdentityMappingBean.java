package com.embeddedplatform.targetmanager.config.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class IdentityMappingBean implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredField = bean.getClass().getDeclaredFields();
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
