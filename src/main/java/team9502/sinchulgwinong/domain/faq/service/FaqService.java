package team9502.sinchulgwinong.domain.faq.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team9502.sinchulgwinong.domain.faq.dto.request.FaqCreationRequestDTO;
import team9502.sinchulgwinong.domain.faq.dto.response.FaqResponseDTO;
import team9502.sinchulgwinong.domain.faq.entity.Faq;
import team9502.sinchulgwinong.domain.faq.repository.FaqRepository;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional
    public FaqResponseDTO createFaq(FaqCreationRequestDTO requestDTO) {

        Faq faq = Faq.builder()
                .faqTitle(requestDTO.getFaqTitle())
                .faqContent(requestDTO.getFaqContent())
                .build();

        Faq savedFaq = faqRepository.save(faq);

        return new FaqResponseDTO(
                savedFaq.getFaqId(),
                savedFaq.getFaqTitle(),
                savedFaq.getFaqContent(),
                savedFaq.getCreatedAt(),
                savedFaq.getModifiedAt()
        );
    }
}
