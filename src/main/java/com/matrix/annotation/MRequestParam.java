package com.matrix.annotation;

import java.lang.annotation.*;

/**
 * @Classname MRequestParam
 * @Description TODO
 * @Date 2020-02-08 10:06
 * @Created by jiaoy
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MRequestParam {
    String value() default "" ;
}
