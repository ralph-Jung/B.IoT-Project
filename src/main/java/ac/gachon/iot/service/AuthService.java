package ac.gachon.iot.service;

import ac.gachon.iot.config.JwtProvider;
import ac.gachon.iot.domain.entity.User;
import ac.gachon.iot.domain.repository.UserRepository;
import ac.gachon.iot.dto.AuthLoginRequest;
import ac.gachon.iot.dto.AuthLoginResponse;
import ac.gachon.iot.exception.InvalidCredentialsException;
import io.jsonwebtoken.JwtBuilder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new InvalidCredentialsException("Invalid"));

        return AuthLoginResponse.builder()
                .token(jwtProvider.generateToken(user.getId()))
                .build();

    }
}
