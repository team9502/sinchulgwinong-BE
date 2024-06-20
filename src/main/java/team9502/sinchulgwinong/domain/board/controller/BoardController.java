package team9502.sinchulgwinong.domain.board.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.board.dto.request.BoardRequestDTO;
import team9502.sinchulgwinong.domain.board.dto.request.BoardUpdateRequestDTO;
import team9502.sinchulgwinong.domain.board.dto.response.BoardListResponseDTO;
import team9502.sinchulgwinong.domain.board.dto.response.BoardResponseDTO;
import team9502.sinchulgwinong.domain.board.service.BoardService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequestMapping("/boards")
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 CRUD API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<GlobalApiResponse<BoardResponseDTO>> boardCreate(
            @RequestBody @Valid BoardRequestDTO boardRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = (User) userDetails.getUser();

        BoardResponseDTO boardResponseDTO = boardService.boardCreate(user, boardRequestDTO);

        return ResponseEntity.status(SUCCESS_CREATE_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CREATE_BOARD.getMessage(),
                                boardResponseDTO
                        )
                );
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse<BoardListResponseDTO>> getAllBoard(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        BoardListResponseDTO boardListResponseDTO = boardService.getAllBoard(page, size);

        return ResponseEntity.status(SUCCESS_READ_ALL_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_BOARD.getMessage(),
                                boardListResponseDTO
                        )
                );
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<GlobalApiResponse<BoardResponseDTO>> getBoardById(
            @PathVariable("boardId") Long boardId) {

        BoardResponseDTO boardResponseDTO = boardService.getBoardById(boardId);

        return ResponseEntity.status(SUCCESS_READ_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_BOARD.getMessage(),
                                boardResponseDTO
                        )
                );
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<GlobalApiResponse<BoardResponseDTO>> boardUpdate(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BoardUpdateRequestDTO boardUpdateRequestDTO) {

        User user = (User) userDetails.getUser();

        BoardResponseDTO boardResponseDTO = boardService.boardUpdate(boardId, user, boardUpdateRequestDTO);

        return ResponseEntity.status(SUCCESS_UPDATE_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_UPDATE_BOARD.getMessage(),
                                boardResponseDTO
                        )
                );
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<GlobalApiResponse<Object>> deleteBoard(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = (User) userDetails.getUser();

        boardService.boardDelete(boardId, user);

        return ResponseEntity.status(SUCCESS_DELETE_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_DELETE_BOARD.getMessage(),
                                null
                        )
                );
    }

    @GetMapping("/my-boards")
    public ResponseEntity<GlobalApiResponse<BoardListResponseDTO>> getAllMyBoards(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        User user = (User) userDetails.getUser();

        BoardListResponseDTO boardListResponseDTO = boardService.getAllMyBoard(user, page, size);

        return ResponseEntity.status(SUCCESS_READ_ALL_MY_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_MY_BOARD.getMessage(),
                                boardListResponseDTO
                        )
                );
    }

}
