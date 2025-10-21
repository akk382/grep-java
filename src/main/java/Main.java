import machines.*;

import java.util.ArrayList;
import java.util.List;
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
    List<RegexToken> tokens = lexPattern(pattern);

    StatePos currState = new StatePos(States.SART_STATE, 0);

    for (RegexToken token : tokens) {
        switch (token.getLexeme()) {
            case LITERAL:
                currState = currState.getCurrInputPos() == 0 ?
                        new StatePos(States.LITERAL_MATCH_ANYWHERE, 0) :
                        new StatePos(States.LITERAL_MATCH_NEXT, currState.getCurrInputPos());
                currState = LiteralMatcher.match(inputLine.substring(currState.getCurrInputPos()), token.getValue(), currState);
                if (currState.getState() != States.LITERAL_MATCHED) System.exit(1);
                break;
            case DIGIT:
                currState = currState.getCurrInputPos() == 0 ?
                        new StatePos(States.DIGIT_MATCH_ANYWHERE, 0) :
                        new StatePos(States.DIGIT_MATCH_NEXT, currState.getCurrInputPos());
                currState = DigitMatcher.match(inputLine.substring(currState.getCurrInputPos()), currState);
                if (currState.getState() != States.DIGIT_MATCHED) System.exit(1);
                break;
            case WORD:
                currState = currState.getCurrInputPos() == 0 ?
                        new StatePos(States.WORD_CLASS_MATCH_ANYWHERE, 0) :
                        new StatePos(States.WORD_CLASS_MATCH_NEXT, currState.getCurrInputPos());
                currState = CharacterClassMatcher.match(inputLine, currState);
                if (currState.getState() != States.WORD_CLASS_MATCHED) System.exit(1);
                break;
            case POSITIVE_GROUP:
                currState = currState.getCurrInputPos() == 0 ?
                        new StatePos(States.POS_GROUP_MATCH_ANYWHERE, 0) :
                        new StatePos(States.POS_GROUP_MATCH_NEXT, currState.getCurrInputPos());
                currState = CharacterGroups.match(inputLine, pattern.substring(token.getPos()), currState);
                if (currState.getState() != States.POS_GROUP_NOT_MATCHED) System.exit(1);
                break;
            case NEGATIVE_GROUP:
                currState = currState.getCurrInputPos() == 0 ?
                        new StatePos(States.NEG_GROUP_MATCH_ANYWHERE, 0) :
                        new StatePos(States.NEG_GROUP_MATCH_NEXT, currState.getCurrInputPos());
                currState = CharacterGroups.negativeMatch(inputLine, pattern.substring(token.getPos()), currState);
                if (currState.getState() != States.NEG_GROUP_NOT_MATCHED) System.exit(1);
                break;
            case NON_WORD:
            case NON_DIGIT:
            case BACK_SLASH:
            default:
                throw new UnsupportedOperationException("Class " + token.getLexeme() + " not supported yet.");
        }
    }
    System.exit(0);

//     if (matchPattern(inputLine, pattern)) {
//         System.exit(0);
//     } else {
//         System.exit(1);
//     }
  }

    private static List<RegexToken> lexPattern(String pattern) {
      List<RegexToken> tokens = new ArrayList<>();
      for (int i = 0; i < pattern.length(); i++) {
          switch (pattern.charAt(i)) {
              case '\\':
                  // check next char and set state
                  // word
                  // digit
                  // \\ -> slash match, ignore for now
                  switch (pattern.charAt(i + 1)) {
                      case 'd': tokens.add(new RegexToken(RegexLexeme.DIGIT, null)); break;
                      case 'w': tokens.add(new RegexToken(RegexLexeme.WORD, null)); break;
                      case '\\': tokens.add(new RegexToken(RegexLexeme.BACK_SLASH, null)); break;
                      default: System.exit(1);
                  }
                  i++;
                  break;
              case '[':
                  // match the positive / negative groups
                  switch (pattern.charAt(i + 1)) {
                      case '^': tokens.add(new RegexToken(RegexLexeme.NEGATIVE_GROUP, null, i + 2)); break;
                      default: tokens.add(new RegexToken(RegexLexeme.POSITIVE_GROUP, null, i + 1)); break;
                  }
                  i++;
                  break;
              default:
                  // literal match
                  tokens.add(new RegexToken(RegexLexeme.LITERAL, pattern.charAt(i)));
                  break;
          }
      }

      return tokens;
    }

//    public static boolean matchPattern(String inputLine, String pattern) {
//    if (pattern.length() == 1) {
//      return LiteralMatcher.match(inputLine, pattern.charAt(0));
//    } else if (pattern.equals("\\d")) {
//        return DigitMatcher.match(inputLine);
//    } else if (pattern.equals("\\w")) {
//        return CharacterClassMatcher.match(inputLine);
//    } else if (pattern.startsWith("[^")) {
//        return CharacterGroups.negativeMatch(inputLine, pattern.substring(2));
//    }  else if (pattern.startsWith("[")) {
//        return CharacterGroups.match(inputLine, pattern.substring(1));
//    }
//
//    else {
//      throw new RuntimeException("Unhandled pattern: " + pattern);
//    }
//  }
}
