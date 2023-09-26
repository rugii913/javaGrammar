import jungsuk.JungsukExRunner;
import jungsuk.ch02.CharToCodeAndSpecialCharEx;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(CharToCodeAndSpecialCharEx.class);
        exRunner.run();
    }
}
