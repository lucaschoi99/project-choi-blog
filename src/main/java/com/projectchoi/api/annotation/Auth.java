package com.projectchoi.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 파라미터에만 사용할 어노테이션
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 어노테이션 정보가 남아있도록 설정
public @interface Auth {
}
