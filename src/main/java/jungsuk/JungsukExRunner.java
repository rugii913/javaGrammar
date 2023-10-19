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
            /*
            * TODO
            *  (1) https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/package-summary.html Java programming language and JVM modeling in core reflection 읽어보기
            *  (2) 선언된 메서드 순서대로 실행하고 싶은데 쉽진 않은 듯  cf. getDeclaredMethods()는 "The elements in the returned array are not sorted and are not in any particular order."
            *   - https://stackoverflow.com/questions/28585843/java-reflection-getdeclaredmethods-in-declared-order-strange-behaviour
            *   - https://stackoverflow.com/questions/3148274/get-declared-methods-in-order-they-appear-in-source-code
            *    => 정말 하고 싶다면 class 파일을 파싱해야할 듯하다.
            * */

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
