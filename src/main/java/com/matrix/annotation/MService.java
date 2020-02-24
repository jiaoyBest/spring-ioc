package com.matrix.annotation;

import java.lang.annotation.*;

/**
 * @Classname MService
 * @Description TODO
 * @Date 2020-02-08 10:03
 * @Created by jiaoy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MService {
    String value() default "" ;
}
