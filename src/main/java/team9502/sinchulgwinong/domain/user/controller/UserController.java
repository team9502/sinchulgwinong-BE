package team9502.sinchulgwinong.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team9502.sinchulgwinong.domain.user.dto.response.UserProfileResponseDTO;
import team9502.sinchulgwinong.domain.user.service.UserService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_USER_PROFILE_READ;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API [김은채]")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "구인자 프로필 조회", description = "로그인한 사용자의 프로필 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구인자 프로필 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"구인자 프로필 조회 성공\", \"data\": {\"userId\": 1, \"username\": \"김은채\", \"nickname\": \"정신체리라\", \"email\": \"email@example.com\", \"phoneNumber\": \"010-1234-5678\"} }"))),
            @ApiResponse(responseCode = "400", description = "잘못된 사용자 유형",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"잘못된 사용자 유형입니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"404\", \"message\": \"사용자를 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<UserProfileResponseDTO>> getUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserProfileResponseDTO responseDTO = userService.getUserProfile(userDetails.getUserId());

        return ResponseEntity.status(SUCCESS_USER_PROFILE_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_USER_PROFILE_READ.getMessage(),
                                responseDTO
                        )
                );
    }
}
