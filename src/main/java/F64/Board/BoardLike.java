package F64.Board;

import F64.User.Member;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "board_id")
    private Board board;


}
