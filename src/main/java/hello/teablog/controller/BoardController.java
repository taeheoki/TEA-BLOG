package hello.teablog.controller;

import hello.teablog.core.auth.MyUserDetails;
import hello.teablog.dto.board.BoardRequest;
import hello.teablog.model.board.Board;
import hello.teablog.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // RestAPI 주소 설계 규칙에서 자원에는 복수를 붙인다. boards 정석!!
    @GetMapping({"/", "/board"})
    public String main(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            Model model
    ) {
        Page<Board> boardPG = boardService.글목록보기(page, keyword); // OSIV가 꺼져 있어서 db랑 상관없는 데이터가 되기 때문에
        model.addAttribute("boardPG", boardPG);
        return "board/main";
    }

//    public String main(
//            @PageableDefault 애노테이션으로 파라미터로 받을 수 있다.
//    ) {
//        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("id").descending());
//        boardService.글목록보기();
//        return "board/main";
//    }

    @GetMapping("/s/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @PostMapping("/s/board/save")
    public String save(BoardRequest.SaveInDTO saveInDTO, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        boardService.글쓰기(saveInDTO, myUserDetails.getUser().getId());
        return "redirect:/";
    }

    @GetMapping( "/board/{id}")
    public String detail(@PathVariable Long id, Model model){
        Board board = boardService.게시글상세보기(id);
        model.addAttribute("board", board);
        return "board/detail";
    }
}
