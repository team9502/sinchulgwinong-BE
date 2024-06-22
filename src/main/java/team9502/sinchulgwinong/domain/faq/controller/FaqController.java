package team9502.sinchulgwinong.domain.faq.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.faq.dto.request.FaqCreationRequestDTO;
import team9502.sinchulgwinong.domain.faq.dto.request.FaqUpdateRequestDTO;
import team9502.sinchulgwinong.domain.faq.dto.response.FaqListResponseDTO;
import team9502.sinchulgwinong.domain.faq.dto.response.FaqResponseDTO;
import team9502.sinchulgwinong.domain.faq.service.FaqService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;

import java.util.List;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faqs")
@Tag(name = "Faq", description = "자주 묻는 질문 관련 API, 사용 금지❌ [김은채]")
public class FaqController {

    private final FaqService faqService;

    @PostMapping
    @Operation(summary = "FAQ 생성",
            description = "요청 바디의 유효성 검사에서 실패할 경우, 각 필드의 문제를 설명하는 오류 메시지를 반환합니다. " +
                    "이는 요청 데이터가 필수 필드를 포함하지 않거나, 필드 값이 지정된 제한을 초과하는 경우 발생할 수 있습니다. " +
                    "가능한 오류 사례로는 FAQ 제목 또는 내용이 빈 값이거나, 제목의 길이가 100자를 초과하거나, 내용의 길이가 1000자를 초과하는 경우입니다. " +
                    "모든 유효성 검사 실패는 상태 코드와 오류 메시지가 응답에 포함됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "FAQ 생성 성공", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"FAQ 생성 성공\", \"data\": {\"faqId\": 1, \"faqTitle\": \"제목\", \"faqContent\": \"내용\", \"createdAt\": \"2024-06-22T20:53:42.62033\", \"modifiedAt\": \"2024-06-22T20:53:42.62033\"} }"))),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "NULL_FAQ_TITLE", summary = "제목 필드 누락",
                                            value = "{ \"message\": \"유효성 검사 실패\", \"data\": {\"faqTitle\": \"FAQ 제목은 필수입니다.\"} }"),
                                    @ExampleObject(name = "NULL_FAQ_CONTENT", summary = "내용 필드 누락",
                                            value = "{ \"message\": \"유효성 검사 실패\", \"data\": {\"faqContent\": \"FAQ 내용은 필수입니다.\"} }"),
                                    @ExampleObject(name = "필드 누락과 글자수 초과는 동시에 응답에 포함될 수 있습니다.", summary = "글자수 초과",
                                            value = "{ \"message\": \"유효성 검사 실패\", \"data\": {\"faqTitle\": \"FAQ 제목은 100자를 초과할 수 없습니다.\", \"faqContent\": \"FAQ 내용은 1000자를 초과할 수 없습니다.\"} }")
                            })),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<FaqResponseDTO>> createFaq(
            @Valid @RequestBody FaqCreationRequestDTO requestDTO) {

        FaqResponseDTO responseDTO = faqService.createFaq(requestDTO);

        return ResponseEntity.status(SUCCESS_FAQ_CREATE.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_FAQ_CREATE.getMessage(),
                                responseDTO
                        )
                );
    }

    @GetMapping
    @Operation(summary = "FAQ 전체 조회",
            description = "FAQ 전체 목록을 조회합니다. " +
                    "FAQ 목록에 대한 응답값은 FAQ ID, 제목, 생성일, 수정일, 조회수로 구성됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FAQ 전체 조회 성공", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"FAQ 전체 조회 성공\", \"data\": [{\"faqId\": 1, \"faqTitle\": \"제목\", \"createdAt\": \"2024-06-22T20:53:42.62033\", \"modifiedAt\": \"2024-06-22T20:53:42.62033\", \"viewCount\": 0}] }"))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"서버 에러\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<List<FaqListResponseDTO>>> findAllFaqs() {

        List<FaqListResponseDTO> faqs = faqService.findAllFaqs();

        return ResponseEntity.status(SUCCESS_FAQ_LIST_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_FAQ_LIST_READ.getMessage(),
                                faqs
                        )
                );
    }

    @GetMapping("/{faqId}")
    @Operation(summary = "FAQ 상세 조회", description = "주어진 ID의 FAQ 상세 정보를 조회합니다. 조회시 조회수도 증가됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FAQ 상세 조회 성공", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"FAQ 상세 조회 성공\", \"data\": {\"faqId\": 1, \"faqTitle\": \"What is API?\", \"faqContent\": \"API stands for Application Programming Interface.\", \"createdAt\": \"2024-06-22T20:53:42.62033\", \"modifiedAt\": \"2024-06-22T20:53:42.62033\", \"viewCount\": 1} }"))),
            @ApiResponse(responseCode = "404", description = "FAQ not found", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"FAQ not found\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<FaqResponseDTO>> getFaqDetail(
            @PathVariable(value = "faqId") Long faqId) {

        FaqResponseDTO responseDTO = faqService.getFaqDetail(faqId);

        return ResponseEntity.status(SUCCESS_FAQ_READ.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_FAQ_READ.getMessage(),
                                responseDTO
                        )
                );
    }

    @PatchMapping("/{faqId}")
    @Operation(summary = "FAQ 수정", description = "주어진 ID의 FAQ 제목과 내용을 수정합니다. 제목 또는 내용 중 하나만 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FAQ 수정 성공", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"FAQ 수정 성공\", \"data\": {\"faqId\": 1, \"faqTitle\": \"Updated Title\", \"faqContent\": \"Updated Content\", \"createdAt\": \"2024-06-22T20:53:42.62033\", \"modifiedAt\": \"2024-06-22T21:53:42.62033\", \"viewCount\": 1} }"))),
            @ApiResponse(responseCode = "404", description = "FAQ not found", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"FAQ를 찾을 수 없습니다.\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<FaqResponseDTO>> updateFaq(
            @PathVariable(value = "faqId") Long faqId,
            @Valid @RequestBody FaqUpdateRequestDTO requestDTO) {

        FaqResponseDTO responseDTO = faqService.updateFaq(faqId, requestDTO);

        return ResponseEntity.status(SUCCESS_FAQ_UPDATE.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_FAQ_UPDATE.getMessage(),
                                responseDTO
                        )
                );
    }

    @DeleteMapping("/{faqId}")
    @Operation(summary = "FAQ 삭제", description = "주어진 ID의 FAQ를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "FAQ 삭제 성공", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"FAQ 삭제 성공\", \"data\": null }"))),
            @ApiResponse(responseCode = "404", description = "FAQ not found", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{ \"message\": \"FAQ를 찾을 수 없습니다.\", \"data\": null }")))
    })
    public ResponseEntity<GlobalApiResponse<Void>> deleteFaq(
            @PathVariable(value = "faqId") Long faqId) {

        faqService.deleteFaq(faqId);

        return ResponseEntity.status(SUCCESS_FAQ_DELETE.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_FAQ_DELETE.getMessage(),
                                null
                        )
                );
    }
}
