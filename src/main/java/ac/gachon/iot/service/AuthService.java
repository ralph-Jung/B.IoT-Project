package ac.gachon.iot.service;

import ac.gachon.iot.config.JwtProvider;
import ac.gachon.iot.domain.entity.User;
import ac.gachon.iot.domain.repository.UserRepository;
import ac.gachon.iot.dto.AuthLoginRequest;
import ac.gachon.iot.dto.AuthLoginResponse;
import ac.gachon.iot.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AuthLoginResponse login(AuthLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return AuthLoginResponse.builder()
                .token(jwtProvider.generateToken(user.getId()))
                .build();
    }
}
