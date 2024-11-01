package math.server.dto.response;

@SuppressWarnings("unused")
public class QuestionDTO {

    private String numbers, target;

    public QuestionDTO(String numbers, String target) {
        this.numbers = numbers;
        this.target = target;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
