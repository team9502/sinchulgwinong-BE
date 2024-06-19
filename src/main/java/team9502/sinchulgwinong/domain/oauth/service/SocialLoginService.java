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
    public User createOrUpdateSocialLogin(UserSignupRequestDTO requestDTO, SocialType socialType) {
        return userRepository.findByEmail(requestDTO.getEmail())
                .orElseGet(() -> createUser(requestDTO, socialType));
    }

    @Transactional
    public String handleGoogleCallback(String code) throws Exception {

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
            String responseBody = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            String accessToken = jsonNode.get("access_token").asText();

            String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);

            HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
            ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, String.class);

            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                // Google 사용자 정보 파싱
                JsonNode userInfo = mapper.readTree(userInfoResponse.getBody());
                String email = userInfo.get("email").asText();
                String username = userInfo.get("name").asText();
                String nickname = userInfo.get("given_name").asText();

                // 임시로 소셜 로그인 사용자 정보 세션에 저장
                // 클라이언트 측에서 추가 정보 입력 폼으로 리디렉션 후 이 정보를 사용
                return "redirect:/social-login/additional-info?email=" + email + "&username=" + username + "&nickname=" + nickname;
            } else {
                throw new RuntimeException("사용자 정보 요청 실패");
            }
        } else {
            // 오류 메시지 로깅
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            throw new RuntimeException("액세스 토큰 요청 실패");
        }
    }

    private User createUser(UserSignupRequestDTO requestDTO, SocialType socialType) {
        User newUser = User.builder()
                .email(requestDTO.getEmail())
                .nickname(requestDTO.getNickname())
                .password(requestDTO.getPassword())
                .username(requestDTO.getUsername())
                .phoneNumber(requestDTO.getPhoneNumber())
                .loginType(socialType)
                .build();
        User savedUser = userRepository.save(newUser);
        pointService.earnPoints(savedUser, SpType.SIGNUP); // 포인트 적립
        return savedUser;
    }
}
