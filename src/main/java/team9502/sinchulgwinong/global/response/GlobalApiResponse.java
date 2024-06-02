package team9502.sinchulgwinong.global.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalApiResponse<T> {

    private String code;
    private String message;
    private T data;

    public static <T> GlobalApiResponse<T> of(String code, String message, T data) {
        return new GlobalApiResponse<>(code, message, data);
    }

    public static GlobalApiResponse<Object> of(String code, String message) {
        return new GlobalApiResponse<>(code, message, new Object());
    }
}