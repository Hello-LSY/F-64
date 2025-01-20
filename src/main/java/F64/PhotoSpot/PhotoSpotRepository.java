package F64.PhotoSpot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoSpotRepository extends JpaRepository<PhotoSpot, Long> {
    List<PhotoSpot> findAllByOrderByDateDesc();

}
