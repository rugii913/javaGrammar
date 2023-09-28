import jungsuk.JungsukExRunner;
import jungsuk.ch02.CastingEx;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(CastingEx.class);
        exRunner.run();
    }
}
