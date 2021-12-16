package com.stockrock.analysis.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Annotations {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Type {
        String name();
    }

    public interface MethodCallback {
        void call(Method method) throws InvocationTargetException, IllegalAccessException;
    }

    public static class Processor {

        public static void methodInject(Object base, String key)
                throws InvocationTargetException, IllegalAccessException {
            Class<?> clazz = base.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Type.class) && method.getAnnotation(Type.class).name().equals(key)) {
                    method.setAccessible(true);
                    method.invoke(base);
                }
            }
        }

        public static void methodInject(Object base, String key, Object arg)
                throws InvocationTargetException, IllegalAccessException {
            Class<?> clazz = base.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Type.class) && method.getAnnotation(Type.class).name().equals(key)) {
                    method.setAccessible(true);
                    method.invoke(base, arg);
                }
            }
        }

        public static void methodInject(Object base, String key, Object... arg)
                throws InvocationTargetException, IllegalAccessException {
            Class<?> clazz = base.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Type.class) && method.getAnnotation(Type.class).name().equals(key)) {
                    method.setAccessible(true);
                    method.invoke(base, arg);
                }
            }
        }
    }

}
