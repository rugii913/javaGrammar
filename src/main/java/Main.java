import jungsuk.JungsukExRunner;
import jungsuk.ch14.Stream01Basic;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(Stream01Basic.class);
        Runnable exSingleMethodRunner = new JungsukExRunner(Stream01Basic.class, "createStreamByConcatenation");

        // 클래스 전체 예제 메서드 run()
        // exRunner.run();

        // 클래스 메서드 중 이름을 명시한 메서드 run()
        exSingleMethodRunner.run();
    }
}
