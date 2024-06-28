package team9502.sinchulgwinong.domain.companyUser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.companyUser.dto.request.CpUserPasswordUpdateRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.request.CpUserProfileUpdateRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.response.CpUserPageResponseDTO;
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
    @Operation(summary = "기업(회원) 프로필 조회", description = "기업(회원)의 정보를 조회합니다. 로그인을 하지 않아도 확인할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기업(회원) 프로필 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"기업(회원) 프로필 조회 성공\", \"data\": {\"cpUserId\": 1, \"cpName\": \"고양이탕후루\", \"cpEmail\": \"rihic26977@exeneli.com\", \"cpPhoneNumber\": \"01012345678\", \"cpUsername\": \"김고양이\", \"hiringStatus\": true, \"employeeCount\": 10, \"foundationDate\": \"1999-10-06\", \"description\": \"고양이는 세상을 움직이는 혁신입니다.\", \"cpNum\": \"1234567890\", \"averageRating\": null, \"reviewCount\": null} }"))),
            @ApiResponse(responseCode = "404", description = "기업(회원)을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"기업(회원)을 찾을 수 없습니다.\" }"))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"서버 오류가 발생했습니다.\" }")))
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
    @Operation(summary = "기업(회원) 프로필 수정", description = "로그인한 기업(회원)이 본인의 프로필 정보를 수정합니다. 원하는 정보만 수정 가능합니다. 이메일 수정 시 인증이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"기업(회원) 프로필 수정 성공\", \"data\": {\"cpUserId\": 1, \"cpName\": \"고양이탕후루\", \"cpEmail\": \"rihic26977@exeneli.com\", \"cpPhoneNumber\": \"01099998888\", \"cpUsername\": \"김고양이\", \"hiringStatus\": true, \"employeeCount\": 10, \"foundationDate\": \"1999-10-06\", \"description\": \"고양이는 세상을 움직이는 혁신입니다.\", \"cpNum\": \"1234567890\", \"averageRating\": null, \"reviewCount\": null} }"))),
            @ApiResponse(responseCode = "400", description = "요청 처리 중 오류 발생",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "EMAIL_VERIFICATION_NEEDED", summary = "이메일 인증 필요",
                                            value = "{\"message\": \"이메일 인증이 필요합니다.\", \"data\": null }"),
                                    @ExampleObject(name = "INVALID_INPUT", summary = "요청값 누락",
                                            value = "{\"message\": \"잘못된 입력입니다.\", \"data\": null }"),
                                    @ExampleObject(name = "잘못된 사용자 유형",
                                            value = "{\"message\": \"잘못된 사용자 유형입니다.\", \"data\": null }")
                            })),
            @ApiResponse(responseCode = "404", description = "기업(회원)을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"기업(회원)을 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"서버 에러\", \"data\": null }")))
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
    @Operation(summary = "비밀번호 변경", description = "로그인한 기업(회원)의 비밀번호를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"기업(회원) 비밀번호 수정 성공\", \"data\": null }"))),
            @ApiResponse(responseCode = "400", description = "요청 처리 중 오류 발생",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "비밀번호 불일치",
                                            value = "{\"message\": \"입력한 비밀번호가 기존 비밀번호와 일치하지 않습니다.\", \"data\": null }"),
                                    @ExampleObject(name = "비밀번호 확인 불일치",
                                            value = "{\"message\": \"비밀번호와 비밀번호 확인이 일치하지 않습니다.\", \"data\": null }"),
                                    @ExampleObject(name = "잘못된 사용자 유형",
                                            value = "{\"message\": \"잘못된 사용자 유형입니다.\", \"data\": null }")
                            })),
            @ApiResponse(responseCode = "404", description = "기업(회원)을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"기업(회원)을 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"서버 오류가 발생했습니다.\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<Void>> updateUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CpUserPasswordUpdateRequestDTO requestDTO) {

        cpUserService.updateCpUserPassword(userDetails.getCpUserId(), requestDTO);

        return ResponseEntity.status(SUCCESS_CP_USER_PASSWORD_UPDATED.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CP_USER_PASSWORD_UPDATED.getMessage(),
                                null));
    }

    @GetMapping
    @Operation(summary = "기업 사용자 전체 조회"
            , description = "기업 사용자의 목록을 페이지네이션된 형식으로 조회합니다. " +
            "스크랩순, 리뷰순, 최신순, 채용글 작성개수순으로 정렬 가능합니다. " +
            "sort에 reviewsDesc, jobPostingsDesc, viewsDesc, scrapsDesc, createdAtDesc 중 하나를 입력하세요. " +
            "평점 필터링 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기업 사용자 전체 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"기업 사용자 전체 조회 성공\", \"data\": { \"cpUsers\": [{ \"cpUserId\": 1, \"cpName\": \"고양이탕후루\", \"reviewCount\": 3, \"averageRating\": 3.0 }], \"totalCpUserCount\": 1, \"currentPage\": 0, \"totalPages\": 1 } }"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"잘못된 요청 값입니다.\" }"))),
            @ApiResponse(responseCode = "404", description = "기업 사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"기업 사용자를 찾을 수 없습니다.\" }"))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"서버 오류가 발생했습니다.\", \"data\": null }")))
    })

    public ResponseEntity<GlobalApiResponse<CpUserPageResponseDTO>> findAllCompanyUsers(
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "minRating") Float minRating,
            @RequestParam(required = false, value = "maxRating") Float maxRating) {

        Pageable pageable = PageRequest.of(page, size);
        CpUserPageResponseDTO usersPage = cpUserService.getAllCompanyUsers(sort, minRating, maxRating, pageable);

        return ResponseEntity.status(SUCCESS_CP_USER_ALL_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CP_USER_ALL_READ.getMessage(),
                                usersPage));
    }

    @PostMapping("/usePoints/banner")
    @Operation(summary = "배너를 위해 포인트 사용", description = "기업(회원)이 배너를 위해 포인트를 사용합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 사용 성공"),
            @ApiResponse(responseCode = "400", description = "포인트가 부족합니다."),
            @ApiResponse(responseCode = "404", description = "기업(회원)을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.")
    })
    public ResponseEntity<GlobalApiResponse<Void>> usePointsForBanner(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cpUserService.usePointsForBanner(userDetails.getCpUserId());

        return ResponseEntity.status(SUCCESS_POINTS_USED_FOR_BANNER.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_POINTS_USED_FOR_BANNER.getMessage(),
                                null
                        )
                );
    }
}
