import machines.CharacterClassMatcher;
import machines.CharacterGroups;
import machines.DigitMatcher;
import machines.LiteralMatcher;

import java.io.IOException;
import java.util.Scanner;

public class Main {
  public static void main(String[] args){
    if (args.length != 2 || !args[0].equals("-E")) {
      System.out.println("Usage: ./your_program.sh -E <pattern>");
      System.exit(1);
    }

    String pattern = args[1];  
    Scanner scanner = new Scanner(System.in);
    String inputLine = scanner.nextLine();

    System.err.println("Logs from your program will appear here!");

     if (matchPattern(inputLine, pattern)) {
         System.exit(0);
     } else {
         System.exit(1);
     }
  }

  public static boolean matchPattern(String inputLine, String pattern) {
    if (pattern.length() == 1) {
      return LiteralMatcher.match(inputLine, pattern.charAt(0));
    } else if (pattern.equals("\\d")) {
        return DigitMatcher.match(inputLine);
    } else if (pattern.equals("\\w")) {
        return CharacterClassMatcher.match(inputLine);
    } else if (pattern.startsWith("[")) {
        return CharacterGroups.match(inputLine, pattern.substring(1));
    }

    else {
      throw new RuntimeException("Unhandled pattern: " + pattern);
    }
  }
}
