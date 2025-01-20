package F64.Board;

import F64.Board.Comment.Comment;
import F64.Board.Like.BoardLike;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "board", indexes = {
        @Index(name = "idx_board_created_date", columnList = "createdDate DESC")
})
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardLike> likes;


}
