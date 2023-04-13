package F64.Board;


import F64.User.Member;
import F64.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/board/list")
    public String BoardForm(Model model){
        List<Board> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);
        return "listForm";
    }

    @GetMapping("/board/write")
    public String BoardWrite(Model model){
        model.addAttribute("board", new Board());
        return "writeForm";
    }

    @PostMapping("/board/writePro")
    public String BoardWritePro(Board board) {

        boardService.writeBoard(board);
        return "redirect:/board/list";
    }

    @GetMapping("/board/update/{id}")
    public String BoardUpdate(@PathVariable Long id, Model model){
        //id에 해당하는 게시물 가져오기
        Board board = boardService.getBoardById(id);
        //board 속성 넘겨줌
        model.addAttribute("board", board);
        return "updateForm";
    }

    @PostMapping("/board/updatePro/{id}")
    public String BoardUpdatePro(@PathVariable Long id,  @ModelAttribute Board board){
        boardService.updateBoard(id, board);
        return "redirect:/board/list";
    }

    @GetMapping("/board/view/{id}")
    public String BoardView(@PathVariable Long id, Model model, Authentication authentication){
        Board board = boardService.getBoardAndIncreaseViewCount(id);
        model.addAttribute("board", board);

        boolean isWriter = false;
        if(authentication != null){
            String username = authentication.getName();
            if(username.equals(board.getWriterUsername())){
                isWriter = true;
            }
        }
        model.addAttribute("isWriter",isWriter);

        return "boardView";
    }


    @PostMapping("/board/like/{id}")
    public ModelAndView likeBoard(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<Member> optionalMember = userRepository.findByusername(currentUsername);
        if (!optionalMember.isPresent()) {
            throw new IllegalStateException("사용자 정보를 찾을 수 없습니다.");
        }
        Member member = optionalMember.get();

        ModelAndView mav = new ModelAndView();

        boolean isLiked = boardService.likeBoard(id, member);
        if (!isLiked) {
            // 중복 추천 시 예외 처리
            mav.addObject("message", "이미 추천한 게시물입니다.");
            mav.setViewName("alert");
            return mav;
        }

        mav.setViewName("redirect:/board/view/" + id);
        return mav;
    }

}
