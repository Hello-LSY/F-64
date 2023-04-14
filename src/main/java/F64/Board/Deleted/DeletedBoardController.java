package F64.Board.Deleted;

import F64.Board.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeletedBoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/board/delete/{id}")
    public ModelAndView BoardDelete(@PathVariable Long id)
    {
        boardService.deleteBoard(id);
        ModelAndView mav = new ModelAndView();
        mav.addObject("message","삭제되었습니다.");
        mav.setViewName("alert");
        mav.addObject("redirectPath", "/board/list");
        return mav;
    }
}
