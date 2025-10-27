package machines;

//class Pair {
//  int val1;
//  int val2;
//
//  public Pair(int val1, int val2) {
//    this.val1 = val1;
//    this.val2 = val2;
//  }
//}

public class StatePos {
  private States state;
  private int currInputPos;
  private int prevMatchStartPos;
  private boolean matchInReverseDirection;
//  private boolean matchWildCardAtTheEnd;
//  private Map<RegexLexeme, List<Pair>> matchWildsWithPositions = new HashMap<>();

  public StatePos(States state, int currInputPos) {
    this.state = state;
    this.currInputPos = currInputPos;
    this.prevMatchStartPos = 0;
    this.matchInReverseDirection = false;
//    this.matchWildCardAtTheEnd = false;
  }

  public States getState() {
    return state;
  }

  public void setState(States state) {
    this.state = state;
  }

  public int getCurrInputPos() {
    return currInputPos;
  }

  public void setCurrInputPos(int currInputPos) {
    this.currInputPos = currInputPos;
  }

  public int getPrevMatchStartPos() {
    return prevMatchStartPos;
  }

  public void setPrevMatchStartPos(int prevMatchStartPos) {
    this.prevMatchStartPos = prevMatchStartPos;
  }
//  public boolean isMatchWildCardAtTheEnd() {
//    return matchWildCardAtTheEnd;
//  }
//
//  public void setMatchWildCardAtTheEnd(boolean matchWildCardAtTheEnd) {
//    this.matchWildCardAtTheEnd = matchWildCardAtTheEnd;
//  }

  public boolean isMatchInReverseDirection() {
    return matchInReverseDirection;
  }

  public void setMatchInReverseDirection(boolean matchInReverseDirection) {
    this.matchInReverseDirection = matchInReverseDirection;
  }

//  public Map<RegexLexeme, List<Pair>> getMatchWildsWithPositions() {
//    return matchWildsWithPositions;
//  }
//
//  public void setMatchWildsWithPositions(Map<RegexLexeme, List<Pair>> matchWildsWithPositions) {
//    this.matchWildsWithPositions = matchWildsWithPositions;
//  }
}
