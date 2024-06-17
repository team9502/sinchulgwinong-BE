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
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.companyUser.dto.request.CpUserPasswordUpdateRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.request.CpUserProfileUpdateRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.response.CpUserProfileResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.service.CpUserService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "CompanyUser", description = "기업(회원) 관련 API [김은채]")
@RequestMapping("/cpUsers")
public class CpUserController {

    private final CpUserService cpUserService;

    @GetMapping("/{cpUserId}/profile")
    @Operation(summary = "기업(회원) 프로필 조회", description = "기업(회원)의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기업(회원) 프로필 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"프로필 조회 성공\", \"data\": {\"cpUserId\": 1, \"cpName\": \"팀9502\", \"cpEmail\": \"example@email.com\", \"cpPhoneNumber\": \"01012345678\", \"cpUsername\": \"김은채\", \"hiringStatus\": true, \"employeeCount\": 50, \"foundationDate\": \"2021-01-01\", \"description\": \"기업 소개 예시입니다.\", \"cpNum\": \"1234567890\", \"averageRating\": 4.5, \"reviewCount\": 20} }"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"잘못된 요청 데이터입니다.\" }"))),
            @ApiResponse(responseCode = "404", description = "기업(회원)을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"404\", \"message\": \"기업(회원)을 찾을 수 없습니다.\" }"))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 오류가 발생했습니다.\" }")))
    })
    public ResponseEntity<GlobalApiResponse<CpUserProfileResponseDTO>> getCompanyUserProfile(
            @PathVariable("cpUserId") Long cpUserId) {

        CpUserProfileResponseDTO responseDTO = cpUserService.getCpUserProfile(cpUserId);

        return ResponseEntity.status(SUCCESS_CP_USER_PROFILE_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CP_USER_PROFILE_READ.getMessage(),
                                responseDTO
                        )
                );
    }

    @PatchMapping("/profile")
    @Operation(summary = "기업(회원) 프로필 수정", description = "로그인한 기업(회원)이 본인의 프로필 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"프로필 수정 성공\", \"data\": {\"cpUserId\": 1, \"cpName\": \"팀9502\", \"cpEmail\": \"fix@email.com\", \"cpPhoneNumber\": \"01012345678\", \"cpUsername\": \"김은채\", \"hiringStatus\": true, \"employeeCount\": 100, \"description\": \"수정된 기업 소개입니다.\", \"cpNum\": \"1234567890\"} }"))),
            @ApiResponse(responseCode = "400", description = "이메일 인증 필요",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"이메일 인증이 필요합니다.\" }"))),
            @ApiResponse(responseCode = "404", description = "기업(회원)을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"404\", \"message\": \"기업(회원)을 찾을 수 없습니다.\" }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<CpUserProfileResponseDTO>> updateCompanyUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CpUserProfileUpdateRequestDTO updateRequestDTO) {

        CpUserProfileResponseDTO responseDTO = cpUserService.updateCpUserProfile(userDetails.getCpUserId(), updateRequestDTO);

        return ResponseEntity.status(SUCCESS_CP_USER_PROFILE_UPDATED.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CP_USER_PROFILE_UPDATED.getMessage(),
                                responseDTO
                        )
                );
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경", description = "로그인한 기업(회원)의 비밀번호를 안전하게 변경합니다.")
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
            @ApiResponse(responseCode = "400", description = "잘못된 사용자 유형",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"잘못된 사용자 유형입니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"404\", \"message\": \"사용자를 찾을 수 없습니다.\" }"))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 오류가 발생했습니다.\" }")))
    })
    public ResponseEntity<GlobalApiResponse<Void>> updateUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CpUserPasswordUpdateRequestDTO requestDTO) {

        cpUserService.updateCpUserPassword(userDetails.getCpUserId(), requestDTO);

        return ResponseEntity.status(SUCCESS_USER_PASSWORD_UPDATED.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_USER_PASSWORD_UPDATED.getMessage(),
                                null));
    }
}
