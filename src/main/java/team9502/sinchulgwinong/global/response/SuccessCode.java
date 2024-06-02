package team9502.sinchulgwinong.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    // Global
    OK(HttpStatus.OK, "200", "요청 성공");





    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}
