package team9502.sinchulgwinong.domain.email.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team9502.sinchulgwinong.domain.email.dto.request.EmailVerificationCodeRequestDTO;
import team9502.sinchulgwinong.domain.email.dto.request.EmailVerificationRequestDTO;
import team9502.sinchulgwinong.domain.email.service.EmailVerificationService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_EMAIL_VERIFICATION;
import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_EMAIL_VERIFICATION_SENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@Tag(name = "Email Verification", description = "이메일 인증 관련 API [김은채]")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/sendCode")
    @Operation(summary = "이메일 인증 코드 발송", description = "사용자 이메일로 인증 코드를 발송합니다.")
    @ApiResponse(responseCode = "200", description = "인증 코드 발송 성공",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"인증 코드 발송 성공\", \"data\": null }")))
    @ApiResponse(responseCode = "400", description = "잘못된 입력",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"잘못된 입력입니다.\", \"data\": null }")))
    @ApiResponse(responseCode = "401", description = "이메일 인증 실패",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"code\": \"401\", \"message\": \"이메일 인증에 실패했습니다.\", \"data\": null }")))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
    public ResponseEntity<GlobalApiResponse<Void>> sendVerificationCode(
            @RequestBody EmailVerificationRequestDTO requestDTO) {

        emailVerificationService.createVerification(requestDTO.getEmail(), requestDTO.getUserType());

        return ResponseEntity.status(SUCCESS_EMAIL_VERIFICATION_SENT.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_EMAIL_VERIFICATION_SENT.getMessage(),
                                null
                        )
                );
    }

    @PostMapping("/verifyCode")
    @Operation(summary = "이메일 인증 코드 검증", description = "제공된 이메일과 인증 코드의 유효성을 검증합니다.")
    @ApiResponse(responseCode = "200", description = "이메일 인증 성공",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"이메일 인증 성공\", \"data\": null }")))
    @ApiResponse(responseCode = "400", description = "유효하지 않거나 만료된 코드",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"유효하지 않거나 만료된 인증 코드\", \"data\": null }")))
    @ApiResponse(responseCode = "401", description = "잘못된 입력",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"잘못된 입력입니다.\", \"data\": null }")))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
    public ResponseEntity<GlobalApiResponse<Void>> verifyCode(
            @RequestBody EmailVerificationCodeRequestDTO requestDTO) {

        emailVerificationService.verifyCode(requestDTO.getEmail(), requestDTO.getCode());


        return ResponseEntity.status(SUCCESS_EMAIL_VERIFICATION.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_EMAIL_VERIFICATION.getMessage(),
                                null
                        )
                );
    }
}
