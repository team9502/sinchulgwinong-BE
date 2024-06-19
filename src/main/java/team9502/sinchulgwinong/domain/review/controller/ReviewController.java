package team9502.sinchulgwinong.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.review.dto.request.ReviewCreationRequestDTO;
import team9502.sinchulgwinong.domain.review.dto.response.ReviewCreationResponseDTO;
import team9502.sinchulgwinong.domain.review.dto.response.ReviewListResponseDTO;
import team9502.sinchulgwinong.domain.review.dto.response.ReviewResponseDTO;
import team9502.sinchulgwinong.domain.review.dto.response.UserReviewListResponseDTO;
import team9502.sinchulgwinong.domain.review.service.ReviewService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    @Operation(summary = "리뷰 작성", description = "사용자가 리뷰를 작성합니다. 이미 리뷰를 작성한 기업에 대해선 리뷰를 추가로 작성할 수 없습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"리뷰 작성 성공\", \"data\": {\"reviewId\": 1, \"cpUserId\": 1, \"reviewTitle\": \"친절한 사장님!\", \"reviewContent\": \"돍망갉셇욦\", \"rating\": 3}}"))),
            @ApiResponse(responseCode = "400", description = "리뷰 작성 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"이미 리뷰를 작성하셨습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "404", description = "사용자 또는 기업(회원)을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "USER_NOT_FOUND", summary = "미존재 사용자",
                                            value = "{\"message\": \"사용자를 찾을 수 없습니다.\", \"data\": null }"),
                                    @ExampleObject(name = "COMPANY_USER_NOT_FOUND", summary = "미존재 기업(회원)",
                                            value = "{\"message\": \"기업(회원)을 찾을 수 없습니다.\", \"data\": null }")
                            })),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<ReviewCreationResponseDTO>> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ReviewCreationRequestDTO requestDTO) {

        Long userId = userDetails.getUserId();
        ReviewCreationResponseDTO responseDTO = reviewService.createReview(userId, requestDTO);

        return ResponseEntity.status(SUCCESS_REVIEW_CREATION.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_REVIEW_CREATION.getMessage(),
                                responseDTO
                        )
                );
    }

    @GetMapping("/cpUsers/self/reviews")
    @Operation(summary = "기업 사용자 리뷰 전체 조회", description = "기업 사용자가 자신에 대한 모든 리뷰를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기업 리뷰 전체 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"기업 리뷰 전체 조회 성공\", \"data\": {\"reviews\": [{\"reviewId\": 1, \"reviewTitle\": \"친절한 사장님!\", \"reviewContent\": \"돍망갉셇욦\", \"rating\": 3}], \"totalReviewCount\": 1 }}"))),
            @ApiResponse(responseCode = "400", description = "잘못된 사용자 유형",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"잘못된 사용자 유형입니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<ReviewListResponseDTO>> getAllReviewsForCompanyUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long cpUserId = userDetails.getCpUserId();

        ReviewListResponseDTO responseDTO = reviewService.findAllReviewsByCompanyUserId(cpUserId);

        return ResponseEntity.status(SUCCESS_CP_USER_REVIEW_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CP_USER_REVIEW_READ.getMessage(),
                                responseDTO
                        )
                );
    }

    @GetMapping("/cpUsers/{cpUserId}/reviews")
    @Operation(
            summary = "기업 회원 리뷰 목록 조회",
            description = "사용자가 기업 회원의 리뷰 목록을 조회합니다. 각 리뷰의 가시성 상태도(공개/비공개) 함께 반환합니다.",
            parameters = {
                    @Parameter(
                            name = "cpUserId",
                            description = "기업 회원의 고유 ID, 해당 기업에 대한 리뷰를 조회합니다.",
                            required = true,
                            schema = @Schema(type = "long")
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "기업 리뷰 전체 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"message\": \"리뷰 목록 조회 성공\", \"data\": {\"reviews\": [{\"reviewId\": 1, \"reviewTitle\": \"친절한 사장님!\", \"reviewContent\": \"사장님이 맛있고 사과가 친절해요.\", \"rating\": 5, \"visibility\": \"PRIVATE\"}, {\"reviewId\": 2, \"reviewTitle\": \"좋은 품질\", \"reviewContent\": \"상품의 품질이 아주 좋습니다.\", \"rating\": 4, \"visibility\": \"PUBLIC\"}], \"totalReviewCount\": 2 }}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"message\": \"서버 에러\", \"data\": null }"
                            )
                    )
            )
    })
    public ResponseEntity<GlobalApiResponse<UserReviewListResponseDTO>> getReviewsWithVisibility(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("cpUserId") Long cpUserId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Long userId = userDetails.getUserId();
        UserReviewListResponseDTO responseDTO = reviewService.getReviewsWithVisibility(cpUserId, userId, page, size);

        return ResponseEntity.status(SUCCESS_CP_USER_REVIEW_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CP_USER_REVIEW_READ.getMessage(),
                                responseDTO
                        )
                );
    }

    @PostMapping("/reviews/{reviewId}/view")
    @Operation(summary = "리뷰 열람", description = "사용자가 100포인트를 사용하여 리뷰를 열람합니다. 포인트가 충분하지 않거나 리뷰가 이미 공개된 경우 오류가 발생할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 사용하여 리뷰 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"포인트 사용하여 리뷰 조회 성공\", \"data\": {\"reviewId\": 1, \"reviewTitle\": \"친절한 사장님!\", \"reviewContent\": \"돍망갉셇욦\", \"rating\": 3} }"))),
            @ApiResponse(responseCode = "400", description = "포인트 부족 또는 리뷰 이미 공개",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "POINT_NOT_FOUND", value = "{ \"message\": \"포인트를 찾을 수 없습니다.\", \"data\": null }"),
                                    @ExampleObject(name = "INSUFFICIENT_POINTS", value = "{ \"message\": \"포인트가 부족합니다.\", \"data\": null }"),
                                    @ExampleObject(name = "REVIEW_ALREADY_PUBLIC", value = "{ \"message\": \"이미 공개된 리뷰입니다.\", \"data\": null }")
                            })),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"리뷰를 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<ReviewResponseDTO>> viewReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("reviewId") Long reviewId) {

        Long userId = userDetails.getUserId();
        ReviewResponseDTO responseDTO = reviewService.viewReview(reviewId, userId);

        return ResponseEntity.status(SUCCESS_USER_USE_POINT_TO_REVIEW.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_USER_USE_POINT_TO_REVIEW.getMessage(),
                                responseDTO
                        )
                );
    }
}