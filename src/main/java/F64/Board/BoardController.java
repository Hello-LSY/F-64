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
    BoardService boardService;
    UserRepository userRepository;
    @GetMapping("/list")
    public String BoardForm(Model model){
        List<Board> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String un = authentication.getName();
//        String message = "안녕하세요! ";
//        if (authentication.getPrincipal() instanceof Member) {
//            Member mem = (Member) authentication.getPrincipal();
//            message += mem.getNickname() + "님!";
//        }
//        else {
//            message += un + "님!";
//        }
//        System.out.println(message);




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

}
