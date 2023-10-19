package jungsuk;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class JungsukExRunner implements Runnable {

    private final Class<?> targetClass;
    private Method targetMethod = null;

    public JungsukExRunner(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public JungsukExRunner(Class<?> targetClass, String methodName) {
        this.targetClass = targetClass;

        try {
            this.targetMethod = targetClass.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("입력된 이름을 가진 메서드가 없음", e);
        }
    }

    @Override
    public void run() {
        Constructor<?> constructor = getDefaultConstructor();

        if (this.targetMethod == null) {
            invokeDeclaredPublicMethods(constructor, targetClass.getDeclaredMethods());

        } else {
            Object object;

            try {
                object = constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            try {
                targetMethod.invoke(object); // TODO ***** 왜 정확한 타입 정보 없이 Object 타입일 뿐인데도 invoke할 수 있는지 찾아볼 것 *****
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Constructor<?> getDefaultConstructor() {
        Constructor<?> constructor;

        try {
            constructor = targetClass.getConstructor();
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
