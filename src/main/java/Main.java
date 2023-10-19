import jungsuk.JungsukExRunner;
import jungsuk.ch14.Stream01Basic;
import jungsuk.ch14.Stream02IntermediateOperation;

public class Main {

    public static void main(String[] args) {

        Class<?> targetClass = Stream02IntermediateOperation.class;
        String methodName = "abcd";

        JungsukExRunner.of(targetClass, methodName).run();
    }
}
