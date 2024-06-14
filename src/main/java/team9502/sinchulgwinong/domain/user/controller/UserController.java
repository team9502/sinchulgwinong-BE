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
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.user.dto.request.UserPasswordUpdateRequestDTO;
import team9502.sinchulgwinong.domain.user.dto.request.UserProfileUpdateRequestDTO;
import team9502.sinchulgwinong.domain.user.dto.response.UserProfileResponseDTO;
import team9502.sinchulgwinong.domain.user.service.UserService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

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

    @PatchMapping("/profile")
    @Operation(summary = "사용자 프로필 수정", description = "로그인한 사용자의 프로필 정보를 수정합니다. 전체를 수정할 필요 없이, 원하는 부분만 수정 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"프로필 수정 성공\", \"data\": {\"userId\": 1, \"username\": \"김수정\", \"nickname\": \"수정이의 별명\", \"email\": \"fix@email.com\", \"phoneNumber\": \"010-5678-1234\"} }"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"요청 데이터가 유효하지 않습니다.\", \"data\": null }"))),
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
    public ResponseEntity<GlobalApiResponse<UserProfileResponseDTO>> updateUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserProfileUpdateRequestDTO requestDTO) {

        UserProfileResponseDTO responseDTO = userService.updateUserProfile(userDetails.getUserId(), requestDTO);

        return ResponseEntity.status(SUCCESS_USER_PROFILE_UPDATED.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_USER_PROFILE_UPDATED.getMessage(),
                                responseDTO));
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경", description = "로그인한 사용자의 비밀번호를 안전하게 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"비밀번호 변경 성공\" }"))),
            @ApiResponse(responseCode = "400", description = "비밀번호 불일치 또는 유효하지 않은 요청",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "비밀번호 불일치", value = "{ \"code\": \"400\", \"message\": \"입력한 비밀번호가 기존 비밀번호와 일치하지 않습니다.\" }"),
                                    @ExampleObject(name = "비밀번호 확인 불일치", value = "{ \"code\": \"400\", \"message\": \"비밀번호와 비밀번호 확인이 일치하지 않습니다.\" }")
                            })),
            @ApiResponse(responseCode = "401", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"401\", \"message\": \"로그인 타입이 EMAIL이 아닙니다.\" }"))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"404\", \"message\": \"사용자를 찾을 수 없습니다.\" }"))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 오류가 발생했습니다.\" }")))
    })
    public ResponseEntity<GlobalApiResponse<Void>> updateUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserPasswordUpdateRequestDTO requestDTO) {

        userService.updateUserPassword(userDetails.getUserId(), requestDTO, userDetails);

        return ResponseEntity.status(SUCCESS_USER_PASSWORD_UPDATED.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_USER_PASSWORD_UPDATED.getMessage(),
                                null));
    }
}
