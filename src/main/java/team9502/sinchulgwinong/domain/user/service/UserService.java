package team9502.sinchulgwinong.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.user.dto.response.UserProfileResponseDTO;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResponseDTO getUserProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        return new UserProfileResponseDTO(
                userId,
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getPhoneNumber());
    }
}
