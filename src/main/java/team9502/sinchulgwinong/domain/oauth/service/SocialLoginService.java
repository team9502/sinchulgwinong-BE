package team9502.sinchulgwinong.domain.oauth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import team9502.sinchulgwinong.domain.auth.dto.request.UserSignupRequestDTO;
import team9502.sinchulgwinong.domain.oauth.dto.request.SocialLoginRequestDTO;
import team9502.sinchulgwinong.domain.oauth.enums.SocialType;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final PointService pointService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Transactional
    public void createOrUpdateSocialLogin(SocialLoginRequestDTO requestDTO, SocialType socialType) {
        String email = getEmailFromCode(requestDTO.getCode());
        userRepository.findByEmail(email)
                .orElseGet(() -> createUser(requestDTO, socialType, email));
    }

    @Transactional
    public String handleGoogleCallback(String code) throws Exception {
        String email = getEmailFromCode(code);

        // 구글 사용자 정보를 추가로 가져와 username과 nickname을 설정합니다.
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(getAccessToken(code));

        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, String.class);

        if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode userInfo = mapper.readTree(userInfoResponse.getBody());
            String username = userInfo.get("name").asText();
            String nickname = userInfo.get("given_name").asText();

            // 임시로 소셜 로그인 사용자 정보 세션에 저장
            // 클라이언트 측에서 추가 정보 입력 폼으로 리디렉션 후 이 정보를 사용
            return "redirect:/social-login/additional-info?code=" + code + "&username=" + username + "&nickname=" + nickname;
        } else {
            throw new RuntimeException("사용자 정보 요청 실패");
        }
    }

    private String getEmailFromCode(String code) {
        try {
            String accessToken = getAccessToken(code);
            String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);

            HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
            ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, String.class);

            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode userInfo = mapper.readTree(userInfoResponse.getBody());
                return userInfo.get("email").asText();
            } else {
                System.out.println("Error Response: " + userInfoResponse.getBody());
                throw new RuntimeException("사용자 정보 요청 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이메일 추출 실패: " + e.getMessage(), e);
        }
    }

    private String getAccessToken(String code) {
        try {
            restTemplate.setMessageConverters(Arrays.asList(new FormHttpMessageConverter(), new StringHttpMessageConverter(StandardCharsets.UTF_8)));

            String accessTokenUrl = "https://oauth2.googleapis.com/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(accessTokenUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.getBody());
                return jsonNode.get("access_token").asText();
            } else {
                System.out.println("Error Response: " + response.getBody());
                throw new RuntimeException("액세스 토큰 요청 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("액세스 토큰 추출 실패: " + e.getMessage(), e);
        }
    }

    private User createUser(SocialLoginRequestDTO requestDTO, SocialType socialType, String email) {
        User newUser = User.builder()
                .email(email)
                .nickname(requestDTO.getNickname())
                .username(requestDTO.getUsername())
                .phoneNumber(requestDTO.getPhoneNumber())
                .password("social_login")
                .loginType(socialType)
                .build();
        User savedUser = userRepository.save(newUser);
        pointService.earnPoints(savedUser, SpType.SIGNUP); // 포인트 적립
        return savedUser;
    }
}
