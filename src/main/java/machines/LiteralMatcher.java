package machines;

public class LiteralMatcher {
    public static StatePos match(String input, char pattern, StatePos currentStatePos, boolean reverseDirection) {
        if (currentStatePos == null) currentStatePos = new StatePos(States.LITERAL_MATCH_ANYWHERE, !reverseDirection ? 0 : input.length() - 1);
        if (currentStatePos.getCurrInputPos() >= input.length()) {
            return new StatePos(States.LITERAL_NOT_MATCHED, currentStatePos.getCurrInputPos());
        }
        return switch (currentStatePos.getState()) {
            case LITERAL_MATCH_NEXT -> input.charAt(currentStatePos.getCurrInputPos()) == pattern ?
                    new StatePos(States.LITERAL_MATCHED, !reverseDirection ? currentStatePos.getCurrInputPos() + 1 : currentStatePos.getCurrInputPos() - 1)
                    : new StatePos(States.LITERAL_NOT_MATCHED, currentStatePos.getCurrInputPos());
            case LITERAL_MATCH_ANYWHERE -> {
                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == pattern) {
                        yield new StatePos(States.LITERAL_MATCHED, i + 1);
                    }
                }
                yield new StatePos(States.LITERAL_NOT_MATCHED, currentStatePos.getCurrInputPos());
            }
            default -> new StatePos(States.LITERAL_NOT_MATCHED, currentStatePos.getCurrInputPos());
        };
    }

    public static StatePos match(String input, char pattern, boolean reverseDirection) {
        return match(input, pattern, new StatePos(States.LITERAL_MATCH_ANYWHERE, 0), reverseDirection);
    }
}
