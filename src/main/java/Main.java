import jungsuk.JungsukExRunner;
import jungsuk.ch02.PrintfEx1And2;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(PrintfEx1And2.class);
        exRunner.run();
    }
}
