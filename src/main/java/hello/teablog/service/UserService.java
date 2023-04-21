package hello.teablog.service;

import hello.teablog.dto.user.UserRequest;
import hello.teablog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // SecurityConfig Bean 등록

    // insert, update, delete -> try catch 처리
    @Transactional
    public void 회원가입(UserRequest.JoinInDTO joinInDTO) {
        try {
            // 1. 패스워드 암호화
            joinInDTO.setPassword(passwordEncoder.encode(joinInDTO.getPassword()));

            // 2. DB 저장
            userRepository.save(joinInDTO.toEntity());
        } catch (Exception e) {
            throw new RuntimeException("회원가입 오류 : " + e.getMessage());
        }

    } // 더티체킹, DB 세션 종료(OSIV=false)) -> open-in-view: false
}
