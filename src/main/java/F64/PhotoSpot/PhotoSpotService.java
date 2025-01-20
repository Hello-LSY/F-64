package F64.PhotoSpot;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoSpotService {

    private final PhotoSpotRepository photoSpotRepository;

    public PhotoSpotService(PhotoSpotRepository photoSpotRepository) {
        this.photoSpotRepository = photoSpotRepository;
    }

    public List<PhotoSpot> getPhotoSpotList() {
        return photoSpotRepository.findAllByOrderByDateDesc();
    }

    public PhotoSpot savePhotoSpot(PhotoSpot photoSpot) {
        return photoSpotRepository.save(photoSpot);
    }

    public PhotoSpot getPhotoSpotById(Long id) {
        return photoSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PhotoSpot not found. ID=" + id));
    }

    public void deletePhotoSpot(Long id) {
        photoSpotRepository.deleteById(id);
    }
}
