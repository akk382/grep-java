import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Tester {
    public static void main(String[] args) {
        InputStream originalSystemIn = System.in;

//        test(new String[]{"-E", "\\d\\d\\d apples"}, "sally has 124 apples", 0);
//        test(new String[]{"-E", "\\d apple"}, "sally has 3 apple", 0);
//        test(new String[]{"-E", "\\d apple"}, "sally has 1 orange", 1);
        test(new String[]{"-E", "[^xyz]"}, "apple", 0);

        System.setIn(originalSystemIn);
    }

    private static void test(String[] args, String input, int expectedExitStatus) {
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);
        Main.main(args);
    }
}