package math.server.model;

import math.server.dto.response.QuestionDTO;

@SuppressWarnings("unused")
public class Question {

    private final String question, answer, expression;

    public Question(String question, String answer, String expression) {
        this.answer = answer;
        this.question = question;
        this.expression = expression;
    }

    public QuestionDTO getDTO() {
        return new QuestionDTO(question, answer);
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
