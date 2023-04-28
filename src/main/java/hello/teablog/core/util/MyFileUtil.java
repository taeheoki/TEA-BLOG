package hello.teablog.core.util;

import hello.teablog.core.exception.ssr.Exception500;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class MyFileUtil {
    public static String write(String uploadFolder, MultipartFile file) {
        // 롤링 기법 (사진명을 202304280740_random_person.png) 충돌나지않게 방지 하지만 시간은 동시에 파일이 들어올수 있기 때문에 위험하다.
        UUID uuid = UUID.randomUUID();
        String originalFilename = file.getOriginalFilename();
        String uuidFilename = uuid + "_" + originalFilename;
        try {
            // 파일 사이즈 줄이기
            Path filePath = Paths.get(uploadFolder + uuidFilename);
            Files.write(filePath, file.getBytes());
        } catch (Exception e) {
            throw new Exception500("파일 업로드 실패 : "+e.getMessage());
        }
        return uuidFilename;
    }
}
