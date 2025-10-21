package machines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterGroups {
    public static boolean match(String input, String patternFromNextToBrace) {
        Map<CaptureGroupType, List<Character>> captureGroups = extractCaptureGroup(patternFromNextToBrace);
        for (int i = 0; i < input.length(); i++) {
            if (captureGroups.get(CaptureGroupType.CHAR).contains(input.charAt(i))) return true;
        }
        return false;
    }

    private static Map<CaptureGroupType, List<Character>> extractCaptureGroup(String patternFromNextToBrace) {
        int i = 0;
        Map<CaptureGroupType, List<Character>> captureGroups = new HashMap<>();
        while (patternFromNextToBrace.charAt(i) != ']') {
            char c = patternFromNextToBrace.charAt(i);
            List<Character> characters = captureGroups.get(CaptureGroupType.CHAR);
            characters.add(c);
            captureGroups.put(CaptureGroupType.CHAR, characters);
//            if (c == '-') {} // We will deal with this later
            i++;
        }
        return captureGroups;
    }
}
