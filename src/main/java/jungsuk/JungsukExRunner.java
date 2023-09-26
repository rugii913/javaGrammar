package jungsuk;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class JungsukExRunner implements Runnable {

    private final Class<?> target;

    public JungsukExRunner(Class<?> target) {
        this.target = target;
    }

    @Override
    public void run() {
        Constructor<?> constructor = getDefaultConstructor();
        invokeDeclaredPublicMethods(constructor, target.getDeclaredMethods());
    }

    private Constructor<?> getDefaultConstructor() {
        Constructor<?> constructor;

        try {
            constructor = target.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return constructor;
    }

    private void invokeDeclaredPublicMethods(Constructor<?> constructor, Method[] declaredMethods) {
        Object object;

        try {
            object = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        Arrays.stream(declaredMethods)
                .filter(method -> method.getModifiers() == Modifier.PUBLIC)
                .forEach(method -> invokeWithExceptionHandling(method, object));
    }

    private void invokeWithExceptionHandling(Method method, Object object) {
        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
