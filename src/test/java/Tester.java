import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Tester {
  public static void main(String[] args) {
    InputStream originalSystemIn = System.in;

//    test(new String[]{"-E", "\\d\\d\\d apples"}, "sally has 124 apples");
//    test(new String[]{"-E", "\\d apple"}, "sally has 3 apple");
//    test(new String[]{"-E", "\\d apple"}, "sally has 1 orange");
//    test(new String[]{"-E", "[^xyz]"}, "apple");
//    test(new String[]{"-E", "[^abc]"}, "apple");
//    test(new String[]{"-E", "\\d \\w\\w\\ws"}, "sally has 1 dog");
//    test(new String[]{"-E", "apple$"}, "mango_apple");
//    test(new String[]{"-E", "^banana$"}, "banana_banana");
//    test(new String[]{"-E", "^banana$"}, "banana");
//    test(new String[]{"-E", "c\\d+\\dt"}, "c125ts"); // TODO
//    test(new String[]{"-E", "ca+at"}, "caaats");
//    test(new String[]{"-E", "ca+t"}, "act");
//    test(new String[]{"-E", "ca?a?t"}, "cat");
//    test(new String[]{"-E", "c\\d?\\d?t"}, "c12t");
//    test(new String[]{"-E", "cat"}, "cajsfcat");
    test(new String[]{"-E", "g.+gol"}, "goøö0Ogol");
//    test(new String[]{"-E", ".gol"}, "goøö0Ogol");

    System.setIn(originalSystemIn);
  }

  private static void test(String[] args, String input) {
    ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
    System.setIn(bais);
    Main.main(args);
  }
}