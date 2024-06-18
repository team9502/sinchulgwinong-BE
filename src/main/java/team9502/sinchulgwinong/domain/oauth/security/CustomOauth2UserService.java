package team9502.sinchulgwinong.domain.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;

import java.util.Map;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Autowired
    public CustomOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        String provider = request.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo userInfo = new GoogleUserDetails(attributes);

        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> registerNewUser(userInfo));

        return new CustomOauth2UserDetails(userInfo, attributes);
    }

    private User registerNewUser(OAuth2UserInfo userInfo) {
        User user = new User();
        user.setEmail(userInfo.getEmail());
        user.setUsername(userInfo.getName());

        return userRepository.save(user);
    }
}
