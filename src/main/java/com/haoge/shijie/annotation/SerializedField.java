package com.haoge.shijie.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jeff on 15/10/23.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializedField {
    /**
     * 需要返回(显示)的字段
     * @return
     */
    String[] includes() default {};
    /**
     * 需要加密的字段
     * @return
     */
    String[] encryptions() default {};
    /**
     * 需要去除的字段（没什么用）
     * @return
     */
    String[] excludes() default {};

    /**
     * 数据是否需要加密
     * @return
     */
    boolean encode() default true;
}