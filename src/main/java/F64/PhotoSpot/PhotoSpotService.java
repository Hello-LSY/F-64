package F64.PhotoSpot;

import F64.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PhotoSpotService {

    @Autowired
    private PhotoSpotRepository photoSpotRepository;

    @Autowired
    private UserSecurityService userSecurityService;

    public void savePhotoSpot(PhotoSpot photoSpot){

        CustomUser user = userSecurityService.getCurrentUser();
        photoSpot.setUserNickname(user.getNickname());
        photoSpotRepository.save(photoSpot);
    }

    public PhotoSpot getPhotoSpotById(Long id) {
        Optional<PhotoSpot> optionalPhotoSpot = photoSpotRepository.findById(id);
        if (optionalPhotoSpot.isPresent()) {
            return optionalPhotoSpot.get();
        } else {
            throw new NoSuchElementException("PhotoSpot with id " + id + " does not exist.");
        }
    }

    public List<PhotoSpot> getPhotoSpotList() {
        return photoSpotRepository.findAll();
    }
}
