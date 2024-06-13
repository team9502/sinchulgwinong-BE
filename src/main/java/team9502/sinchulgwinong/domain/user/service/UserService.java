package team9502.sinchulgwinong.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.user.dto.request.UserProfileUpdateRequestDTO;
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

    @Transactional
    public UserProfileResponseDTO updateUserProfile(Long userId, UserProfileUpdateRequestDTO requestDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (requestDTO.getUsername() != null) {
            user.setUsername(requestDTO.getUsername());
        }
        if (requestDTO.getNickname() != null) {
            user.setNickname(requestDTO.getNickname());
        }
        if (requestDTO.getEmail() != null) {
            user.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(requestDTO.getPhoneNumber());
        }

        userRepository.save(user);

        return new UserProfileResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getPhoneNumber());
    }
}