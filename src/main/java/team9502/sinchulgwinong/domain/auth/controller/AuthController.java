package team9502.sinchulgwinong.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team9502.sinchulgwinong.domain.auth.dto.request.UserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.service.AuthService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_USER_SIGN_UP;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다. 사업자 번호 여부로 관리자를 구분합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 가입 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"회원 가입 성공\", \"data\": null }"))),
            @ApiResponse(responseCode = "400", description = "회원 가입 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"회원 가입 실패\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<Object>> signup(
            @RequestBody @Valid UserSignupRequestDTO requestDTO) {

        authService.signup(requestDTO);

        return ResponseEntity.status(SUCCESS_USER_SIGN_UP.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_USER_SIGN_UP.getMessage(),
                                null
                        )
                );
    }
}
