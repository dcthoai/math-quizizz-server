package math.server.model;

public class Question {

    private final String question, answer, expression;

    public Question(String question, String answer, String expression) {
        this.answer = answer;
        this.question = question;
        this.expression = expression;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getExpression() {
        return expression;
    }
}
