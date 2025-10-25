package machines;

import java.util.ArrayList;
import java.util.List;

public class StatePos {
  private States state;
  private int currInputPos;
  private boolean matchWildCardAtTheEnd;
  private boolean matchInReverseDirection;
  private List<Integer> matchWildStart = new ArrayList<>();
  private List<Integer> matchWildEnds = new ArrayList<>();

  public StatePos(States state, int currInputPos) {
    this.state = state;
    this.currInputPos = currInputPos;
    this.matchWildCardAtTheEnd = false;
    this.matchInReverseDirection = false;
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

  public List<Integer> getMatchWildStart() {
    return matchWildStart;
  }

  public void setMatchWildStart(List<Integer> matchWildStart) {
    this.matchWildStart = matchWildStart;
  }

  public List<Integer> getMatchWildEnds() {
    return matchWildEnds;
  }

  public void setMatchWildEnds(List<Integer> matchWildEnds) {
    this.matchWildEnds = matchWildEnds;
  }
}
