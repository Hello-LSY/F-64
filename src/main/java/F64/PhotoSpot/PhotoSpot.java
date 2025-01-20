package F64.PhotoSpot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PhotoSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String userNickname;
    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "photo_spot_path", joinColumns = @JoinColumn(name = "photo_spot_id"))
    private List<LatLng> pathList;

    /**
     * Thymeleaf 템플릿에서 JSON 형태로 사용하기 위한 Getter
     */
    @Transient
    public String getPathListJson() {
        try {
            return new ObjectMapper().writeValueAsString(this.pathList);
        } catch (JsonProcessingException e) {
            return "[]"; // 파싱 오류 시 빈 배열
        }
    }
}
