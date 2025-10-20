package machines;

public class LiteralMatcher {
    public static boolean match(String input, char pattern, States currentState) {
        if (currentState == null) currentState = States.LITERAL_MATCH_ANYWHERE;
        return switch (currentState) {
            case LITERAL_MATCH -> false;
            case LITERAL_MATCH_ANYWHERE -> {
                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == pattern) {
                        yield true;
                    }
                }
                yield false;
            }
            default -> false;
        };
    }

    public static boolean match(String input, char pattern) {
        return match(input, pattern, States.LITERAL_MATCH_ANYWHERE);
    }
}
