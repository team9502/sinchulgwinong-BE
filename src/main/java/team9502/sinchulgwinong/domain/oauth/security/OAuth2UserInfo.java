package team9502.sinchulgwinong.domain.oauth.security;

public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
