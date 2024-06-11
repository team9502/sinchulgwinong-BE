package team9502.sinchulgwinong.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import team9502.sinchulgwinong.domain.review.service.ReviewService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_CP_USER_REVIEW_READ;
import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_REVIEW_CREATION;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "리뷰 작성", description = "사용자가 리뷰를 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"리뷰 작성 성공\", \"data\": {\"reviewId\": 1, \"reviewTitle\": \"친절한 사장님!\", \"reviewContent\": \"사장님이 맛있고 사과가 친절해요.\", \"rating\": 5}}"))),
            @ApiResponse(responseCode = "400", description = "리뷰 작성 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"400\", \"message\": \"리뷰 작성 실패\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
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

    @GetMapping("/company/reviews")
    @Operation(summary = "기업 사용자 리뷰 전체 조회", description = "기업 사용자가 자신에 대한 모든 리뷰를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"조회 성공\", \"data\": {\"reviews\": [{\"reviewId\": 1, \"reviewTitle\": \"친절한 사장님!\", \"reviewContent\": \"사장님이 맛있고 사과가 친절해요.\", \"rating\": 5}, {\"reviewId\": 2, \"reviewTitle\": \"좋은 품질\", \"reviewContent\": \"상품의 품질이 아주 좋습니다.\", \"rating\": 4}], \"totalReviewCount\": 2 }}"))),
            @ApiResponse(responseCode = "404", description = "기업 사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"404\", \"message\": \"기업 사용자를 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
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
}