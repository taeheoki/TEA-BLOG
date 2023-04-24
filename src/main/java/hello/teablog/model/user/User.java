package hello.teablog.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder // AllArgsConstructor이 있어야 붙일 수 있다.
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 개발자가 직접 new 할수 없도록
@Getter
@Table(name = "user_tb")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 20)
    private String username;
    @Column(length = 60) // 실제로는 20자 이하만 받을 것, 그러나 암호화 과정에서 60자가 된다.
    private String password;
    @Column(length = 50)
    private String email;
    private String role; // USER(고객)
    private String profile; // 유저 프로필 사진의 경로
    private Boolean status;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;

    // 프로필 사진 변경의 용도
    public void changeProfile(String profile){
        this.profile = profile;
    }

    // 회원정보 수정시 사용
    public void update(String password, String email) {
        this.password = password;
        this.email = email;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
