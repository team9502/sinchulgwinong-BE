package team9502.sinchulgwinong.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "글로벌 API 응답 객체")
public class GlobalApiResponse<T> {

    private String message;
    private T data;

    public static <T> GlobalApiResponse<T> of(String message, T data) {
        return new GlobalApiResponse<>(message, data);
    }

    public static GlobalApiResponse<Object> of(String message) {
        return new GlobalApiResponse<>(message, new Object());
    }
}