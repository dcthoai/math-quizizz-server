package math.server.dto.request;

public class BaseRequest {

    private String endPoint, request;

    public BaseRequest() {}

    public BaseRequest(String endPoint, String request) {
        this.endPoint = endPoint;
        this.request = request;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
