package team9502.sinchulgwinong.domain.email.service;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.FlagTerm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team9502.sinchulgwinong.domain.review.service.ReviewService;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailReceiverService {

    private static final Logger log = LoggerFactory.getLogger(EmailReceiverService.class);
    private final ReviewService reviewService;

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
            log.info("메시지 내용: {}", content);

            try {
                Long reviewId = extractReviewId(content);
                log.info("추출된 리뷰 ID: {}", reviewId);

                if (content.contains("리뷰 삭제에 동의합니다.")) {
                    reviewService.deleteReview(reviewId);
                } else if (content.contains("리뷰 삭제에 비동의합니다.")) {
                    reviewService.blindReview(reviewId, 30);
                }
            } catch (Exception e) {
                log.error("리뷰 처리 중 에러 발생", e);
            }
        }

        emailFolder.close(false);
        store.close();
    }

    private Long extractReviewId(String content) {
        Pattern pattern = Pattern.compile("리뷰 ID: (\\d+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new IllegalArgumentException("리뷰 ID를 찾을 수 없습니다.");
    }

    private String getTextFromMessage(Message message) throws MessagingException {
        try {
            if (message.isMimeType("text/plain")) {
                return message.getContent().toString();
            } else if (message.isMimeType("multipart/*")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                return mimeMultipart.getBodyPart(0).getContent().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}