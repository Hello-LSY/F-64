package F64.Board.Like;

import F64.Board.Board;
import F64.Board.BoardRepository;
import F64.User.Member;
import F64.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardLikeController {

    private final UserRepository userRepository;
    private final BoardLikeService boardLikeService;
    private final BoardRepository boardRepository;

    @PostMapping("/like/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> likeBoard(@PathVariable("id") Long boardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();

        // 비로그인 사용자가 요청한 경우
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("비로그인 사용자가 추천 시도");
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response); // 401 Unauthorized
        }

        String currentUsername = authentication.getName();
        Optional<Member> optionalMember = userRepository.findByUsername(currentUsername);

        if (!optionalMember.isPresent()) {
            log.error("사용자 정보 조회 실패: {}", currentUsername);
            response.put("success", false);
            response.put("message", "사용자 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(403).body(response); // 403 Forbidden
        }

        Member member = optionalMember.get();
        boolean isLiked = boardLikeService.likeBoard(boardId, member);
        Board board = boardRepository.findById(boardId).orElseThrow();

        if (!isLiked) {
            log.warn("중복 추천 방지 - 사용자: {}", member.getUsername());
            response.put("success", false);
            response.put("message", "이미 추천한 게시물입니다.");
        } else {
            response.put("success", true);
            response.put("message", "추천이 완료되었습니다.");
        }

        response.put("likeCount", board.getLikeCount()); // 최신 추천 수 반환
        return ResponseEntity.ok(response);
    }
}
