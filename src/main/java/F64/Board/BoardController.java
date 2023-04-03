package F64.Board;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

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

}
