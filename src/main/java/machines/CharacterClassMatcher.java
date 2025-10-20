package machines;

// Should match any alphanumeric character:- A-Za-z0-9_
public class CharacterClassMatcher {
    public static boolean match(String input, States currentState) {
        if (currentState == null) currentState = States.WORD_CLASS_MATCH_ANYWHERE;
        return switch (currentState) {
            case WORD_CLASS_MATCH -> false;
            case WORD_CLASS_MATCH_ANYWHERE -> {
                for (int i = 0; i < input.length(); i++) {
                    if (Character.isLetterOrDigit(input.charAt(i)) || '_' == input.charAt(i)) {
                        yield true;
                    }
                }
                yield false;
            }
            default -> false;
        };
    }

    public static boolean match(String input) {
        return match(input, States.WORD_CLASS_MATCH_ANYWHERE);
    }
}
