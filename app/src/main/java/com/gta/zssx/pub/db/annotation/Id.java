package com.gta.zssx.pub.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.FIELD)
public @interface Id {
	String generator() default "";
}
