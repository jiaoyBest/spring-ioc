package com.matrix.annotation;

import java.lang.annotation.*;

/**
 * 这是一个类说明
 * @author (yu.jiao)
 * @date 2020/02/08
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MController {
    String value() default "";
}
