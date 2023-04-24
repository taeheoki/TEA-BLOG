package hello.teablog.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.teablog.DummyEntity;
import hello.teablog.model.board.Board;
import hello.teablog.model.board.BoardRepository;
import hello.teablog.model.user.User;
import hello.teablog.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@Import(BCryptPasswordEncoder.class)
@DataJpaTest
public class BoardRepositoryTest extends DummyEntity {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private void join_good(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<User> userList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            userList.add(newUser("user"+i, passwordEncoder));
        }
        userRepository.saveAll(userList);

        List<Board> boardList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            boardList.add(newBoard("제목"+i, userList.get(i-1)));
        }
        boardRepository.saveAll(boardList);
        em.clear();
    }

    private void in_query_good(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            userList.add(newUser("user"+i, passwordEncoder));
        }
        userRepository.saveAll(userList);

        List<Board> boardList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            boardList.add(newBoard("제목"+i, userList.get(0)));
        }
        for (int i = 5; i < 9; i++) {
            boardList.add(newBoard("제목"+i, userList.get(1)));
        }
        for (int i = 9; i < 11; i++) {
            boardList.add(newBoard("제목"+i, userList.get(2)));
        }
        boardRepository.saveAll(boardList);
        em.clear();
    }

    @BeforeEach
    public void setUp(){
        in_query_good();
    }

    @Test
    public void findByUserIds_test() {
        List<Long> userIds = boardRepository.findUserIdDistinct();
        List<User> userList = userRepository.findByUserIds(userIds); // user1, user2, user3
        List<Board> boardList = boardRepository.findByUserIds(userIds);
    }

    @Test
    public void findAllFetchUser_test() {
        boardRepository.findAllFetchUser();
    }

    @Test
    public void findAll_test(){
        // Lazy 전략 -> Board만 조회
        List<Board> boardList = boardRepository.findAll();
        // Lazy Loading 하기 (20바퀴)
        boardList.stream().forEach(board -> {
            // loop 1번에 select (ssar)
            // loop 2~10번 1차 캐싱 (PC)
            // loop 11번에 select (cos)
            // loop 12~20번 1차 캐싱 (PC)
            System.out.println(board.getUser().getUsername()); // select 2번
        });
    }

    @Test
    public void findAllV3_Test() throws JsonProcessingException {
        // Lazy -> Board만 조회
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("id").descending());
        Page<Board> boardPG = this.boardRepository.findAll(pageRequest);
        boardPG.stream().forEach(board -> {
            System.out.println(board.getUser().getUsername()); // select 2번
        });
        String responseBody = new ObjectMapper().writeValueAsString(boardPG);
        System.out.println("디버그 : " + responseBody);
    }

}