package F64.Board.Deleted;

import F64.Board.Board;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class DeletedBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

    private String title;
    private String content;
    private String writerNickname;
    private String writerUsername;
    private LocalDateTime createdDate;
    private int viewCount;
    private int likeCount;
    private String filename;

}
