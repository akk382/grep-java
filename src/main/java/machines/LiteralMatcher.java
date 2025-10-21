package machines;

public class LiteralMatcher {
    public static StatePos match(String input, char pattern, StatePos currentStatePos) {
        if (currentStatePos == null) currentStatePos = new StatePos(States.LITERAL_MATCH_ANYWHERE, 0);
        if (currentStatePos.getCurrInputPos() >= input.length()) {
            return new StatePos(States.LITERAL_NOT_MATCHED, currentStatePos.getCurrInputPos());
        }
        return switch (currentStatePos.getState()) {
            case LITERAL_MATCH_NEXT -> input.charAt(currentStatePos.getCurrInputPos()) == pattern ?
                    new StatePos(States.LITERAL_MATCHED, currentStatePos.getCurrInputPos() + 1)
                    : new StatePos(States.LITERAL_NOT_MATCHED, currentStatePos.getCurrInputPos());
            case LITERAL_MATCH_ANYWHERE -> {
                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == pattern) {
                        yield new StatePos(States.LITERAL_MATCHED, currentStatePos.getCurrInputPos() + 1);
                    }
                }
                yield new StatePos(States.LITERAL_NOT_MATCHED, currentStatePos.getCurrInputPos());
            }
            default -> new StatePos(States.LITERAL_NOT_MATCHED, currentStatePos.getCurrInputPos());
        };
    }

    public static StatePos match(String input, char pattern) {
        return match(input, pattern, new StatePos(States.LITERAL_MATCH_ANYWHERE, 0));
    }
}
