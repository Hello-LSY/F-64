package F64.Board.Deleted;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeletedBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "board_id")
    private Long boardId; // @OneToOne 직접참조 이슈 발생 해결 : Board 객체 대신 boardId만 저장

    private String title;
    private String content;
    private String writerNickname;
    private String writerUsername;
    private LocalDateTime createdDate;
    private int viewCount;
    private int likeCount;
    private String filename;
}
