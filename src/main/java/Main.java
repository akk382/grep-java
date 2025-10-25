import machines.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static machines.States.*;
import static machines.RegexLexeme.*;

public class Main {
  public static void main(String[] args) {
    if (args.length != 2 || !args[0].equals("-E")) {
      System.out.println("Usage: ./your_program.sh -E <pattern>");
      System.exit(1);
    }

    String pattern = args[1];
    Scanner scanner = new Scanner(System.in);
    String inputLine = scanner.nextLine();

    System.err.println("Logs from your program will appear here!");
    List<RegexToken> tokens = lexPattern(pattern);

    StatePos currState = new StatePos(START_STATE, 0);
    currState.setMatchInReverseDirection(tokens.getLast().getLexeme() == ENDS_WITH);
    currState.setState(currState.isMatchInReverseDirection() ? END_STATE :  START_STATE);
    currState.setCurrInputPos(currState.isMatchInReverseDirection() ? inputLine.length() - 1 : 0);

    int tokenPos = currState.isMatchInReverseDirection() ? tokens.size() - 1 : 0;

    while (tokenPos > -1 && tokenPos < tokens.size()) {
      currState = tokens.get(tokenPos).getLexeme()
              .match(currState, inputLine, pattern, tokens, tokenPos);
      tokenPos = currState.isMatchInReverseDirection() ? tokenPos - 1 : tokenPos + 1;
    }
    if (currState.isMatchWildCardAtTheEnd() && !tokens.getFirst().getLexeme().matchWildCardAtTheEnd(currState, inputLine))
      System.exit(1);
    System.out.println("Matched");
    System.exit(0);
  }

  private static List<RegexToken> lexPattern(String pattern) {
    List<RegexToken> tokens = new ArrayList<>();
    int nextTokenPos = 0;
    for (int i = 0; i < pattern.length(); i++) {
      switch (pattern.charAt(i)) {
        case '\\':
          // check next char and set state
          // word
          // digit
          // \\ -> slash match, ignore for now
          switch (pattern.charAt(i + 1)) {
            case 'd':
              tokens.add(new RegexToken(DIGIT));
              break;
            case 'w':
              tokens.add(new RegexToken(WORD));
              break;
            case '.':
              tokens.add(new RegexToken(LITERAL, '.'));
            case '\\':
              tokens.add(new RegexToken(BACK_SLASH));
              break;
            default:
              System.exit(1);
          }
          i++;
          break;
        case '[':
          // match the positive / negative groups
          switch (pattern.charAt(i + 1)) {
            case '^':
              tokens.add(new RegexToken(i + 2, NEGATIVE_GROUP));
              break;
            default:
              tokens.add(new RegexToken(i + 1, POSITIVE_GROUP));
              break;
          }
          i++;
          while (pattern.charAt(i) != ']') i++;
          i++;
          break;
        case '^':
          tokens.add(new RegexToken(STARTS_WITH));
          break;
        case '$':
          tokens.add(new RegexToken(ENDS_WITH));
          break;
        case '+':
          switch (tokens.get(nextTokenPos - 1).getLexeme()) {
            case DIGIT -> tokens.set(nextTokenPos - 1, new RegexToken(DIGIT_PLUS));
            case LITERAL ->
                    tokens.set(nextTokenPos - 1, new RegexToken(LITERAL_PLUS, tokens.get(nextTokenPos - 1).getValue()));
            case WORD -> tokens.set(nextTokenPos - 1, new RegexToken(WORD_PLUS));
            case POSITIVE_GROUP ->
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), POSITIVE_GROUP_PLUS));
            case NEGATIVE_GROUP ->
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), NEGATIVE_GROUP_PLUS));
            case WILD_CARD -> tokens.set(nextTokenPos - 1, new RegexToken(WILD_CARD_PLUS));
            default ->
                    throw new UnsupportedOperationException("Encountered unsupported token " + tokens.get(nextTokenPos - 1).getLexeme() + " before +");
          }
          break;
        case '*':
          switch (tokens.get(nextTokenPos - 1).getLexeme()) {
            case DIGIT -> tokens.set(nextTokenPos - 1, new RegexToken(DIGIT_STAR));
            case LITERAL ->
                    tokens.set(nextTokenPos - 1, new RegexToken(LITERAL_STAR, tokens.get(nextTokenPos - 1).getValue()));
            case WORD -> tokens.set(nextTokenPos - 1, new RegexToken(WORD_STAR));
            case POSITIVE_GROUP ->
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), POSITIVE_GROUP_STAR));
            case NEGATIVE_GROUP ->
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), NEGATIVE_GROUP_STAR));
            case WILD_CARD -> tokens.set(nextTokenPos - 1, new RegexToken(WILD_CARD_STAR));
            default ->
                    throw new UnsupportedOperationException("Encountered unsupported token " + tokens.get(nextTokenPos - 1).getLexeme() + " before *");
          }
          break;
        case '?':
          switch (tokens.get(nextTokenPos - 1).getLexeme()) {
            case DIGIT:
              tokens.set(nextTokenPos - 1, new RegexToken(DIGIT_QUE));
              break;
            case LITERAL:
              tokens.set(nextTokenPos - 1, new RegexToken(LITERAL_QUE, tokens.get(nextTokenPos - 1).getValue()));
              break;
            case WORD:
              tokens.set(nextTokenPos - 1, new RegexToken(WORD_QUE));
              break;
            case POSITIVE_GROUP:
              tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), POSITIVE_GROUP_QUE));
              break;
            case NEGATIVE_GROUP:
              tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), NEGATIVE_GROUP_QUE));
              break;
            case WILD_CARD:
              tokens.set(nextTokenPos - 1, new RegexToken(WILD_CARD_QUE));
              break;
            case DIGIT_QUE:
            case LITERAL_QUE:
            case WORD_QUE:
            case POSITIVE_GROUP_QUE:
            case NEGATIVE_GROUP_QUE:
              break;
            default:
              throw new UnsupportedOperationException("Encountered unsupported token " + tokens.get(nextTokenPos - 1).getLexeme() + " before ?");
          }
          break;
        case '.':
          tokens.add(new RegexToken(WILD_CARD));
          break;
        default:
          // literal match
          if (nextTokenPos > 0) {
            // Check if the previous token is LITERAL_PLUS | LITERAL_STAR | LITERAL_QUE
            // and matches their LITERAL value to current value, and increment the minOccurrences if it is.
            RegexToken regexToken = tokens.get(nextTokenPos - 1);
            RegexLexeme lexeme = regexToken.getLexeme();
            char currentLiteralValue = pattern.charAt(i);
            if ((lexeme == LITERAL_PLUS || lexeme == LITERAL_STAR) && regexToken.getValue() == currentLiteralValue) {
              regexToken.setMinOccurrences(regexToken.getMinOccurrences() + 1);
              tokens.set(nextTokenPos - 1, regexToken);
            } else if (lexeme == LITERAL_QUE && regexToken.getValue() == currentLiteralValue) {
              regexToken.setMinOccurrences(regexToken.getMinOccurrences() + 1);
              regexToken.setMaxOccurrences(regexToken.getMaxOccurrences() + 1);
              tokens.set(nextTokenPos - 1, regexToken);
            } else {
              tokens.add(new RegexToken(LITERAL, pattern.charAt(i)));
            }
          } else {
            tokens.add(new RegexToken(LITERAL, pattern.charAt(i)));
          }
          break;
      }
      nextTokenPos = tokens.size();
    }

    return tokens;
  }
}
