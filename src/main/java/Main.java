import jungsuk.JungsukExRunner;
import jungsuk.ch03.Operator;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(Operator.class);
        exRunner.run();
    }
}
