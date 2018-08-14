package org.hjf.annotation.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * View 点击防抖
 * Class 文件有效
 * RetentionPolicy: 注解声明周期 SOURCE CLASS RUNTIME
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface SingleClick {

}
