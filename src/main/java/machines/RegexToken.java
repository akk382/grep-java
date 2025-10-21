package machines;

public class RegexToken {
  private RegexLexeme lexeme;
  private Character value;
  private int groupPos;
  private int minOccurrences;
  private int maxOccurrences;

  public RegexToken(RegexLexeme lexeme, Character value) {
    this.lexeme = lexeme;
    this.value = value;
    this.groupPos = 0;
    updateRemainingFields();
  }

  private void updateRemainingFields() {
    switch (lexeme) {
      case DIGIT_PLUS:
      case LITERAL_PLUS:
      case WORD_PLUS:
      case POSITIVE_GROUP_PLUS:
      case NEGATIVE_GROUP_PLUS:
        this.minOccurrences = 1;
        break;
      case DIGIT_STAR:
      case LITERAL_STAR:
      case WORD_STAR:
      case POSITIVE_GROUP_STAR:
      case NEGATIVE_GROUP_STAR:
        this.minOccurrences = 0;
        break;
      case DIGIT_QUE:
      case LITERAL_QUE:
      case WORD_QUE:
      case POSITIVE_GROUP_QUE:
      case NEGATIVE_GROUP_QUE:
        this.minOccurrences = 0;
        this.maxOccurrences = 1;
        break;
      default: break;
    }
  }

  public RegexToken(RegexLexeme lexeme) {
    this(lexeme, null);
  }

  public RegexToken(int groupPos, RegexLexeme lexeme) {
    this(lexeme, null);
    this.groupPos = groupPos;
  }

  public RegexLexeme getLexeme() {
    return lexeme;
  }

  public void setLexeme(RegexLexeme lexeme) {
    this.lexeme = lexeme;
  }

  public char getValue() {
    return value;
  }

  public void setValue(Character value) {
    this.value = value;
  }

  public int getGroupPos() {
    return groupPos;
  }

  public void setGroupPos(int pos) {
    this.groupPos = pos;
  }

  public void setMinOccurrences(int minOccurrences) {
    this.minOccurrences = minOccurrences;
  }

  public void setMaxOccurrences(int maxOccurrences) {
    this.maxOccurrences = maxOccurrences;
  }

  public int getMinOccurrences() {
    return minOccurrences;
  }

  public int getMaxOccurrences() {
    return maxOccurrences;
  }

  @Override
  public String toString() {
    return "RegexToken{" +
            "lexeme=" + lexeme +
            ", value=" + value +
            ", groupPos=" + groupPos +
            ", minOccurrences=" + minOccurrences +
            ", maxOccurrences=" + maxOccurrences +
            '}';
  }
}
