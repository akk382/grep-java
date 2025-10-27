package machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static machines.States.*;

public enum RegexLexeme {
  STARTS_WITH {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      if (currState.isMatchInReverseDirection()) {
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
      return currState;
    }
  },

  LITERAL {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      if (currState.getState() != START_STATE || currState.getState() != LITERAL_MATCH_ANYWHERE) {
        currState.setState(LITERAL_MATCH_NEXT);
      } else {
        currState.setState(LITERAL_MATCH_ANYWHERE);
      }
      currState = literalMatcher(inputLine, tokens.get(tokenPos).getValue(), currState);

      return currState;
    }
  },
  LITERAL_PLUS {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      if (currState.getState() != START_STATE || currState.getState() != LITERAL_MATCH_ANYWHERE) {
        currState.setState(LITERAL_MATCH_NEXT);
      } else {
        currState.setState(LITERAL_MATCH_ANYWHERE);
      }
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == LITERAL_MATCHED ? LITERAL_MATCH_NEXT : currState.getState());
        currState = literalMatcher(inputLine, tokens.get(tokenPos).getValue(), currState);
        i++;
      } while (i < minOcc && currState.getState() == LITERAL_MATCHED);
      if (i == minOcc && currState.getState() == LITERAL_MATCHED) {
        while (currState.getState() == LITERAL_MATCHED) {
          currState.setState(LITERAL_MATCH_NEXT);
          currState = literalMatcher(inputLine, tokens.get(tokenPos).getValue(), currState);
        }
        currState.setState(LITERAL_MATCHED);
      }
      return currState;
    }
  }, // +: one or more
  LITERAL_STAR {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      throw new UnsupportedOperationException("Not Implemented Yet.");
    }
  }, // *: zero or more
  LITERAL_QUE {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != LITERAL_MATCH_ANYWHERE ? LITERAL_MATCH_NEXT : LITERAL_MATCH_ANYWHERE);
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int maxOcc = tokens.get(tokenPos).getMaxOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == LITERAL_MATCHED ? LITERAL_MATCH_NEXT : currState.getState());
        currState = literalMatcher(inputLine, tokens.get(tokenPos).getValue(), currState);
        i++;
      } while (i < maxOcc && currState.getState() == LITERAL_MATCHED);
      if (i < minOcc) currState.setState(LITERAL_NOT_MATCHED);
      else currState.setState(LITERAL_MATCHED);
      return currState;
    }
  }, // ?: one or none

  DIGIT {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != DIGIT_MATCH_ANYWHERE ? DIGIT_MATCH_NEXT : DIGIT_MATCH_ANYWHERE);
      currState = digitMatcher(currState, inputLine);
      return currState;
    }
  },
  DIGIT_PLUS {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != DIGIT_MATCH_ANYWHERE ? DIGIT_MATCH_NEXT : DIGIT_MATCH_ANYWHERE);
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == DIGIT_MATCHED ? DIGIT_MATCH_NEXT : currState.getState());
        currState = digitMatcher(currState, inputLine);
        i++;
      } while (i < minOcc && currState.getState() == DIGIT_MATCHED);
      if (i == minOcc && currState.getState() == DIGIT_MATCHED) {
        while (currState.getState() == DIGIT_MATCHED) {
          currState.setState(DIGIT_MATCH_NEXT);
          currState = digitMatcher(currState, inputLine);
        }
        currState.setState(DIGIT_MATCHED);
      }
      return currState;
    }
  },
  DIGIT_STAR {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      throw new UnsupportedOperationException("Not Implemented Yet.");
    }
  },
  DIGIT_QUE {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != DIGIT_MATCH_ANYWHERE ? DIGIT_MATCH_NEXT : DIGIT_MATCH_ANYWHERE);
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int maxOcc = tokens.get(tokenPos).getMaxOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == DIGIT_MATCHED ? DIGIT_MATCH_NEXT : currState.getState());
        currState = digitMatcher(currState, inputLine);
        i++;
      } while (i < maxOcc && currState.getState() == DIGIT_MATCHED);
      if (i < minOcc) currState.setState(DIGIT_NOT_MATCHED);
      else currState.setState(DIGIT_MATCHED);
      return currState;
    }
  },

  NON_DIGIT {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      throw new UnsupportedOperationException("Not Implemented Yet.");
    }
  },

  WORD {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != WORD_CLASS_MATCH_ANYWHERE ? WORD_CLASS_MATCH_NEXT : WORD_CLASS_MATCH_ANYWHERE);
      currState = characterMatcher(inputLine, currState);
      return currState;
    }
  },
  WORD_PLUS {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != WORD_CLASS_MATCH_ANYWHERE ? WORD_CLASS_MATCH_NEXT : WORD_CLASS_MATCH_ANYWHERE);
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == DIGIT_MATCHED ? DIGIT_MATCH_NEXT : currState.getState());
        currState = characterMatcher(inputLine, currState);
        i++;
      } while (i < minOcc && currState.getState() == WORD_CLASS_MATCHED);
      if (i == minOcc && currState.getState() == WORD_CLASS_MATCHED) {
        while (currState.getState() == WORD_CLASS_MATCHED) {
          currState.setState(WORD_CLASS_MATCH_NEXT);
          currState = characterMatcher(inputLine, currState);
        }
        currState.setState(WORD_CLASS_MATCHED);
      }
      return currState;
    }
  },
  WORD_STAR {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      throw new UnsupportedOperationException("Not Implemented Yet.");
    }
  },
  WORD_QUE {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != WORD_CLASS_MATCH_ANYWHERE ? WORD_CLASS_MATCH_NEXT : WORD_CLASS_MATCH_ANYWHERE);
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int maxOcc = tokens.get(tokenPos).getMaxOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == WORD_CLASS_MATCHED ? WORD_CLASS_MATCH_NEXT : currState.getState());
        currState = digitMatcher(currState, inputLine);
        i++;
      } while (i < maxOcc && currState.getState() == WORD_CLASS_MATCHED);
      if (i < minOcc) currState.setState(WORD_CLASS_NOT_MATCHED);
      else currState.setState(WORD_CLASS_MATCHED);
      return currState;
    }
  },

  NON_WORD {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      throw new UnsupportedOperationException("Not Implemented Yet.");
    }
  },

  POSITIVE_GROUP {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != POS_GROUP_MATCH_ANYWHERE ? POS_GROUP_MATCH_NEXT : POS_GROUP_MATCH_ANYWHERE);
      currState = positiveMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState);
      return currState;
    }
  },
  POSITIVE_GROUP_PLUS {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != POS_GROUP_MATCH_ANYWHERE ? POS_GROUP_MATCH_NEXT : POS_GROUP_MATCH_ANYWHERE);
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == POS_GROUP_MATCHED ? POS_GROUP_MATCH_NEXT : currState.getState());
        currState = positiveMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState);
        i++;
      } while (i < minOcc && currState.getState() == POS_GROUP_MATCHED);

      if (i == minOcc && currState.getState() == POS_GROUP_MATCHED) {
        while (currState.getState() == POS_GROUP_MATCHED) {
          currState.setState(POS_GROUP_MATCH_NEXT);
          currState = positiveMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState);
        }
        currState.setState(POS_GROUP_MATCHED);
      }
      return currState;
    }
  },
  POSITIVE_GROUP_STAR {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      throw new UnsupportedOperationException("Not Implemented Yet.");
    }
  },
  POSITIVE_GROUP_QUE {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != POS_GROUP_MATCH_ANYWHERE ? POS_GROUP_MATCH_NEXT : POS_GROUP_MATCH_ANYWHERE);
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int maxOcc = tokens.get(tokenPos).getMaxOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == POS_GROUP_MATCHED ? POS_GROUP_MATCH_NEXT : currState.getState());
        currState = positiveMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState);
        i++;
      } while (i < maxOcc && currState.getState() == POS_GROUP_MATCHED);
      if (i < minOcc) currState.setState(POS_GROUP_NOT_MATCHED);
      else currState.setState(POS_GROUP_MATCHED);
      return currState;
    }
  },

  NEGATIVE_GROUP {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != NEG_GROUP_MATCH_ANYWHERE ? NEG_GROUP_MATCH_NEXT : NEG_GROUP_MATCH_ANYWHERE);
      currState = negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState);
      return currState;
    }
  },
  NEGATIVE_GROUP_PLUS {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != NEG_GROUP_MATCH_ANYWHERE ? NEG_GROUP_MATCH_NEXT : NEG_GROUP_MATCH_ANYWHERE);
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == NEG_GROUP_MATCHED ? NEG_GROUP_MATCH_NEXT : currState.getState());
        currState = negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState);
        i++;
      } while (i < minOcc && currState.getState() == NEG_GROUP_MATCHED);

      if (i == minOcc && currState.getState() == NEG_GROUP_MATCHED) {
        while (currState.getState() == NEG_GROUP_MATCHED) {
          currState.setState(NEG_GROUP_MATCH_NEXT);
          currState = negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState);
        }
        currState.setState(NEG_GROUP_MATCHED);
      }
      return currState;
    }
  },
  NEGATIVE_GROUP_STAR {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      throw new UnsupportedOperationException("Not Implemented Yet.");
    }
  },
  NEGATIVE_GROUP_QUE {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      currState.setState(currState.getState() != START_STATE ||
              currState.getState() != NEG_GROUP_MATCH_ANYWHERE ? NEG_GROUP_MATCH_NEXT : NEG_GROUP_MATCH_ANYWHERE);
      int minOcc = tokens.get(tokenPos).getMinOccurrences();
      int maxOcc = tokens.get(tokenPos).getMaxOccurrences();
      int i = 0;
      do {
        currState.setState(currState.getState() == NEG_GROUP_MATCHED ? NEG_GROUP_MATCH_NEXT : currState.getState());
        currState = negativeMatch(inputLine, pattern.substring(tokens.get(tokenPos).getGroupPos()), currState);
        i++;
      } while (i < maxOcc && currState.getState() == NEG_GROUP_MATCHED);
      if (i < minOcc) currState.setState(NEG_GROUP_NOT_MATCHED);
      else currState.setState(NEG_GROUP_MATCHED);
      return currState;
    }
  },

  BACK_SLASH {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      throw new UnsupportedOperationException("Not Implemented Yet.");
    }
  },

  WILD_CARD {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      if (currState.getState() == START_STATE) {
        if (currState.isMatchInReverseDirection() && inputLine.charAt(currState.getCurrInputPos()) == '\n') {
          System.exit(1);
        } else if (currState.isMatchInReverseDirection()) {
          currState.setPrevMatchStartPos(currState.getCurrInputPos());
          currState.setCurrInputPos(currState.getCurrInputPos() - 1);
          currState.setState(WILDCARD_MATCHED);
          return currState;
        } else {
          while (currState.getCurrInputPos() < inputLine.length() &&
                  inputLine.charAt(currState.getCurrInputPos()) == '\n') {
            currState.setCurrInputPos(currState.getCurrInputPos() + 1);
          }
          if (currState.getCurrInputPos() < inputLine.length() &&
                  inputLine.charAt(currState.getCurrInputPos()) != '\n') {
            currState.setPrevMatchStartPos(currState.getCurrInputPos());
            currState.setCurrInputPos(currState.getCurrInputPos() + 1);
            currState.setState(WILDCARD_MATCHED);
          } else {
            System.exit(1);
          }
        }
      } else if (inputLine.charAt(currState.getCurrInputPos()) == '\n') {
        currState.setState(WILDCARD_NOT_MATCHED);
      } else {
        currState.setState(WILDCARD_MATCHED);
        currState.setCurrInputPos(currState.getCurrInputPos() + 1);
      }
      return currState;
    }
  }, // '.' -> matches any character except new line.
  WILD_CARD_PLUS {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {

      // Can be at the start
      // try to find the regex from next class and match the start poss at the end

      // Can be in the middle
      // if matched, try it and check to match from next with anywhere.
      // else reject

      // Can be at the end
      // if matched, try it and check to match from next with anywhere.
      // else reject

      if (currState.getState() == START_STATE) {
        if (!currState.isMatchInReverseDirection()) {
          currState.setState(getNextState(tokens, tokenPos + 1));
          currState.setPrevMatchStartPos(currState.getCurrInputPos());
          currState.setCurrInputPos(currState.getCurrInputPos());
          currState = RegexMatcher.match(tokens, tokenPos + 1, inputLine, pattern, currState);
          if (currState.getPrevMatchStartPos() - 1 >= 0 && inputLine.charAt(currState.getPrevMatchStartPos() - 1) != '\n') {
            currState.setState(WILDCARD_MATCHED);
            System.exit(0);
          } else {
            currState.setState(WILDCARD_NOT_MATCHED);
            System.exit(1);
          }
        } else {
          if (inputLine.charAt(currState.getCurrInputPos()) == '\n') System.exit(1);
          else {
            // TODO: fix it when we match in reverse direction.
          }
        }
      } else {
        if (inputLine.charAt(currState.getCurrInputPos()) != '\n') {
          int currPos = currState.getCurrInputPos();
          currState.setState(getNextState(tokens, tokenPos + 1));
          currState.setPrevMatchStartPos(currState.getCurrInputPos());
          currState.setCurrInputPos(currState.getCurrInputPos());
          currState = RegexMatcher.match(tokens, tokenPos + 1, inputLine, pattern, currState);
          for (int i = currPos + 1; i < currState.getPrevMatchStartPos(); i++) {
            if (inputLine.charAt(i) == '\n') System.exit(1);
          }
          System.exit(0);
        } else {
          System.exit(1);
        }
      }
      return currState;
    }

    private States getNextState(List<RegexToken> tokens, int tokenPos) {
      if (tokenPos + 1 < tokens.size()) {
        return switch (tokens.get(tokenPos + 1).getLexeme()) {
          case DIGIT, DIGIT_PLUS, DIGIT_STAR, DIGIT_QUE -> DIGIT_MATCH_ANYWHERE;
          case LITERAL, LITERAL_PLUS, LITERAL_STAR, LITERAL_QUE -> LITERAL_MATCH_ANYWHERE;
          case WORD, WORD_PLUS, WORD_STAR, WORD_QUE -> WORD_CLASS_MATCH_ANYWHERE;
          case WILD_CARD, WILD_CARD_PLUS, WILD_CARD_STAR, WILD_CARD_QUE -> WILDCARD_MATCH_ANYWHERE;
          case POSITIVE_GROUP, POSITIVE_GROUP_PLUS, POSITIVE_GROUP_STAR, POSITIVE_GROUP_QUE -> POS_GROUP_MATCH_ANYWHERE;
          case NEGATIVE_GROUP, NEGATIVE_GROUP_PLUS, NEGATIVE_GROUP_STAR, NEGATIVE_GROUP_QUE -> NEG_GROUP_MATCHED;
          default -> NULL_STATE;
        };
      }
      return null;
    }
  },
  WILD_CARD_STAR {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      throw new UnsupportedOperationException("Not Implemented Yet.");
    }
  },
  WILD_CARD_QUE {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      if (currState.getState() == START_STATE) {
//        currState.setMatchWildCardAtTheEnd(true);
      } else if (inputLine.charAt(currState.getCurrInputPos()) != '\n') {
        currState.setState(WILDCARD_MATCHED);
        currState.setCurrInputPos(currState.getCurrInputPos() + 1);
      } else {
        currState.setState(WILDCARD_MATCHED);
      }
      return currState;
    }
  },

  ENDS_WITH {
    @Override
    public StatePos match(StatePos currState, String inputLine, String pattern, List<RegexToken> tokens, int tokenPos) {
      return currState;
    }
  };

  public abstract StatePos match(StatePos currState, String inputLine, String pattern,
                                 List<RegexToken> tokens, int tokenPos);

  public StatePos negativeMatch(String input, String patternFromNextToBrace, StatePos currStatePos) {
    if (!List.of(NEG_GROUP_MATCHED, NEG_GROUP_MATCH_NEXT, NEG_GROUP_MATCH_ANYWHERE, NEG_GROUP_NOT_MATCHED)
            .contains(currStatePos.getState())) {
      throw new UnsupportedOperationException("This method only supports negative group matcher.");
    }
    if (currStatePos.getCurrInputPos() >= input.length()) {
      currStatePos.setState(States.NEG_GROUP_NOT_MATCHED);
      return currStatePos;
    }
    Map<CaptureGroupType, List<Character>> captureGroups = extractCaptureGroup(patternFromNextToBrace);

    return switch (currStatePos.getState()) {
      case NEG_GROUP_MATCH_ANYWHERE -> {
        for (int i = currStatePos.getCurrInputPos(); i < input.length(); i++) {
          if (!captureGroups.get(CaptureGroupType.CHAR).contains(input.charAt(i))) {
            currStatePos.setState(States.NEG_GROUP_MATCHED);
            currStatePos.setCurrInputPos(i + 1);
            currStatePos.setPrevMatchStartPos(i);
            yield currStatePos;
          }
        }
        currStatePos.setState(States.NEG_GROUP_NOT_MATCHED);
        yield currStatePos;
      }
      case NEG_GROUP_MATCH_NEXT -> {
        if (!captureGroups.get(CaptureGroupType.CHAR).contains(input.charAt(currStatePos.getCurrInputPos()))) {
          currStatePos.setState(States.NEG_GROUP_MATCHED);
          currStatePos.setCurrInputPos(!currStatePos.isMatchInReverseDirection() ?
                  currStatePos.getCurrInputPos() + 1 : currStatePos.getCurrInputPos() - 1);
          yield currStatePos;
        }
        currStatePos.setState(States.NEG_GROUP_NOT_MATCHED);
        yield currStatePos;
      }
      default -> {
        currStatePos.setState(States.NEG_GROUP_NOT_MATCHED);
        yield currStatePos;
      }
    };
  }

  // TODO: Remove dependency on reverseDirection and other params, except inout, currStatePos.
  public StatePos positiveMatch(String input, String patternFromNextToBrace, StatePos currStatePos) {
    if (!List.of(POS_GROUP_MATCHED, POS_GROUP_MATCH_NEXT, POS_GROUP_MATCH_ANYWHERE, POS_GROUP_NOT_MATCHED)
            .contains(currStatePos.getState())) {
      throw new UnsupportedOperationException("This method only supports positive group matcher.");
    }
    if (currStatePos.getCurrInputPos() >= input.length()) {
      currStatePos.setState(States.POS_GROUP_NOT_MATCHED);
      return currStatePos;
    }
    Map<CaptureGroupType, List<Character>> captureGroups = extractCaptureGroup(patternFromNextToBrace);

    return switch (currStatePos.getState()) {
      case POS_GROUP_MATCH_ANYWHERE -> {
        for (int i = currStatePos.getCurrInputPos(); i < input.length(); i++) {
          if (captureGroups.get(CaptureGroupType.CHAR).contains(input.charAt(i))) {
            currStatePos.setState(States.POS_GROUP_MATCHED);
            currStatePos.setCurrInputPos(i + 1);
            currStatePos.setPrevMatchStartPos(i);
            yield currStatePos;
          }
        }
        currStatePos.setState(States.POS_GROUP_NOT_MATCHED);
        yield currStatePos;
      }
      case POS_GROUP_MATCH_NEXT -> {
        if (captureGroups.get(CaptureGroupType.CHAR).contains(input.charAt(currStatePos.getCurrInputPos()))) {
          currStatePos.setState(States.POS_GROUP_MATCHED);
          currStatePos.setCurrInputPos(!currStatePos.isMatchInReverseDirection() ?
                  currStatePos.getCurrInputPos() + 1 : currStatePos.getCurrInputPos() - 1);
          yield currStatePos;
        }
        currStatePos.setState(States.POS_GROUP_NOT_MATCHED);
        yield currStatePos;
      }
      default -> {
        currStatePos.setState(States.POS_GROUP_NOT_MATCHED);
        yield currStatePos;
      }
    };
  }

  private Map<CaptureGroupType, List<Character>> extractCaptureGroup(String patternFromNextToBrace) {
    int i = 0;
    Map<CaptureGroupType, List<Character>> captureGroups = new HashMap<>();
    while (patternFromNextToBrace.charAt(i) != ']') {
      char c = patternFromNextToBrace.charAt(i);
      List<Character> characters = captureGroups.getOrDefault(CaptureGroupType.CHAR, new ArrayList<>());
      characters.add(c);
      captureGroups.put(CaptureGroupType.CHAR, characters);
//            if (c == '-') {} // We will deal with this later
      i++;
    }
    return captureGroups;
  }

  public StatePos characterMatcher(String input, StatePos currentStatePos) {
    if (!List.of(WORD_CLASS_MATCH_ANYWHERE, WORD_CLASS_MATCH_NEXT, WORD_CLASS_MATCHED, WORD_CLASS_NOT_MATCHED)
            .contains(currentStatePos.getState())) {
      throw new UnsupportedOperationException("This method only supports WORD class matcher.");
    }
    if (currentStatePos.getCurrInputPos() >= input.length()) {
      currentStatePos.setState(States.WORD_CLASS_NOT_MATCHED);
      return currentStatePos;
    }
    return switch (currentStatePos.getState()) {
      case WORD_CLASS_MATCH_NEXT -> {
        if (Character.isLetterOrDigit(input.charAt(currentStatePos.getCurrInputPos())) ||
                '_' == input.charAt(currentStatePos.getCurrInputPos())) {
          currentStatePos.setState(States.WORD_CLASS_MATCHED);
          currentStatePos.setCurrInputPos(!currentStatePos.isMatchInReverseDirection() ?
                  currentStatePos.getCurrInputPos() + 1 : currentStatePos.getCurrInputPos() - 1);
          yield currentStatePos;
        }
        currentStatePos.setState(States.WORD_CLASS_NOT_MATCHED);
        yield currentStatePos;
      }
      case WORD_CLASS_MATCH_ANYWHERE -> {
        for (int i = currentStatePos.getCurrInputPos(); i < input.length(); i++) {
          if (Character.isLetterOrDigit(input.charAt(i)) || '_' == input.charAt(i)) {
            currentStatePos.setState(States.WORD_CLASS_MATCHED);
            currentStatePos.setCurrInputPos(i + 1);
            currentStatePos.setPrevMatchStartPos(i);
            yield currentStatePos;
          }
        }
        currentStatePos.setState(States.WORD_CLASS_NOT_MATCHED);
        yield currentStatePos;
      }
      default -> {
        currentStatePos.setState(States.WORD_CLASS_NOT_MATCHED);
        yield currentStatePos;
      }
    };
  }

  public StatePos digitMatcher(StatePos currentStatePos, String input) {
    if (!List.of(DIGIT_MATCH_ANYWHERE, DIGIT_MATCH_NEXT, DIGIT_NOT_MATCHED, DIGIT_MATCHED)
            .contains(currentStatePos.getState())) {
      throw new UnsupportedOperationException("This method only supports DIGIT matcher.");
    }
    if (currentStatePos.getCurrInputPos() >= input.length()) {
      currentStatePos.setState(States.DIGIT_NOT_MATCHED);
    }
    return switch (currentStatePos.getState()) {
      case DIGIT_MATCH_NEXT: {
        if (Character.isDigit(input.charAt(currentStatePos.getCurrInputPos()))) {
          currentStatePos.setState(States.DIGIT_MATCHED);
          currentStatePos.setCurrInputPos(!currentStatePos.isMatchInReverseDirection() ?
                  currentStatePos.getCurrInputPos() + 1 : currentStatePos.getCurrInputPos() - 1);
          yield currentStatePos;
        } else {
          currentStatePos.setState(States.DIGIT_NOT_MATCHED);
          yield currentStatePos;
        }
      }
      case DIGIT_MATCH_ANYWHERE: {
        for (int i = currentStatePos.getCurrInputPos(); i < input.length(); i++) {
          if (Character.isDigit(input.charAt(i))) {
            currentStatePos.setState(States.DIGIT_MATCHED);
            currentStatePos.setCurrInputPos(i + 1);
            currentStatePos.setPrevMatchStartPos(i);
            yield currentStatePos;
          }
        }
        currentStatePos.setState(States.DIGIT_NOT_MATCHED);
        yield currentStatePos;
      }
      default: {
        currentStatePos.setState(States.DIGIT_NOT_MATCHED);
        yield currentStatePos;
      }
    };
  }

  public StatePos literalMatcher(String input, char pattern, StatePos currentStatePos) {
    if (!List.of(LITERAL_MATCHED, LITERAL_NOT_MATCHED, LITERAL_MATCH_ANYWHERE, LITERAL_MATCH_NEXT)
            .contains(currentStatePos.getState())) {
      throw new UnsupportedOperationException("This method only supports LITERAL matcher.");
    }
    if (currentStatePos.getCurrInputPos() >= input.length()) {
      currentStatePos.setState(LITERAL_NOT_MATCHED);
    }
    return switch (currentStatePos.getState()) {
      case LITERAL_MATCH_NEXT: {
        if (input.charAt(currentStatePos.getCurrInputPos()) == pattern) {
          currentStatePos.setState(States.LITERAL_MATCHED);
          currentStatePos.setCurrInputPos(!currentStatePos.isMatchInReverseDirection() ?
                  currentStatePos.getCurrInputPos() + 1 : currentStatePos.getCurrInputPos() - 1);
        } else {
          currentStatePos.setState(States.LITERAL_NOT_MATCHED);
          currentStatePos.setCurrInputPos(currentStatePos.getCurrInputPos());
        }
        yield currentStatePos;
      }
      case LITERAL_MATCH_ANYWHERE: {
        for (int i = currentStatePos.getCurrInputPos(); i < input.length(); i++) {
          if (input.charAt(i) == pattern) {
            currentStatePos.setState(States.LITERAL_MATCHED);
            currentStatePos.setCurrInputPos(i + 1);
            currentStatePos.setPrevMatchStartPos(i);
            yield currentStatePos;
          }
        }
        currentStatePos.setState(States.LITERAL_NOT_MATCHED);
        yield currentStatePos;
      }
      default: {
        currentStatePos.setState(States.LITERAL_NOT_MATCHED);
        yield currentStatePos;
      }
    };
  }
}
