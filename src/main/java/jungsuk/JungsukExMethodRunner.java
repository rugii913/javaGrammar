package jungsuk;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class JungsukExMethodRunner implements Runnable {

    private final Class<?> targetClass;
    private final Method targetMethod;

    private JungsukExMethodRunner(Class<?> targetClass, String methodName) throws NoSuchMethodException {
        this.targetClass = targetClass;
        this.targetMethod = targetClass.getMethod(methodName);
    }

    public static Runnable of(Class<?> targetClass, String methodName) throws NoSuchMethodException {
        return new JungsukExMethodRunner(targetClass, methodName);
    }

    @Override
    public void run() {
        Constructor<?> constructor = getDefaultConstructor();

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

    private Constructor<?> getDefaultConstructor() {
        Constructor<?> constructor;

        try {
            constructor = targetClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return constructor;
    }
}
