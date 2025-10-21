package machines;

// Should match any alphanumeric character:- A-Za-z0-9_
public class CharacterClassMatcher {
  public static StatePos match(String input, StatePos currentStatePos, boolean reverseDirection) {
    if (currentStatePos == null)
      currentStatePos = new StatePos(States.WORD_CLASS_MATCH_ANYWHERE, !reverseDirection ? 0 : input.length() - 1);
    if (currentStatePos.getCurrInputPos() >= input.length()) {
      return new StatePos(States.WORD_CLASS_NOT_MATCHED, currentStatePos.getCurrInputPos());
    }
    return switch (currentStatePos.getState()) {
      case WORD_CLASS_MATCH_NEXT -> Character.isLetterOrDigit(input.charAt(currentStatePos.getCurrInputPos())) ||
              '_' == input.charAt(currentStatePos.getCurrInputPos()) ?
              new StatePos(States.WORD_CLASS_MATCHED, !reverseDirection ? currentStatePos.getCurrInputPos() + 1 : currentStatePos.getCurrInputPos() - 1) :
              new StatePos(States.WORD_CLASS_NOT_MATCHED, currentStatePos.getCurrInputPos());
      case WORD_CLASS_MATCH_ANYWHERE -> {
        for (int i = 0; i < input.length(); i++) {
          if (Character.isLetterOrDigit(input.charAt(i)) || '_' == input.charAt(i)) {
            yield new StatePos(States.WORD_CLASS_MATCHED, currentStatePos.getCurrInputPos() + 1);
          }
        }
        yield new StatePos(States.WORD_CLASS_NOT_MATCHED, currentStatePos.getCurrInputPos());
      }
      default -> new StatePos(States.WORD_CLASS_NOT_MATCHED, currentStatePos.getCurrInputPos());
    };
  }

  public static StatePos match(String input, boolean reverseDirection) {
    return match(input, new StatePos(States.WORD_CLASS_MATCH_ANYWHERE, 0), reverseDirection);
  }
}
