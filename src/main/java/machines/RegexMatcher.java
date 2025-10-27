package machines;

import java.util.List;

import static machines.RegexLexeme.ENDS_WITH;
import static machines.RegexLexeme.STARTS_WITH;
import static machines.States.*;
import static machines.States.WILDCARD_MATCHED;

public class RegexMatcher {

  public static final List<States> FINAL_STATES =
          List.of(LITERAL_MATCHED, DIGIT_MATCHED, WORD_CLASS_MATCHED,
                  POS_GROUP_MATCHED, NEG_GROUP_MATCHED, WILDCARD_MATCHED, END_STATE);

  public static final List<States> NOT_MATCHED_STATES =
          List.of(LITERAL_NOT_MATCHED, DIGIT_NOT_MATCHED, WORD_CLASS_NOT_MATCHED,
                  POS_GROUP_NOT_MATCHED, NEG_GROUP_NOT_MATCHED, WILDCARD_NOT_MATCHED);

  public static StatePos match(List<RegexToken> tokens, String inputLine, String pattern) {
    StatePos currState = new StatePos(START_STATE, 0);
    currState.setMatchInReverseDirection(tokens.getLast().getLexeme() == ENDS_WITH);
    currState.setState(currState.isMatchInReverseDirection() ? END_STATE :  START_STATE);
    currState.setCurrInputPos(currState.isMatchInReverseDirection() ? inputLine.length() - 1 : 0);
    int prevStartPos = currState.isMatchInReverseDirection() ? inputLine.length() - 2 : -1;
    currState.setPrevMatchStartPos(prevStartPos);

    int tokenPos;

    do {
      currState.setState(START_STATE);
      currState.setPrevMatchStartPos(currState.getPrevMatchStartPos() + 1);
      currState.setCurrInputPos(currState.getPrevMatchStartPos());
      tokenPos = currState.isMatchInReverseDirection() ? tokens.size() - 2 : 0;
      do {
        currState = tokens.get(tokenPos).getLexeme()
                .match(currState, inputLine, pattern, tokens, tokenPos);
        tokenPos = currState.isMatchInReverseDirection() ? tokenPos - 1 : tokenPos + 1;
      } while (tokenPos > -1 && tokenPos < tokens.size() && !NOT_MATCHED_STATES.contains(currState.getState()));
    } while (!currState.isMatchInReverseDirection() && tokens.getFirst().getLexeme() != STARTS_WITH &&
            currState.getPrevMatchStartPos() < inputLine.length() && !FINAL_STATES.contains(currState.getState()));

    return currState;
  }

  public static StatePos match(List<RegexToken> tokens, int tokenPos, String inputLine, String pattern, StatePos currState) {
    int prevTokenPos = tokenPos;
    do {
      currState.setPrevMatchStartPos(currState.getPrevMatchStartPos() + 1);
      currState.setCurrInputPos(currState.getPrevMatchStartPos());
      tokenPos = prevTokenPos;
      do {
        currState = tokens.get(tokenPos).getLexeme()
                .match(currState, inputLine, pattern, tokens, tokenPos);
        tokenPos = currState.isMatchInReverseDirection() ? tokenPos - 1 : tokenPos + 1;
      } while (tokenPos > -1 && tokenPos < tokens.size() && !NOT_MATCHED_STATES.contains(currState.getState()));
    } while (!currState.isMatchInReverseDirection() && tokens.getFirst().getLexeme() != STARTS_WITH &&
            currState.getPrevMatchStartPos() < inputLine.length() && !FINAL_STATES.contains(currState.getState()));

    return currState;
  }
}
