package machines;

public class RegexToken {
    private RegexLexeme lexeme;
    private Character value;
    private int pos;

    public RegexToken(RegexLexeme lexeme, Character value) {
        this.lexeme = lexeme;
        this.value = value;
        this.pos = 0;
    }

    public RegexToken(RegexLexeme lexeme, Character value, int pos) {
        this.lexeme = lexeme;
        this.value = value;
        this.pos = pos;
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

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "RegexToken{" +
                "lexeme=" + lexeme +
                ", value=" + value +
                ", pos=" + pos +
                '}';
    }
}
