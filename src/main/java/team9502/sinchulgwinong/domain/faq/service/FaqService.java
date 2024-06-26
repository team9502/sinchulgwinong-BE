package team9502.sinchulgwinong.domain.faq.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.faq.dto.request.FaqCreationRequestDTO;
import team9502.sinchulgwinong.domain.faq.dto.request.FaqUpdateRequestDTO;
import team9502.sinchulgwinong.domain.faq.dto.response.FaqListResponseDTO;
import team9502.sinchulgwinong.domain.faq.dto.response.FaqPageResponseDTO;
import team9502.sinchulgwinong.domain.faq.dto.response.FaqResponseDTO;
import team9502.sinchulgwinong.domain.faq.entity.Faq;
import team9502.sinchulgwinong.domain.faq.repository.FaqRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional
    public FaqResponseDTO createFaq(FaqCreationRequestDTO requestDTO) {

        Faq faq = Faq.builder()
                .faqTitle(requestDTO.getFaqTitle())
                .faqContent(requestDTO.getFaqContent())
                .viewCount(0) // 초기 조회수 설정
                .build();
        faq = faqRepository.save(faq);

        return convertToResponseDTO(faq);
    }

    @Transactional(readOnly = true)
    public FaqPageResponseDTO findAllFaqs(Pageable pageable) {

        Page<Faq> faqPage = faqRepository.findAll(pageable);
        return new FaqPageResponseDTO(
                faqPage.stream().map(this::convertToListDTO).collect(Collectors.toList()),
                (int) faqPage.getTotalElements(),
                faqPage.getNumber(),
                faqPage.getTotalPages()
        );
    }

    @Transactional
    public FaqResponseDTO getFaqDetail(Long faqId) {

        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new ApiException(ErrorCode.FAQ_NOT_FOUND));
        faq.incrementViewCount();
        faqRepository.save(faq);

        return convertToResponseDTO(faq);
    }

    @Transactional
    public FaqResponseDTO updateFaq(Long faqId, FaqUpdateRequestDTO requestDTO) {

        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new ApiException(ErrorCode.FAQ_NOT_FOUND));

        if (requestDTO.getFaqTitle() != null) {
            faq.setFaqTitle(requestDTO.getFaqTitle());
        }

        if (requestDTO.getFaqContent() != null) {
            faq.setFaqContent(requestDTO.getFaqContent());
        }

        faq = faqRepository.save(faq);
        return convertToResponseDTO(faq);
    }

    @Transactional
    public void deleteFaq(Long faqId) {

        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new ApiException(ErrorCode.FAQ_NOT_FOUND));

        faqRepository.delete(faq);
    }


    /*
        공통 로직 메서드로 분리
     */
    private FaqResponseDTO convertToResponseDTO(Faq faq) {

        return new FaqResponseDTO(
                faq.getFaqId(),
                faq.getFaqTitle(),
                faq.getFaqContent(),
                faq.getCreatedAt(),
                faq.getModifiedAt(),
                faq.getViewCount()
        );
    }

    private FaqListResponseDTO convertToListDTO(Faq faq) {

        return new FaqListResponseDTO(
                faq.getFaqId(),
                faq.getFaqTitle(),
                faq.getCreatedAt(),
                faq.getModifiedAt(),
                faq.getViewCount()
        );
    }
}
