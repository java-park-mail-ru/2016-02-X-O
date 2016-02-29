package util;

/*
 * Created by kvukolov on 17.02.16.
 */
public class RegexCheckedParameter {
    private String value;
    private RegexId regexId;
    private ServerAnswer answer;

    public RegexCheckedParameter(String value, RegexId regexId, ServerAnswer answer) {
        this.value = value;
        this.regexId = regexId;
        this.answer = answer;
    }

    public String getValue() {
        return value;
    }

    public RegexId getRegexId() {
        return regexId;
    }

    public ServerAnswer getAnswer() {
        return answer;
    }
}
