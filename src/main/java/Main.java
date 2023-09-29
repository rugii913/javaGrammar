import jungsuk.JungsukExRunner;
import jungsuk.ch04.ControlStatement;

public class Main {

    public static void main(String[] args) {
        Runnable exRunner = new JungsukExRunner(ControlStatement.class);
        exRunner.run();
    }
}
