/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zhengjie.annotation.rest;

import jakarta.validation.Valid;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping HTTP {@code POST} requests onto specific handler
 * methods.
 * 支持匿名访问 PostMapping
 *
 * @author liaojinlong
 * @see AnonymousGetMapping
 * @see AnonymousPostMapping
 * @see AnonymousPutMapping
 * @see AnonymousDeleteMapping
 */
@AnonymousAccess
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnonymousPostMapping {

    /**
     */
    @AliasFor(annotation = Valid.class)
    String name() default "";

    /**
     */
    @AliasFor(annotation = Valid.class)
    String[] value() default {};

    /**
     */
    @AliasFor(annotation = Valid.class)
    String[] path() default {};

    /**
     */
    @AliasFor(annotation = Valid.class)
    String[] params() default {};

    /**
     */
    @AliasFor(annotation = Valid.class)
    String[] headers() default {};

    /**
     */
    @AliasFor(annotation = Valid.class)
    String[] consumes() default {};

    /**
     */
    @AliasFor(annotation = Valid.class)
    String[] produces() default {};

}
