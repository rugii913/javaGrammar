import jungsuk.JungsukExRunner;
import jungsuk.ch02.FloatEx;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(FloatEx.class);
        exRunner.run();
    }
}
