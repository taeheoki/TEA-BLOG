package hello.teablog;

import hello.teablog.model.board.Board;
import hello.teablog.model.board.BoardRepository;
import hello.teablog.model.user.User;
import hello.teablog.model.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class TeablogApplication {

    @Bean
    CommandLineRunner init(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, BoardRepository boardRepository) {
        return args -> {
            User ssar = User.builder()
                    .username("ssar")
                    .password(passwordEncoder.encode("1234"))
                    .email("ssar@nate.com")
                    .role("USER")
                    .profile("person.png")
                    .build();
            User cos = User.builder()
                    .username("cos")
                    .password(passwordEncoder.encode("1234"))
                    .email("cos@nate.com")
                    .role("USER")
                    .profile("person.png")
                    .build();
            userRepository.saveAll(Arrays.asList(ssar, cos));

            Board b1 = Board.builder()
                    .title("제목1")
                    .content("내용1")
                    .user(ssar)
                    .thumbnail("/upload/person.png")
                    .build();
            Board b2 = Board.builder()
                    .title("제목2")
                    .content("내용2")
                    .user(cos)
                    .thumbnail("/upload/person.png")
                    .build();
            boardRepository.saveAll(Arrays.asList(b1, b2));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(TeablogApplication.class, args);
    }

}
