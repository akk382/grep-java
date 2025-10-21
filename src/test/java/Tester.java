import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Tester {
    public static void main(String[] args) {
        InputStream originalSystemIn = System.in;

        String[] args1 = {"-E", "\\d apple"};
        String input = "1 apple";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        Main.main(args1);

        System.setIn(originalSystemIn);
    }
}