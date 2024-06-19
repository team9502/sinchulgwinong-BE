package team9502.sinchulgwinong.domain.point.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
import team9502.sinchulgwinong.domain.point.dto.response.PagedResponseDTO;
import team9502.sinchulgwinong.domain.point.dto.response.PointSummaryResponseDTO;
import team9502.sinchulgwinong.domain.point.dto.response.SavedPointDetailResponseDTO;
import team9502.sinchulgwinong.domain.point.dto.response.UsedPointDetailResponseDTO;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

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
                            examples = @ExampleObject(value = "{ \"message\": \"포인트 총액 조회 성공\", \"data\": {\"totalSaved\": 400, \"totalUsed\": 0} }"))),
            @ApiResponse(responseCode = "404", description = "포인트 미존재",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"포인트를 찾을 수 없습니다.\", \"data\": null }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
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

    @GetMapping("/saved")
    @Operation(summary = "포인트 적립 내역 조회", description = "로그인한 사용자의 포인트 적립 내역을 커서 기반 페이지네이션으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 적립 내역 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"적립 포인트 조회 성공\", \"data\": { \"data\": [{\"type\": \"BOARD\", \"savedPoint\": 100, \"createdAt\": \"2024-06-19\"}, {\"type\": \"SIGNUP\", \"savedPoint\": 300, \"createdAt\": \"2024-06-18\"}], \"hasNextPage\": false} }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
    })
    @Parameter(
            name = "cursorId",
            description = "이전 로드에서 마지막으로 보여진 포인트 내역의 ID, 다음 페이지를 불러올 시작점, 생략 가능",
            required = false,
            schema = @Schema(type = "long", defaultValue = "최신 데이터부터 시작")
    )
    @Parameter(
            name = "limit",
            description = "불러올 최대 데이터 수, 생략 가능, 생략 시 기본값 사용",
            required = false,
            schema = @Schema(type = "integer", defaultValue = "6")
    )
    public ResponseEntity<GlobalApiResponse<PagedResponseDTO<SavedPointDetailResponseDTO>>> getPointDetails(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "cursorId", required = false) Long cursorId,
            @RequestParam(value = "limit", defaultValue = "6") int limit) {

        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }

        PagedResponseDTO<SavedPointDetailResponseDTO> responseDTOs = pointService.getSpDetails(userDetails, cursorId, limit);

        return ResponseEntity.status(SUCCESS_SAVED_POINT_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_SAVED_POINT_READ.getMessage(),
                                responseDTOs
                        )
                );
    }

    @GetMapping("/used")
    @Operation(
            summary = "포인트 사용 내역 조회",
            description = "로그인한 사용자의 포인트 사용 내역을 커서 기반 페이지네이션으로 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "cursorId",
                            description = "이전 로드에서 마지막으로 보여진 포인트 내역의 ID, 다음 페이지를 불러올 시작점, 생략 가능",
                            required = false,
                            schema = @Schema(type = "long", defaultValue = "최신 데이터부터 시작")
                    ),
                    @Parameter(
                            name = "limit",
                            description = "불러올 최대 데이터 수, 생략 가능, 생략 시 기본값(6) 사용",
                            required = false,
                            schema = @Schema(type = "integer", defaultValue = "6")
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 사용 내역 조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"message\": \"사용 포인트 조회 성공\", \"data\": [{\"upType\": \"REVIEW\", \"upAmount\": 100, \"usedAt\": \"2024-06-11\"}, {\"upType\": \"REVIEW\", \"upAmount\": 100, \"usedAt\": \"2024-06-11\"}], \"hasNextPage\": false}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "포인트를 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"포인트를 찾을 수 없습니다.\", \"data\": null }")
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")
                    )
            )
    })
    public ResponseEntity<GlobalApiResponse<PagedResponseDTO<UsedPointDetailResponseDTO>>> getUsedPointDetails(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "cursorId", required = false) Long cursorId,
            @RequestParam(value = "limit", defaultValue = "6") int limit) {

        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }

        PagedResponseDTO<UsedPointDetailResponseDTO> responseDTOs = pointService.getUpDetails(userDetails, cursorId, limit);

        return ResponseEntity.status(SUCCESS_USED_POINT_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_USED_POINT_READ.getMessage(),
                                responseDTOs
                        )
                );
    }
}
