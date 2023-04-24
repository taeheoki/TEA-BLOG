package hello.teablog;

import hello.teablog.model.board.Board;
import hello.teablog.model.board.BoardRepository;
import hello.teablog.model.user.User;
import hello.teablog.model.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TeablogApplication extends DummyEntity {

    @Profile("dev")
    @Bean
    CommandLineRunner init(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, BoardRepository boardRepository) {
        return args -> {
            User ssar = newUser("ssar", passwordEncoder);
            User cos = newUser("cos", passwordEncoder);
            userRepository.saveAll(Arrays.asList(ssar, cos));

            List<Board> boardList = new ArrayList<>();
            for (int i = 1; i < 11; i++) {
                boardList.add(newBoard("제목"+i, ssar));
            }
            for (int i = 11; i < 21; i++) {
                boardList.add(newBoard("제목"+i, cos));
            }
            boardRepository.saveAll(boardList);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(TeablogApplication.class, args);
    }

}
