package hello.teablog.dto.board;

import hello.teablog.model.board.Board;
import hello.teablog.model.user.User;
import lombok.Getter;
import lombok.Setter;

public class BoardRequest {
    @Getter
    @Setter
    public static class SaveInDTO {
        private String title;
        private String content;

        public Board toEntity(User user) {
            return Board.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .thumbnail(null)
                    .build();
        }
    }
}
