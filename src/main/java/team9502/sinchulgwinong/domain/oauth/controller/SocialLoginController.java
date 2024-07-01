package team9502.sinchulgwinong.domain.oauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.oauth.dto.request.SocialLoginRequestDTO;
import team9502.sinchulgwinong.domain.oauth.enums.SocialType;
import team9502.sinchulgwinong.domain.oauth.service.SocialLoginService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_SOCIAL_LOGIN;

@RestController
@RequestMapping("/social-login")
@RequiredArgsConstructor
@Tag(name = "SocialLogin", description = "소셜 로그인 관련 API [김은채]")
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping("/additional-info")
    @Operation(summary = "소셜 로그인 추가 정보", description = "소셜 로그인 후 추가 정보를 입력받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추가 정보 등록 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"추가 정보 등록 성공\", \"data\": {\"userId\": 3, \"socialType\": \"GOOGLE\", \"email\": \"user@example.com\"}}"))),
            @ApiResponse(responseCode = "400", description = "입력 데이터 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"입력 데이터 오류\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<Object>> submitAdditionalInfo(
            @RequestBody @Valid SocialLoginRequestDTO requestDTO) {

        socialLoginService.createOrUpdateSocialLogin(requestDTO, SocialType.GOOGLE);

        return ResponseEntity.status(SUCCESS_SOCIAL_LOGIN.getHttpStatus())
                .body(GlobalApiResponse.of(SUCCESS_SOCIAL_LOGIN.getMessage(), null));
    }

    @GetMapping("/callback")
    @Operation(summary = "소셜 로그인 콜백", description = "구글 OAuth2 콜백을 처리합니다.")
    public ResponseEntity<String> handleGoogleCallback(
            @RequestParam("code") String code) {
        try {
            String response = socialLoginService.handleGoogleCallback(code);
            return ResponseEntity.ok("소셜 로그인 콜백 처리 완료: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("소셜 로그인 콜백 처리 실패: " + e.getMessage());
        }
    }
}
