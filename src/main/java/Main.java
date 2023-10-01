import jungsuk.JungsukExRunner;
import jungsuk.ch05.ArrayBasic;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(ArrayBasic.class);
        exRunner.run();
    }
}
