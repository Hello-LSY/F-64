package F64.Board;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String writerNickname;
    private String writerUsername;
    private String filename;
    private String filepath;

    private LocalDateTime createdDate;
    private int viewCount;
    private int likeCount;

}