package F64.Board;


import F64.User.Member;
import F64.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/list")
    public String BoardForm(Model model){
        List<Board> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);
        return "listForm";
    }

    @GetMapping("/write")
    public String BoardWrite(Model model){
        model.addAttribute("board", new Board());
        return "writeForm";
    }

    @PostMapping("/writePro")
    public String BoardWritePro(Board board) {

        boardService.write(board);
        return "redirect:/board/list";
    }



    @GetMapping("view/{id}")
    public String BoardView(@PathVariable Long id, Model model){
        Board board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "boardView";
    }

    @PostMapping("/like/{id}")
    public String likeBoard(@PathVariable("id") Long id) {
        // 현재 로그인한 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 사용자가 존재하는지 확인하고 존재한다면 Member 객체 생성
        Optional<Member> optionalMember = userRepository.findByusername(currentUsername);
        if (!optionalMember.isPresent()) {
            throw new IllegalStateException("사용자 정보를 찾을 수 없습니다.");
        }
        Member member = optionalMember.get();

        // 해당 id에 해당하는 게시글에 대해 추천 기능 실행
        boardService.likeBoard(id, member);

        return "redirect:/board/view/" + id;
    }


}
