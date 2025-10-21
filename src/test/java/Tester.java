import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Tester {
  public static void main(String[] args) {
    InputStream originalSystemIn = System.in;

//        test(new String[]{"-E", "\\d\\d\\d apples"}, "sally has 124 apples");
//        test(new String[]{"-E", "\\d apple"}, "sally has 3 apple");
//        test(new String[]{"-E", "\\d apple"}, "sally has 1 orange");
//        test(new String[]{"-E", "[^xyz]"}, "apple");
//        test(new String[]{"-E", "[^abc]"}, "apple");
//        test(new String[]{"-E", "\\d \\w\\w\\ws"}, "sally has 1 dog");
//        test(new String[]{"-E", "apple$"}, "mango_apple");
//        test(new String[]{"-E", "^banana$"}, "banana_banana");
    test(new String[]{"-E", "^banana$"}, "banana");

    System.setIn(originalSystemIn);
  }

  private static void test(String[] args, String input) {
    ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
    System.setIn(bais);
    Main.main(args);
  }
}