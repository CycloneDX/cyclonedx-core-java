package org.cyclonedx.util.serializer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPropertyListSerializer {
}