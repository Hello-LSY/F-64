package F64.Board;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

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

    private LocalDate createdDate;
    private int viewCount;
    private int likeCount;

}