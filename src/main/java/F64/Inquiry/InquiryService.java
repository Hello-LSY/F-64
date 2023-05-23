package F64.Inquiry;

import F64.User.CustomUser;
import F64.User.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private UserSecurityService userSecurityService;


    public void writeInquiry(Inquiry inquiry){

        inquiry.setCreatedDate(LocalDateTime.now());
        CustomUser user = userSecurityService.getCurrentUser();
        inquiry.setNickname(user.getNickname());
        inquiryRepository.save(inquiry);
    }

    public void saveAnswer(Long id, String answer) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 문의글이 없습니다."));
        inquiry.setAnswer(answer);
        inquiryRepository.save(inquiry);
    }

}
