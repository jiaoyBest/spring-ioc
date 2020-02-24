package com.matrix.annotation;

import java.lang.annotation.*;

/**
 * @Classname MRequestMapping
 * @Description TODO
 * @Date 2020-02-08 10:05
 * @Created by jiaoy
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MRequestMapping {
    String value() default "" ;
}
