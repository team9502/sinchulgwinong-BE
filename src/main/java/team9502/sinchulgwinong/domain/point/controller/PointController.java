package team9502.sinchulgwinong.domain.point.controller;

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
import team9502.sinchulgwinong.domain.point.dto.response.PointSummaryResponseDTO;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_POINT_SUMMARY_READ;

@RestController
@RequiredArgsConstructor
@Tag(name = "Point", description = "포인트 관련 API")
@RequestMapping("/points")
public class PointController {

    private final PointService pointService;

    @GetMapping("/{userId}")
    @Operation(summary = "사용자 포인트 조회", description = "사용자의 적립 및 사용 포인트 총액을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"포인트 조회 성공\", \"data\": {\"totalSaved\": 500, \"totalUsed\": 300} }"))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"404\", \"message\": \"사용자를 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<PointSummaryResponseDTO>> getPointSummary(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PointSummaryResponseDTO responseDTO = pointService.getPointSummary(userDetails);

        return ResponseEntity.status(SUCCESS_POINT_SUMMARY_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_POINT_SUMMARY_READ.getMessage(),
                                responseDTO
                        )
                );
    }
}
