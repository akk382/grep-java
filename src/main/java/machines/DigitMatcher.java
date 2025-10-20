package machines;

public class DigitMatcher {
    public static boolean match(String input, States currentState) {
        if (currentState == null) currentState = States.DIGIT_MATCH_ANYWHERE;
        return switch (currentState) {
            case DIGIT_MATCH -> false;
            case DIGIT_MATCH_ANYWHERE -> {
                for (int i = 0; i < input.length(); i++) {
                    if (Character.isDigit(input.charAt(i))) {
                        yield true;
                    }
                }
                yield false;
            }
            default -> false;
        };
    }

    public static boolean match(String input) {
        return match(input, States.DIGIT_MATCH_ANYWHERE);
    }
}
