package jungsuk;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JungsukExRunner implements Runnable {

    private static Runnable runnable;

    private JungsukExRunner(Class<?> targetClass, String methodName) {
        if (methodName == null || methodName.isBlank()) {
            runnable = JungsukExClassRunner.of(targetClass);
        } else {
            try {
                runnable = JungsukExMethodRunner.of(targetClass, methodName);
            } catch (NoSuchMethodException e) {
                Logger logger = Logger.getLogger("jungsuk.JungsukExRunner");
                logger.log(Level.WARNING, "해당 이름과 일치하는 메서드가 없습니다.", e);
                logger.log(Level.WARNING, "메서드 실행 대신 클래스 실행으로 전환합니다.");
                runnable = JungsukExClassRunner.of(targetClass);

                /*
                System.out.println("=============================================================================");
                System.out.println("이름이 일치하는 메서드가 존재하지 않으므로, 입력된 클래스의 모든 메서드를 실행한 결과입니다.");
                logger.log(Level.WARNING, "=============================================================================");
                logger.log(Level.WARNING, "이름이 일치하는 메서드가 존재하지 않으므로, 입력된 클래스의 모든 메서드를 실행한 결과입니다.");

                TODO 아래 사항 원인 확인 후, 클래스 모든 메서드 실행 후 로그 남기는 것으로 개선해볼 것
                    - 실행 시킨 후 마지막에 메시지를 더 추가하고 싶었는데 runnable 실행 후 로그가 뜨는 게 아니라 runnable 실행 전 에러 로그가 뜸
                    - 혹시나 해서 System.out.println으로 바꾸었는데, 에러 로그보다는 늦게 뜨지만 여전히 runnable보다는 빨리 실행됨
                    - wait(1000); 메서드를 추가해보았는데, 에러가 발생함
                    - 혹시 다른 스레드를 사용하는지 확인하기 위해 System.out.println(Thread.currentThread().getName());를 찍어보았는데, 같은 main 스레드에서 실행되는 것으로 확인됨   
                */
            }
        }
    }

    public static Runnable of(Class<?> targetClass, String methodName) {
        return new JungsukExRunner(targetClass, methodName);
    }

    @Override
    public void run() {
        runnable.run();
    }
}
