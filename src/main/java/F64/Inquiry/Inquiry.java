package F64.Inquiry;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String nickname;
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private boolean anonymous;
    private boolean secret;

    private String answer;
}
