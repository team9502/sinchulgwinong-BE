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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team9502.sinchulgwinong.domain.faq.dto.request.FaqCreationRequestDTO;
import team9502.sinchulgwinong.domain.faq.dto.response.FaqResponseDTO;
import team9502.sinchulgwinong.domain.faq.service.FaqService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;

import static team9502.sinchulgwinong.global.response.SuccessCode.SUCCESS_FAQ_CREATE;

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
}
