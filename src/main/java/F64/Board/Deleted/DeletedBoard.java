package F64.Board.Deleted;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class DeletedBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long BoardId;
    private String title;
    private String content;
    private String writerNickname;
    private String writerUsername;

    private LocalDate createdDate;
    private int viewCount;
    private int likeCount;

}
