package machines;

public class RegexToken {
  private RegexLexeme lexeme;
  private Character value;
  private int groupPos;

  public RegexToken(RegexLexeme lexeme, Character value) {
    this.lexeme = lexeme;
    this.value = value;
    this.groupPos = 0;
  }

  public RegexToken(RegexLexeme lexeme) {
    this.lexeme = lexeme;
    this.value = null;
    this.groupPos = 0;
  }

  public RegexToken(RegexLexeme lexeme, Character value, int groupPos) {
    this.lexeme = lexeme;
    this.value = value;
    this.groupPos = groupPos;
  }

  public RegexToken(int groupPos, RegexLexeme lexeme) {
    this.lexeme = lexeme;
    this.value = null;
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

  @Override
  public String toString() {
    return "RegexToken{" +
            "lexeme=" + lexeme +
            ", value=" + value +
            ", groupPos=" + groupPos +
            '}';
  }
}
