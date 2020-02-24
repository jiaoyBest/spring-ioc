package com.matrix.annotation;

import java.lang.annotation.*;

/**
 * @Classname MAitowrited
 * @Description TODO
 * @Date 2020-02-08 10:05
 * @Created by jiaoy
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MAitowrited {
    String value() default "" ;
}
