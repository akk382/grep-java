package machines;

public class StatePos {
  private States state;
  private int currInputPos;
  private int matchStartPos;
  private boolean matchWildCardAtTheEnd;
  private boolean matchInReverseDirection;

  public StatePos(States state, int currInputPos) {
    this.state = state;
    this.currInputPos = currInputPos;
    this.matchStartPos = 0;
    this.matchWildCardAtTheEnd = false;
    this.matchInReverseDirection = false;
  }

  public StatePos(StatePos oldState) {
    this.state = oldState.state;
    this.currInputPos = oldState.currInputPos;
    this.matchStartPos = oldState.matchStartPos;
    this.matchWildCardAtTheEnd = oldState.matchWildCardAtTheEnd;
    this.matchInReverseDirection = oldState.matchInReverseDirection;
  }

  public StatePos(States state, int currInputPos, int matchStartPos) {
    this.state = state;
    this.currInputPos = currInputPos;
    this.matchStartPos = matchStartPos;
    this.matchWildCardAtTheEnd = false;
    this.matchInReverseDirection = false;
  }

  public StatePos(States state, int currInputPos, int matchStartPos, boolean matchWildCardAtTheEnd) {
    this.state = state;
    this.currInputPos = currInputPos;
    this.matchStartPos = matchStartPos;
    this.matchWildCardAtTheEnd = matchWildCardAtTheEnd;
    this.matchInReverseDirection = false;
  }

  public StatePos(States state, int currInputPos, int matchStartPos,
                  boolean matchWildCardAtTheEnd, boolean matchInReverseDirection) {
    this.state = state;
    this.currInputPos = currInputPos;
    this.matchStartPos = matchStartPos;
    this.matchWildCardAtTheEnd = matchWildCardAtTheEnd;
    this.matchInReverseDirection = matchInReverseDirection;
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

  public int getMatchStartPos() {
    return matchStartPos;
  }

  public void setMatchStartPos(int matchStartPos) {
    this.matchStartPos = matchStartPos;
  }

  public boolean isMatchWildCardAtTheEnd() {
    return matchWildCardAtTheEnd;
  }

  public void setMatchWildCardAtTheEnd(boolean matchWildCardAtTheEnd) {
    this.matchWildCardAtTheEnd = matchWildCardAtTheEnd;
  }

  public boolean isMatchInReverseDirection() {
    return matchInReverseDirection;
  }

  public void setMatchInReverseDirection(boolean matchInReverseDirection) {
    this.matchInReverseDirection = matchInReverseDirection;
  }

  @Override
  public String toString() {
    return "StatePos{" +
            "state=" + state +
            ", currInputPos=" + currInputPos +
            ", matchStartPos=" + matchStartPos +
            ", matchWildCardAtTheEnd=" + matchWildCardAtTheEnd +
            ", matchInReverseDirection=" + matchInReverseDirection +
            '}';
  }
}
