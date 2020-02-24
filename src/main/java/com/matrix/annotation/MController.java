package com.matrix.annotation;

import java.lang.annotation.*;

/**
 * @Classname MController
 * @Description TODO
 * @Date 2020-02-08 10:04
 * @Created by jiaoy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MController {
    String value() default "";
}
