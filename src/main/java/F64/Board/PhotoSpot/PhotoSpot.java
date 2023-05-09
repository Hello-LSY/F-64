package F64.Board.PhotoSpot;

import F64.User.Member;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class PhotoSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String userNickname;
    private LocalDate date;

    private double latitude_1;
    private double longitude_1;

    private double latitude_2;
    private double longitude_2;

    private double latitude_3;
    private double longitude_3;

    private double latitude_4;
    private double longitude_4;

    private double latitude_5;
    private double longitude_5;

}
