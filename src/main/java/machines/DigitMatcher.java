package machines;

public class DigitMatcher {
    public static StatePos match(String input, StatePos currentStatePos) {
        if (currentStatePos == null) currentStatePos = new StatePos(States.DIGIT_MATCH_ANYWHERE, 0);
        if (currentStatePos.getCurrInputPos() >= input.length()) {
            return new StatePos(States.DIGIT_NOT_MATCHED, currentStatePos.getCurrInputPos());
        }
        return switch (currentStatePos.getState()) {
            case DIGIT_MATCH_NEXT -> Character.isDigit(input.charAt(currentStatePos.getCurrInputPos())) ?
                    new StatePos(States.DIGIT_MATCHED, currentStatePos.getCurrInputPos() + 1) :
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

    public static StatePos match(String input) {
        return match(input, new StatePos(States.DIGIT_MATCH_ANYWHERE, 0));
    }
}
