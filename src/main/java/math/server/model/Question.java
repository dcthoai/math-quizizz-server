package math.server.model;

import java.util.List;
import java.util.UUID;

public class Question {

    private final String questionID, answer;
    private final List<String> options;

    public Question(String answer, List<String> options) {
        this.questionID = UUID.randomUUID().toString();
        this.answer = answer;
        this.options = options;
    }

    public String getQuestionID() {
        return questionID;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getOptions() {
        return options;
    }
}
