package com.user.Interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicEndpoint
    //This defines a custom annotation named PublicEndpoint.
{
    /*
    @Target(ElementType.METHOD) ---> This tells Java where you can apply the annotation.
            ElementType.METHOD means you can use @PublicEndpoint only on methods.


     @Retention(RetentionPolicy.RUNTIME)
                --> This specifies how long the annotation is retained.
                -->  RUNTIME means the annotation will be available at runtime via reflection.
                -->  This is important if you want to check for the annotation in an interceptor, filter, or aspect.

     */



}
