package team9502.sinchulgwinong.domain.companyUser.controller;

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
import team9502.sinchulgwinong.domain.companyUser.dto.response.CpUserProfileResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.service.CpUserService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_CP_USER_PROFILE_READ;

@RestController
@RequiredArgsConstructor
@Tag(name = "CompanyUser", description = "기업(회원) 관련 API [김은채]")
@RequestMapping("/cpUsers")
public class CpUserController {

    private final CpUserService cpUserService;

    @GetMapping("/profile")
    @Operation(summary = "기업(회원) 프로필 조회", description = "로그인한 사용자의 프로필 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기업(회원) 프로필 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"구인자 프로필 조회 성공\", \"data\": {\"userId\": 1, \"username\": \"김은채\", \"nickname\": \"정신체리라\", \"email\": \"email@example.com\", \"phoneNumber\": \"010-1234-5678\"} }"))),
            @ApiResponse(responseCode = "404", description = "기업(회원)을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"404\", \"message\": \"사용자를 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<CpUserProfileResponseDTO>> getCompanyUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CpUserProfileResponseDTO responseDTO = cpUserService.getCpUserProfile(userDetails.getCpUserId());

        return ResponseEntity.status(SUCCESS_CP_USER_PROFILE_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CP_USER_PROFILE_READ.getMessage(),
                                responseDTO
                        )
                );
    }
}
