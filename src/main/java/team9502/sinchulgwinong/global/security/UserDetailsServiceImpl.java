package team9502.sinchulgwinong.global.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, CompanyUserRepository companyUserRepository) {
        this.userRepository = userRepository;
        this.companyUserRepository = companyUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return new UserDetailsImpl(user.getEmail(), user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")), "USER");
        }

        CompanyUser companyUser = companyUserRepository.findByCpEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(email + "으로 등록된 사용자를 찾을 수 없습니다."));
        return new UserDetailsImpl(companyUser.getCpEmail(), companyUser.getCpPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_COMPANY")), "COMPANY");
    }
}
