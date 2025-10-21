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

    boolean matchInReverseDirection = tokens.getLast().getLexeme() == ENDS_WITH;

    StatePos currState = new StatePos(matchInReverseDirection ? END_STATE : START_STATE, matchInReverseDirection ? inputLine.length() - 1 : 0);

    int tokenPos = !matchInReverseDirection ? 0 : tokens.size() - 1;
    int minOcc = 0, i = 0, maxOcc = 0;

    while (tokenPos > -1 && tokenPos < tokens.size()) {
      switch (tokens.get(tokenPos).getLexeme()) {
        case LITERAL:
          currState = currState.getState() != START_STATE ?
                  new StatePos(LITERAL_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(LITERAL_MATCH_ANYWHERE, 0);
          currState = LiteralMatcher.match(inputLine, tokens.get(tokenPos).getValue(), currState, matchInReverseDirection);
          if (currState.getState() != LITERAL_MATCHED) System.exit(1);
          break;
        case LITERAL_PLUS:
          currState = currState.getState() != START_STATE ?
                  new StatePos(LITERAL_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(LITERAL_MATCH_ANYWHERE, 0);
          minOcc = tokens.get(tokenPos).getMinOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == LITERAL_MATCHED ? LITERAL_MATCH_NEXT : currState.getState());
            currState = LiteralMatcher.match(inputLine, tokens.get(tokenPos).getValue(), currState, matchInReverseDirection);
            i++;
          } while (i < minOcc && currState.getState() == LITERAL_MATCHED);
          if (currState.getState() != LITERAL_MATCHED) System.exit(1);
          else {
            while (currState.getState() == LITERAL_MATCHED) {
              currState.setState(LITERAL_MATCH_NEXT);
              currState = LiteralMatcher.match(inputLine, tokens.get(tokenPos).getValue(), currState, matchInReverseDirection);
            }
          }
          break;
        case LITERAL_STAR:
//          currState = currState.getState() != START_STATE ?
//                  new StatePos(LITERAL_MATCH_NEXT, currState.getCurrInputPos()) :
//                  new StatePos(LITERAL_MATCH_ANYWHERE, 0);
//          minOcc = tokens.get(tokenPos).getMinOccurrences();
//          i = 0;
//          do {
//            currState.setState(currState.getState() == LITERAL_MATCHED ? LITERAL_MATCH_NEXT : currState.getState());
//            currState = LiteralMatcher.match(inputLine, tokens.get(tokenPos).getValue(), currState, matchInReverseDirection);
//            i++;
//          } while (i < minOcc || currState.getState() == LITERAL_MATCHED);
//          if (i < minOcc) System.exit(1);
          break;
        case LITERAL_QUE:
          currState = currState.getState() != START_STATE ?
                  new StatePos(LITERAL_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(LITERAL_MATCH_ANYWHERE, 0);
          minOcc = tokens.get(tokenPos).getMinOccurrences();
          maxOcc = tokens.get(tokenPos).getMaxOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == LITERAL_MATCHED ? LITERAL_MATCH_NEXT : currState.getState());
            currState = LiteralMatcher.match(inputLine, tokens.get(tokenPos).getValue(), currState, matchInReverseDirection);
            i++;
          } while (i < maxOcc && currState.getState() == LITERAL_MATCHED);
          if (i < minOcc) System.exit(1);
          else if (i == maxOcc && currState.getState() == LITERAL_MATCHED) {
            currState.setState(LITERAL_MATCH_NEXT);
            if (LiteralMatcher.match(inputLine, tokens.get(tokenPos).getValue(), currState, matchInReverseDirection).getState() == LITERAL_MATCHED)
              System.exit(1);
          }
          break;
        case DIGIT:
          currState = currState.getState() != START_STATE ?
                  new StatePos(DIGIT_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(DIGIT_MATCH_ANYWHERE, 0);
          currState = DigitMatcher.match(inputLine, currState, matchInReverseDirection);
          if (currState.getState() != DIGIT_MATCHED) System.exit(1);
          break;
        case DIGIT_PLUS:
          currState = currState.getState() != START_STATE ?
                  new StatePos(DIGIT_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(DIGIT_MATCH_ANYWHERE, 0);
          minOcc = tokens.get(tokenPos).getMinOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == DIGIT_MATCHED ? DIGIT_MATCH_NEXT : currState.getState());
            currState = DigitMatcher.match(inputLine, currState, matchInReverseDirection);
            i++;
          } while (i < minOcc && currState.getState() == DIGIT_MATCHED);
          if (currState.getState() != DIGIT_MATCHED) System.exit(1);
          else {
            while (currState.getState() == DIGIT_MATCHED) {
              currState.setState(DIGIT_MATCH_NEXT);
              currState = DigitMatcher.match(inputLine, currState, matchInReverseDirection);
            }
          }
          break;
        case DIGIT_QUE:
          currState = currState.getState() != START_STATE ?
                  new StatePos(DIGIT_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(DIGIT_MATCH_ANYWHERE, 0);
          minOcc = tokens.get(tokenPos).getMinOccurrences();
          maxOcc = tokens.get(tokenPos).getMaxOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == DIGIT_MATCHED ? DIGIT_MATCH_NEXT : currState.getState());
            currState = DigitMatcher.match(inputLine, currState, matchInReverseDirection);
            i++;
          } while (i < maxOcc && currState.getState() == DIGIT_MATCHED);
          if (i < minOcc) System.exit(1);
          else if (i == maxOcc && currState.getState() == DIGIT_MATCHED) {
            currState.setState(DIGIT_MATCH_NEXT);
            if (DigitMatcher.match(inputLine, currState, matchInReverseDirection).getState() == DIGIT_MATCHED)
              System.exit(1);
          }
          break;
        case WORD:
          currState = currState.getState() != START_STATE ?
                  new StatePos(WORD_CLASS_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(WORD_CLASS_MATCH_ANYWHERE, 0);
          currState = CharacterClassMatcher.match(inputLine, currState, matchInReverseDirection);
          if (currState.getState() != WORD_CLASS_MATCHED) System.exit(1);
          break;
        case WORD_PLUS:
          currState = currState.getState() != START_STATE ?
                  new StatePos(WORD_CLASS_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(WORD_CLASS_MATCH_ANYWHERE, 0);
          minOcc = tokens.get(tokenPos).getMinOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == DIGIT_MATCHED ? DIGIT_MATCH_NEXT : currState.getState());
            currState = CharacterClassMatcher.match(inputLine, currState, matchInReverseDirection);
            i++;
          } while (i < minOcc && currState.getState() == WORD_CLASS_MATCHED);
          if (currState.getState() != WORD_CLASS_MATCHED) System.exit(1);
          else {
            while (currState.getState() == WORD_CLASS_MATCHED) {
              currState.setState(WORD_CLASS_MATCH_NEXT);
              currState = CharacterClassMatcher.match(inputLine, currState, matchInReverseDirection);
            }
          }
          break;
        case WORD_QUE:
          currState = currState.getState() != START_STATE ?
                  new StatePos(WORD_CLASS_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(WORD_CLASS_MATCH_ANYWHERE, 0);
          minOcc = tokens.get(tokenPos).getMinOccurrences();
          maxOcc = tokens.get(tokenPos).getMaxOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == WORD_CLASS_MATCHED ? WORD_CLASS_MATCH_NEXT : currState.getState());
            currState = DigitMatcher.match(inputLine, currState, matchInReverseDirection);
            i++;
          } while (i < maxOcc && currState.getState() == WORD_CLASS_MATCHED);
          if (i < minOcc) System.exit(1);
          else if (i == maxOcc && currState.getState() == WORD_CLASS_MATCHED) {
            currState.setState(WORD_CLASS_MATCH_NEXT);
            if (CharacterClassMatcher.match(inputLine, currState, matchInReverseDirection).getState() == WORD_CLASS_MATCHED)
              System.exit(1);
          }
          break;
        case POSITIVE_GROUP:
          currState = currState.getState() != START_STATE ?
                  new StatePos(POS_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(POS_GROUP_MATCH_ANYWHERE, 0);
          currState = CharacterGroups.match(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection);
          if (currState.getState() != POS_GROUP_MATCHED) System.exit(1);
          break;
        case POSITIVE_GROUP_PLUS:
          currState = currState.getState() != START_STATE ?
                  new StatePos(POS_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(POS_GROUP_MATCH_ANYWHERE, 0);

          minOcc = tokens.get(tokenPos).getMinOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == POS_GROUP_MATCHED ? POS_GROUP_MATCH_NEXT : currState.getState());
            currState = CharacterGroups.match(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection);
            i++;
          } while (i < minOcc && currState.getState() == POS_GROUP_MATCHED);

          if (currState.getState() != POS_GROUP_MATCHED) System.exit(1);
          else {
            while (currState.getState() == POS_GROUP_MATCHED) {
              currState.setState(POS_GROUP_MATCH_NEXT);
              currState = CharacterGroups.match(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection);
            }
          }
          break;
        case POSITIVE_GROUP_QUE:
          currState = currState.getState() != START_STATE ?
                  new StatePos(POS_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(POS_GROUP_MATCH_ANYWHERE, 0);
          minOcc = tokens.get(tokenPos).getMinOccurrences();
          maxOcc = tokens.get(tokenPos).getMaxOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == POS_GROUP_MATCHED ? POS_GROUP_MATCH_NEXT : currState.getState());
            currState = CharacterGroups.match(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection);
            i++;
          } while (i < maxOcc && currState.getState() == POS_GROUP_MATCHED);
          if (i < minOcc) System.exit(1);
          else if (i == maxOcc && currState.getState() == POS_GROUP_MATCHED) {
            currState.setState(POS_GROUP_MATCH_NEXT);
            if (CharacterGroups.match(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection).getState() == POS_GROUP_MATCHED)
              System.exit(1);
          }
          break;
        case NEGATIVE_GROUP:
          currState = currState.getState() != START_STATE ?
                  new StatePos(NEG_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(NEG_GROUP_MATCH_ANYWHERE, 0);
          currState = CharacterGroups.negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection);
          if (currState.getState() != NEG_GROUP_MATCHED) System.exit(1);
          break;
        case NEGATIVE_GROUP_PLUS:
          currState = currState.getState() != START_STATE ?
                  new StatePos(NEG_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(NEG_GROUP_MATCH_ANYWHERE, 0);
          minOcc = tokens.get(tokenPos).getMinOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == NEG_GROUP_MATCHED ? NEG_GROUP_MATCH_NEXT : currState.getState());
            currState = CharacterGroups.negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection);
            i++;
          } while (i < minOcc && currState.getState() == NEG_GROUP_MATCHED);

          if (currState.getState() != NEG_GROUP_MATCHED) System.exit(1);
          else {
            while (currState.getState() == NEG_GROUP_MATCHED) {
              currState.setState(NEG_GROUP_MATCH_NEXT);
              currState = CharacterGroups.negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection);
            }
          }
          break;
        case NEGATIVE_GROUP_QUE:
          currState = currState.getState() != START_STATE ?
                  new StatePos(NEG_GROUP_MATCH_NEXT, currState.getCurrInputPos()) :
                  new StatePos(NEG_GROUP_MATCH_ANYWHERE, 0);
          minOcc = tokens.get(tokenPos).getMinOccurrences();
          maxOcc = tokens.get(tokenPos).getMaxOccurrences();
          i = 0;
          do {
            currState.setState(currState.getState() == NEG_GROUP_MATCHED ? NEG_GROUP_MATCH_NEXT : currState.getState());
            currState = CharacterGroups.negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection);
            i++;
          } while (i < maxOcc && currState.getState() == NEG_GROUP_MATCHED);
          if (i < minOcc) System.exit(1);
          else if (i == maxOcc && currState.getState() == NEG_GROUP_MATCHED) {
            currState.setState(NEG_GROUP_MATCH_NEXT);
            if (CharacterGroups.negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState, matchInReverseDirection).getState() == NEG_GROUP_MATCHED)
              System.exit(1);
          }
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
              default ->
                      throw new UnsupportedOperationException("Class " + tokens.get(tokenPos).getLexeme() + " not supported yet.");
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
            case LITERAL -> tokens.set(nextTokenPos - 1, new RegexToken(LITERAL_PLUS, tokens.get(nextTokenPos - 1).getValue()));
            case WORD -> tokens.set(nextTokenPos - 1, new RegexToken(WORD_PLUS));
            case POSITIVE_GROUP ->
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), POSITIVE_GROUP_PLUS));
            case NEGATIVE_GROUP ->
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), NEGATIVE_GROUP_PLUS));
            default ->
                    throw new UnsupportedOperationException("Encountered unsupported token " + tokens.get(nextTokenPos - 1).getLexeme() + " before +");
          }
          break;
        case '*':
          switch (tokens.get(nextTokenPos - 1).getLexeme()) {
            case DIGIT -> tokens.set(nextTokenPos - 1, new RegexToken(DIGIT_STAR));
            case LITERAL -> tokens.set(nextTokenPos - 1, new RegexToken(LITERAL_STAR, tokens.get(nextTokenPos - 1).getValue()));
            case WORD -> tokens.set(nextTokenPos - 1, new RegexToken(WORD_STAR));
            case POSITIVE_GROUP ->
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), POSITIVE_GROUP_STAR));
            case NEGATIVE_GROUP ->
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), NEGATIVE_GROUP_STAR));
            default ->
                    throw new UnsupportedOperationException("Encountered unsupported token " + tokens.get(nextTokenPos - 1).getLexeme() + " before *");
          }
          break;
        case '?':
          switch (tokens.get(nextTokenPos - 1).getLexeme()) {
            case DIGIT: tokens.set(nextTokenPos - 1, new RegexToken(DIGIT_QUE)); break;
            case LITERAL: tokens.set(nextTokenPos - 1, new RegexToken(LITERAL_QUE, tokens.get(nextTokenPos - 1).getValue())); break;
            case WORD: tokens.set(nextTokenPos - 1, new RegexToken(WORD_QUE)); break;
            case POSITIVE_GROUP:
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), POSITIVE_GROUP_QUE));
                    break;
            case NEGATIVE_GROUP:
                    tokens.set(nextTokenPos - 1, new RegexToken(tokens.get(nextTokenPos - 1).getGroupPos(), NEGATIVE_GROUP_QUE));
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
        default:
          // literal match
          if (nextTokenPos > 0) {
            // Check if the previous token is LITERAL_PLUS | LITERAL_STAR | LITERAL_QUE and matches their LITERAL value to current value, and increment the minOccurrences if it is.
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
