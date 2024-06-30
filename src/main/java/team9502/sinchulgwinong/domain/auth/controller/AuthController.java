package team9502.sinchulgwinong.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team9502.sinchulgwinong.domain.auth.dto.request.CompanyUserLoginRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.CpUserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserLoginRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.response.CompanyUserLoginResponseDTO;
import team9502.sinchulgwinong.domain.auth.dto.response.UserLoginResponseDTO;
import team9502.sinchulgwinong.domain.auth.service.AuthService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API [김은채]")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "구직자 회원 가입", description = "새로운 구직자 사용자를 시스템에 등록합니다. 이메일 인증이 선행되어야 합니다. 회원가입시 300포인트가 적립됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "구직자 회원 가입 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"구직자 회원 가입 성공\", \"data\": null }"))),
            @ApiResponse(responseCode = "400", description = "요청 처리 중 오류 발생",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "EMAIL_VERIFICATION_NEEDED", summary = "이메일 인증 필요",
                                            value = "{\"message\": \"이메일 인증이 필요합니다.\", \"data\": null }"),
                                    @ExampleObject(name = "TERMS_AGREEMENT_NEEDED", summary = "약관 동의 필요",
                                            value = "{\"message\": \"약관에 동의해야 합니다.\", \"data\": null }"),
                                    @ExampleObject(name = "PASSWORD_REQUIRED", summary = "비밀번호 입력 필요",
                                            value = "{\"message\": \"비밀번호를 입력해주세요.\", \"data\": null }"),
                                    @ExampleObject(name = "PASSWORD_MISMATCH", summary = "비밀번호 불일치",
                                            value = "{\"message\": \"비밀번호와 비밀번호 확인이 일치하지 않습니다.\", \"data\": null }")
                            })),
            @ApiResponse(responseCode = "409", description = "중복된 이메일",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"중복된 이메일입니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"내부 서버 에러\", \"data\": null }")))
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

    @PostMapping("/cp-signup")
    @Operation(summary = "기업 회원 가입", description = "새로운 기업 사용자를 시스템에 등록합니다. 이메일 인증이 선행되어야 합니다. 회원가입시 300포인트가 적립됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "기업 회원 가입 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"기업 회원 가입 성공\", \"data\": null }"))),
            @ApiResponse(responseCode = "400", description = "요청 처리 중 오류 발생",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "EMAIL_VERIFICATION_NEEDED", summary = "이메일 인증 필요",
                                            value = "{\"message\": \"이메일 인증이 필요합니다.\", \"data\": null }"),
                                    @ExampleObject(name = "TERMS_AGREEMENT_NEEDED", summary = "약관 동의 필요",
                                            value = "{\"message\": \"약관에 동의해야 합니다.\", \"data\": null }"),
                                    @ExampleObject(name = "PASSWORD_REQUIRED", summary = "비밀번호 입력 필요",
                                            value = "{\"message\": \"비밀번호를 입력해주세요.\", \"data\": null }"),
                                    @ExampleObject(name = "PASSWORD_MISMATCH", summary = "비밀번호 불일치",
                                            value = "{\"message\": \"비밀번호와 비밀번호 확인이 일치하지 않습니다.\", \"data\": null }")
                            })),
            @ApiResponse(responseCode = "409", description = "중복된 이메일",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"중복된 이메일입니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"내부 서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<Object>> signupCompany(
            @RequestBody @Valid CpUserSignupRequestDTO requestDTO) {

        authService.cpSignup(requestDTO);

        return ResponseEntity.status(SUCCESS_CP_USER_SIGN_UP.getHttpStatus())
                .body(
                        GlobalApiResponse.of(SUCCESS_CP_USER_SIGN_UP.getMessage(),
                                null
                        )
                );
    }

    @PostMapping("/login")
    @Operation(summary = "구직자(사용자) 로그인", description = "사용자가 이메일로 로그인합니다. 일반 로그인입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"로그인 성공\", \"data\": {\"userId\": 1, \"username\": \"김은채\", \"nickname\": \"대구총잡이\", \"email\": \"faleles442@gawte.com\", \"phoneNumber\": null, \"loginType\": \"NORMAL\"} }"))),
            @ApiResponse(responseCode = "404", description = "미존재 사용자",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"사용자를 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<UserLoginResponseDTO>> login(
            @RequestBody @Valid UserLoginRequestDTO requestDTO) {

        UserLoginResponseDTO responseDTO = authService.login(requestDTO);

        return ResponseEntity.status(SUCCESS_USER_LOGIN.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_USER_LOGIN.getMessage(),
                                responseDTO
                        )
                );
    }

    @PostMapping("/cp-login")
    @Operation(summary = "기업 회원 로그인", description = "기업 회원이 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"로그인 성공\", \"data\": {\"cpUserId\": 1, \"cpUsername\": \"김고양이\", \"cpName\": \"고양이탕후루\", \"cpEmail\": \"rihic26977@exeneli.com\", \"cpPhoneNumber\": \"01012345678\", \"hiringStatus\": true, \"employeeCount\": 10} }"))),
            @ApiResponse(responseCode = "404", description = "미존재 사용자",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"기업(회원)을 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<CompanyUserLoginResponseDTO>> cpLogin(
            @RequestBody @Valid CompanyUserLoginRequestDTO requestDTO) {

        CompanyUserLoginResponseDTO responseDTO = authService.cpLogin(requestDTO);

        return ResponseEntity.status(SUCCESS_CP_USER_LOGIN.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CP_USER_LOGIN.getMessage(),
                                responseDTO
                        )
                );
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"로그아웃 성공\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<Object>> logout(
            HttpServletResponse response) {

        authService.logout(response);

        return ResponseEntity.status(SUCCESS_LOGOUT.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_LOGOUT.getMessage(),
                                null
                        )
                );
    }
}
