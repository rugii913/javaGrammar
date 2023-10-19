import jungsuk.JungsukExRunner;
import jungsuk.ch14.LambdaMethodReference;
import jungsuk.ch14.StreamBasic;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(StreamBasic.class);
        Runnable exSingleMethodRunner = new JungsukExRunner(StreamBasic.class, "createStreamByConcatenation");

        // 클래스 전체 예제 메서드 run()
        // exRunner.run();

        // 클래스 메서드 중 이름을 명시한 메서드 run()
        exSingleMethodRunner.run();
    }
}
