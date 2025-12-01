package team9502.sinchulgwinong.domain.email.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.FlagTerm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team9502.sinchulgwinong.domain.review.entity.ReviewVisibilityRequests;
import team9502.sinchulgwinong.domain.review.enums.ReviewerResponse;
import team9502.sinchulgwinong.domain.review.repository.ReviewVisibilityRequestsRepository;
import team9502.sinchulgwinong.domain.review.service.ReviewService;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailReceiverService {

    private static final Logger log = LoggerFactory.getLogger(EmailReceiverService.class);
    private final ReviewService reviewService;
    private final ReviewVisibilityRequestsRepository reviewVisibilityRequestsRepository;

    @Value("${GOOGLE_ACCOUNT}")
    private String googleAccount;

    @Value("${GOOGLE_PASSWORD}")
    private String googlePassword;

    public void checkEmailForReviewResponses() throws MessagingException {
        log.info("메일 체크 시작");
        String host = "imap.gmail.com";

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.starttls.enable", "true");

        Session emailSession = Session.getInstance(properties);
        Store store = emailSession.getStore("imaps");
        store.connect(googleAccount, googlePassword);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        log.info("읽지 않은 메시지 수: {}", messages.length);

        for (Message message : messages) {
            String content = getTextFromMessage(message);
            String senderEmail = extractSenderEmail(message);

            log.info("메시지 내용: {}", content);
            log.info("발신자: {}", senderEmail);

            try {
                String responseToken = extractResponseToken(content);
                log.info("추출된 참조 번호: {}", responseToken);

                ReviewVisibilityRequests request = reviewVisibilityRequestsRepository
                    .findByResponseToken(responseToken)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 참조 번호입니다: " + responseToken));

                String expectedEmail = request.getReview().getUser().getEmail();
                if (!expectedEmail.equalsIgnoreCase(senderEmail)) {
                    log.error("발신자 불일치 - 예상: {}, 실제: {}", expectedEmail, senderEmail);
                    continue;
                }

                if (request.getReviewerResponse() != ReviewerResponse.UNANSWERED) {
                    log.warn("이미 처리된 요청입니다. 토큰: {}, 상태: {}", responseToken, request.getReviewerResponse());
                    continue;
                }

                Long reviewId = request.getReview().getReviewId();

                if (content.contains("리뷰 삭제에 동의합니다.")) {
                    log.info("리뷰 삭제 동의 - 리뷰 ID: {}", reviewId);
                    reviewService.deleteReview(reviewId);
                } else if (content.contains("리뷰 삭제에 비동의합니다.")) {
                    log.info("리뷰 삭제 비동의 - 리뷰 ID: {}, 30일 블라인드 처리", reviewId);
                    reviewService.blindReview(reviewId, 30);
                } else {
                    log.warn("명확한 응답 문구를 찾을 수 없습니다. 토큰: {}", responseToken);
                }
            } catch (Exception e) {
                log.error("리뷰 처리 중 에러 발생", e);
            }
        }

        emailFolder.close(false);
        store.close();
    }

    private String extractResponseToken(String content) {
        Pattern pattern = Pattern.compile("참조 번호:\\s*([a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12})");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("참조 번호를 찾을 수 없습니다.");
    }

    private String extractSenderEmail(Message message) throws MessagingException {
        Address[] froms = message.getFrom();
        if (froms != null && froms.length > 0) {
            return ((InternetAddress) froms[0]).getAddress();
        }
        throw new MessagingException("발신자 정보를 찾을 수 없습니다.");
    }

    private String getTextFromMessage(Message message) throws MessagingException {
        try {
            if (message.isMimeType("text/plain")) {
                return message.getContent().toString();
            } else if (message.isMimeType("multipart/*")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                return getTextFromMimeMultipart(mimeMultipart);
            }
        } catch (Exception e) {
            log.error("메시지 파싱 중 오류", e);
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append(html);
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
}