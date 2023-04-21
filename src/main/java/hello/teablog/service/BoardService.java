package hello.teablog.service;

import hello.teablog.dto.board.BoardRequest;
import hello.teablog.dto.user.UserRequest;
import hello.teablog.model.board.Board;
import hello.teablog.model.board.BoardRepository;
import hello.teablog.model.user.User;
import hello.teablog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {


    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void 글쓰기(BoardRequest.SaveInDTO saveInDTO, Long userId) {
        try {
            // 1. 유저 존재 확인
            User userPS = userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("유저를 찾을 수 없습니다.")
            );

            // 2. 게시글 쓰기
            boardRepository.save(saveInDTO.toEntity(userPS));
        } catch (Exception e) {
            throw new RuntimeException("글쓰기 실패 : " + e.getMessage());
        }
    }

}
