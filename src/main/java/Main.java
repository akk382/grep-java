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
    boolean shouldStartWith = false;

    for (RegexToken token : tokens) {
        switch (token.getLexeme()) {
            case LITERAL:
                currState = shouldStartWith ?
                        new StatePos(States.LITERAL_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(States.LITERAL_MATCH_ANYWHERE, 0);
                currState = LiteralMatcher.match(inputLine, token.getValue(), currState);
                if (currState.getState() != States.LITERAL_MATCHED) System.exit(1);
                break;
            case DIGIT:
                currState = shouldStartWith ?
                        new StatePos(States.DIGIT_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(States.DIGIT_MATCH_ANYWHERE, 0);
                currState = DigitMatcher.match(inputLine, currState);
                if (currState.getState() != States.DIGIT_MATCHED) System.exit(1);
                break;
            case WORD:
                currState = shouldStartWith ?
                        new StatePos(States.WORD_CLASS_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(States.WORD_CLASS_MATCH_ANYWHERE, 0);
                currState = CharacterClassMatcher.match(inputLine, currState);
                if (currState.getState() != States.WORD_CLASS_MATCHED) System.exit(1);
                break;
            case POSITIVE_GROUP:
                currState = shouldStartWith ?
                        new StatePos(States.POS_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(States.POS_GROUP_MATCH_ANYWHERE, 0);
                currState = CharacterGroups.match(inputLine, pattern.substring(token.getPos()), currState);
                if (currState.getState() != States.POS_GROUP_MATCHED) System.exit(1);
                break;
            case NEGATIVE_GROUP:
                currState = shouldStartWith ?
                        new StatePos(States.NEG_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(States.NEG_GROUP_MATCH_ANYWHERE, 0);
                currState = CharacterGroups.negativeMatch(inputLine, pattern.substring(token.getPos()), currState);
                if (currState.getState() != States.NEG_GROUP_MATCHED) System.exit(1);
                break;
            case STARTS_WITH: shouldStartWith = true; break;
            case NON_WORD:
            case NON_DIGIT:
            case BACK_SLASH:
            default:
                throw new UnsupportedOperationException("Class " + token.getLexeme() + " not supported yet.");
        }
    }
    System.exit(0);
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
                  while (pattern.charAt(i) != ']') i++;
                  i++;
                  break;
              case '^':
                  tokens.add(new RegexToken(RegexLexeme.STARTS_WITH, null)); break;
              default:
                  // literal match
                  tokens.add(new RegexToken(RegexLexeme.LITERAL, pattern.charAt(i)));
                  break;
          }
      }

      return tokens;
    }
}
