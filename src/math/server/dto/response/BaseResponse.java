package math.server.dto.response;

public class BaseResponse<T> {

    private Integer code;
    private String message;
    private T result;

    public BaseResponse() {}

    public BaseResponse(T result) {
        this.code = 200;
        this.message = "Success";
        this.result = result;
    }

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
