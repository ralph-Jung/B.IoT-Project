package ac.gachon.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(200, "success", data);
    }

    public static <T> CommonResponse<T> created(T data) {
        return new CommonResponse<>(201, "created", data);
    }

    public static CommonResponse<Void> noContent() {
        return new CommonResponse<>(204, "success", null);
    }
}
