import machines.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static machines.States.*;
import static machines.RegexLexeme.*;

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

    boolean matchInReverseDirection = tokens.getLast().getLexeme() == ENDS_WITH;

    StatePos currState = new StatePos(matchInReverseDirection ? END_STATE : SART_STATE, matchInReverseDirection ? inputLine.length() - 1 : 0);

    int tokenPos = !matchInReverseDirection ? 0 : tokens.size() - 1;

    while (tokenPos > -1 && tokenPos < tokens.size()) {
        switch (tokens.get(tokenPos).getLexeme()) {
            case LITERAL:
                currState = currState.getState() != SART_STATE ?
                        new StatePos(LITERAL_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(LITERAL_MATCH_ANYWHERE, 0);
                currState = LiteralMatcher.match(inputLine, tokens.get(tokenPos).getValue(), currState, matchInReverseDirection);
                if (currState.getState() != LITERAL_MATCHED) System.exit(1);
                break;
            case DIGIT:
                currState = currState.getState() != SART_STATE ?
                        new StatePos(DIGIT_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(DIGIT_MATCH_ANYWHERE, 0);
                currState = DigitMatcher.match(inputLine, currState, matchInReverseDirection);
                if (currState.getState() != DIGIT_MATCHED) System.exit(1);
                break;
            case WORD:
                currState = currState.getState() != SART_STATE ?
                        new StatePos(WORD_CLASS_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(WORD_CLASS_MATCH_ANYWHERE, 0);
                currState = CharacterClassMatcher.match(inputLine, currState, matchInReverseDirection);
                if (currState.getState() != WORD_CLASS_MATCHED) System.exit(1);
                break;
            case POSITIVE_GROUP:
                currState = currState.getState() != SART_STATE ?
                        new StatePos(POS_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(POS_GROUP_MATCH_ANYWHERE, 0);
                currState = CharacterGroups.match(inputLine, pattern.substring(tokens.get(tokenPos).getPos()), currState, matchInReverseDirection);
                if (currState.getState() != POS_GROUP_MATCHED) System.exit(1);
                break;
            case NEGATIVE_GROUP:
                currState = currState.getState() != SART_STATE ?
                        new StatePos(NEG_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                        new StatePos(NEG_GROUP_MATCH_ANYWHERE, 0);
                currState = CharacterGroups.negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getPos()), currState, matchInReverseDirection);
                if (currState.getState() != NEG_GROUP_MATCHED) System.exit(1);
                break;
            case STARTS_WITH:
                if (matchInReverseDirection) {
                    if (currState.getCurrInputPos() != -1)
                        System.exit(1);
                } else if (tokens.size() > 1) {
                    switch (tokens.get(1).getLexeme()) {
                        case LITERAL -> currState.setState(LITERAL_MATCH_NEXT);
                        case DIGIT -> currState.setState(DIGIT_MATCH_NEXT);
                        case WORD -> currState.setState(WORD_CLASS_MATCH_NEXT);
                        case POSITIVE_GROUP -> currState.setState(POS_GROUP_MATCH_NEXT);
                        case NEGATIVE_GROUP -> currState.setState(NEG_GROUP_MATCH_NEXT);
                        default -> throw new UnsupportedOperationException("Class " + tokens.get(tokenPos).getLexeme() + " not supported yet.");
                    }
                }
                break;
            case ENDS_WITH:
//                if (currState.getCurrInputPos() != inputLine.length() + 1) System.exit(1);
                break;
            case NON_WORD:
            case NON_DIGIT:
            case BACK_SLASH:
            default:
                throw new UnsupportedOperationException("Class " + tokens.get(tokenPos).getLexeme() + " not supported yet.");
        }
        tokenPos = matchInReverseDirection ? tokenPos - 1 : tokenPos + 1;
    }
      System.out.println("Matched");
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
                      case 'd': tokens.add(new RegexToken(DIGIT, null)); break;
                      case 'w': tokens.add(new RegexToken(WORD, null)); break;
                      case '\\': tokens.add(new RegexToken(BACK_SLASH, null)); break;
                      default: System.exit(1);
                  }
                  i++;
                  break;
              case '[':
                  // match the positive / negative groups
                  switch (pattern.charAt(i + 1)) {
                      case '^': tokens.add(new RegexToken(NEGATIVE_GROUP, null, i + 2)); break;
                      default: tokens.add(new RegexToken(POSITIVE_GROUP, null, i + 1)); break;
                  }
                  i++;
                  while (pattern.charAt(i) != ']') i++;
                  i++;
                  break;
              case '^':
                  tokens.add(new RegexToken(STARTS_WITH, null)); break;
              case '$':
                  tokens.add(new RegexToken(ENDS_WITH, null)); break;
              default:
                  // literal match
                  tokens.add(new RegexToken(LITERAL, pattern.charAt(i)));
                  break;
          }
      }

      return tokens;
    }
}
