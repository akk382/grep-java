package machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterGroups {
  public static StatePos match(String input, String patternFromNextToBrace, StatePos currStatePos, boolean reverseDirection) {
    if (currStatePos == null)
      currStatePos = new StatePos(States.POS_GROUP_MATCH_ANYWHERE, !reverseDirection ? 0 : input.length() - 1);
    if (currStatePos.getCurrInputPos() >= input.length()) {
      return new StatePos(States.POS_GROUP_NOT_MATCHED, currStatePos.getCurrInputPos());
    }
    Map<CaptureGroupType, List<Character>> captureGroups = extractCaptureGroup(patternFromNextToBrace);

    return switch (currStatePos.getState()) {
      case POS_GROUP_MATCH_ANYWHERE -> {
        for (int i = 0; i < input.length(); i++) {
          if (captureGroups.get(CaptureGroupType.CHAR).contains(input.charAt(i)))
            yield new StatePos(States.POS_GROUP_MATCHED, i + 1);
        }
        yield new StatePos(States.POS_GROUP_NOT_MATCHED, 0);
      }
      case POS_GROUP_MATCH_NEXT -> {
        if (captureGroups.get(CaptureGroupType.CHAR).contains(input.charAt(currStatePos.getCurrInputPos())))
          yield new StatePos(States.POS_GROUP_MATCHED, !reverseDirection ? currStatePos.getCurrInputPos() + 1 : currStatePos.getCurrInputPos() - 1);
        yield new StatePos(States.POS_GROUP_NOT_MATCHED, currStatePos.getCurrInputPos());
      }
      default -> new StatePos(States.POS_GROUP_NOT_MATCHED, currStatePos.getCurrInputPos());
    };
  }

  private static Map<CaptureGroupType, List<Character>> extractCaptureGroup(String patternFromNextToBrace) {
    int i = 0;
    Map<CaptureGroupType, List<Character>> captureGroups = new HashMap<>();
    while (patternFromNextToBrace.charAt(i) != ']') {
      char c = patternFromNextToBrace.charAt(i);
      List<Character> characters = captureGroups.getOrDefault(CaptureGroupType.CHAR, new ArrayList<>());
      characters.add(c);
      captureGroups.put(CaptureGroupType.CHAR, characters);
//            if (c == '-') {} // We will deal with this later
      i++;
    }
    return captureGroups;
  }

  public static StatePos negativeMatch(String input, String patternFromNextToBrace, StatePos currStatePos, boolean reverseDirection) {
    if (currStatePos == null) currStatePos = new StatePos(States.NEG_GROUP_MATCH_ANYWHERE, 0);
    if (currStatePos.getCurrInputPos() >= input.length()) {
      return new StatePos(States.NEG_GROUP_NOT_MATCHED, currStatePos.getCurrInputPos());
    }
    Map<CaptureGroupType, List<Character>> captureGroups = extractCaptureGroup(patternFromNextToBrace);

    return switch (currStatePos.getState()) {
      case NEG_GROUP_MATCH_ANYWHERE -> {
        for (int i = 0; i < input.length(); i++) {
          if (!captureGroups.get(CaptureGroupType.CHAR).contains(input.charAt(i)))
            yield new StatePos(States.NEG_GROUP_MATCHED, i + 1);
        }
        yield new StatePos(States.POS_GROUP_NOT_MATCHED, currStatePos.getCurrInputPos());
      }
      case NEG_GROUP_MATCH_NEXT -> {
        if (!captureGroups.get(CaptureGroupType.CHAR).contains(input.charAt(currStatePos.getCurrInputPos())))
          yield new StatePos(States.NEG_GROUP_MATCHED, currStatePos.getCurrInputPos() + 1);
        yield new StatePos(States.POS_GROUP_NOT_MATCHED, currStatePos.getCurrInputPos());
      }
      default -> new StatePos(States.POS_GROUP_NOT_MATCHED, currStatePos.getCurrInputPos());
    };
  }
}
