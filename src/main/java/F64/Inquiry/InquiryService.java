package F64.Inquiry;

import F64.User.CustomUser;
import F64.User.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserSecurityService userSecurityService;

    @Transactional
    public void writeInquiry(Inquiry inquiry) {
        CustomUser user = userSecurityService.getCurrentUser();
        if (user == null) throw new IllegalStateException("로그인이 필요합니다.");

        inquiry.setCreatedDate(LocalDateTime.now());
        inquiry.setNickname(user.getNickname());
        inquiryRepository.save(inquiry);
    }

    @Transactional
    public void saveAnswer(Long id, String answer) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의글이 없습니다."));
        inquiry.setAnswer(answer);
        inquiryRepository.save(inquiry);
    }

    public List<Inquiry> getInquiryList() {
        return inquiryRepository.findAllByOrderByCreatedDateDesc();
    }

    @Transactional
    public void deleteInquiry(Long id) {
        if (!inquiryRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 문의글이 존재하지 않습니다.");
        }
        inquiryRepository.deleteById(id);
    }
}
