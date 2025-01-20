package F64.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    @Value("${upload.path}")  // 프로퍼티에서 경로를 읽어옴
    private String filePath;

    @Value("${upload.urlPath}")  // URL 접근 경로
    private String urlPath;

    public void saveFile(Board board, MultipartFile file) {
        if (file.isEmpty()) {
            log.warn("파일이 비어 있습니다. 업로드를 건너뜁니다.");
            return;
        }

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File directory = new File(filePath);

            // 디렉토리가 존재하지 않으면 생성
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("파일 저장 디렉토리 생성 실패: " + directory.getAbsolutePath());
                }
            }

            File saveFile = new File(directory, fileName);
            file.transferTo(saveFile);

            // DB에 저장할 경로 설정 (웹에서 접근 가능하게 설정)
            board.setFilename(fileName);
            board.setFilepath(urlPath + fileName);

            log.info("파일 저장 성공: {}", saveFile.getAbsolutePath());

        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: ", e);
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }
}
