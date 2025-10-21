package machines;

public class DigitMatcher {
    public static StatePos match(String input, StatePos currentStatePos, boolean reverseDirection) {
        if (currentStatePos == null) currentStatePos = new StatePos(States.DIGIT_MATCH_ANYWHERE, !reverseDirection ? 0 : input.length() - 1);
        if (currentStatePos.getCurrInputPos() >= input.length()) {
            return new StatePos(States.DIGIT_NOT_MATCHED, currentStatePos.getCurrInputPos());
        }
        return switch (currentStatePos.getState()) {
            case DIGIT_MATCH_NEXT -> Character.isDigit(input.charAt(currentStatePos.getCurrInputPos())) ?
                    new StatePos(States.DIGIT_MATCHED, !reverseDirection ? currentStatePos.getCurrInputPos() + 1 : currentStatePos.getCurrInputPos() - 1) :
                    new StatePos(States.DIGIT_NOT_MATCHED, currentStatePos.getCurrInputPos());
            case DIGIT_MATCH_ANYWHERE -> {
                for (int i = 0; i < input.length(); i++) {
                    if (Character.isDigit(input.charAt(i))) {
                        yield new StatePos(States.DIGIT_MATCHED, i + 1);
                    }
                }
                yield new StatePos(States.DIGIT_NOT_MATCHED, currentStatePos.getCurrInputPos());
            }
            default -> new StatePos(States.DIGIT_NOT_MATCHED, currentStatePos.getCurrInputPos());
        };
    }

    public static StatePos match(String input, boolean reverseDirection) {
        return match(input, new StatePos(States.DIGIT_MATCH_ANYWHERE, 0), reverseDirection);
    }
}
