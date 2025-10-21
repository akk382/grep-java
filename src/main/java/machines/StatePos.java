package machines;

public class StatePos {
  private States state;
  private int currInputPos;

  public StatePos(States state, int currInputPos) {
    this.state = state;
    this.currInputPos = currInputPos;
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

  @Override
  public String toString() {
    return "StatePos{" +
            "state=" + state +
            ", currInputPos=" + currInputPos +
            '}';
  }
}
