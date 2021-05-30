package pxf.toolkit.basic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内置的注解，用于标识一些要被跳过的一些类，或者其它用途的类
 *
 * @author potatoxf
 * @date 2021/5/30
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BuiltIn {}
