import jungsuk.JungsukExRunner;
import jungsuk.ch02.OverflowEx;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(OverflowEx.class);
        exRunner.run();
    }
}
