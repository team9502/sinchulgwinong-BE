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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team9502.sinchulgwinong.domain.point.dto.response.PointSummaryResponseDTO;
import team9502.sinchulgwinong.domain.point.dto.response.SavedPointDetailResponseDTO;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_POINT_SUMMARY_READ;
import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_SAVED_POINT_READ;

@RestController
@RequiredArgsConstructor
@Tag(name = "Point", description = "포인트 관련 API [김은채]")
@RequestMapping("/points")
public class PointController {

    private final PointService pointService;

    @GetMapping
    @Operation(summary = "포인트 총액 조회", description = "로그인한 사용자의 적립 및 사용 포인트 총액을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 총액 조회 성공",
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

    @GetMapping("/details")
    @Operation(summary = "포인트 적립 내역 조회", description = "로그인한 사용자의 포인트 적립 내역을 커서 기반 페이지네이션으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 적립 내역 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"200\", \"message\": \"적립 포인트 조회 성공\", \"data\": [{\"type\": \"REVIEW\", \"savedPoint\": 300, \"createdAt\": \"2024-06-11\"}, {\"type\": \"SIGNUP\", \"savedPoint\": 300, \"createdAt\": \"2024-06-11\"}] }"))),
            @ApiResponse(responseCode = "404", description = "포인트를 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"404\", \"message\": \"포인트를 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"500\", \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<List<SavedPointDetailResponseDTO>>> getPointDetails(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "cursorId", required = false) Long cursorId,
            @RequestParam(value = "limit", defaultValue = "6") int limit) {

        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }

        List<SavedPointDetailResponseDTO> responseDTOs = pointService.getSpDetails(userDetails, cursorId, limit);

        return ResponseEntity.status(SUCCESS_SAVED_POINT_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_SAVED_POINT_READ.getMessage(),
                                responseDTOs
                        )
                );
    }
}
