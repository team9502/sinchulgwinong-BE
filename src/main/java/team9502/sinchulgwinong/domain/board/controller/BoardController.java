package team9502.sinchulgwinong.domain.board.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9502.sinchulgwinong.domain.board.dto.request.BoardRequestDTO;
import team9502.sinchulgwinong.domain.board.dto.response.BoardResponseDTO;
import team9502.sinchulgwinong.domain.board.service.BoardService;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;

import java.util.List;

import static team9502.sinchulgwinong.global.response.SuccessCode.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 CRUD API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/{userId}")
    public ResponseEntity<GlobalApiResponse<Object>> boardCreate(
            BoardRequestDTO boardRequestDTO,
            @PathVariable Long userId) {

        BoardResponseDTO boardResponseDTO = boardService.boardCreate(userId, boardRequestDTO);

        return ResponseEntity.status(SUCCESS_CREATE_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_CREATE_BOARD.getMessage(),
                                boardResponseDTO
                        )
                );
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse<Object>> getAllBoard() {

        List<BoardResponseDTO> boardResponseDTOS = boardService.getAllBoard();

        return ResponseEntity.status(SUCCESS_READ_ALL_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_ALL_BOARD.getMessage(),
                                boardResponseDTOS
                        )
                );
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<GlobalApiResponse<Object>> getBoardById(@PathVariable Long boardId) {

        BoardResponseDTO boardResponseDTO = boardService.getBoardById(boardId);

        return ResponseEntity.status(SUCCESS_READ_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_READ_BOARD.getMessage(),
                                boardResponseDTO
                        )
                );
    }

    @GetMapping("/{boardId}/user/{userId}")
    public ResponseEntity<GlobalApiResponse<Object>> boardUpdate(
            @PathVariable Long boardId,
            @PathVariable Long userId,
            BoardRequestDTO boardRequestDTO) {

        BoardResponseDTO boardResponseDTO = boardService.boardUpdate(boardId, userId, boardRequestDTO);

        return ResponseEntity.status(SUCCESS_UPDATE_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_UPDATE_BOARD.getMessage(),
                                boardResponseDTO
                        )
                );
    }

    @DeleteMapping("/{boardId}/user/{userId}")
    public ResponseEntity<GlobalApiResponse<Object>> deleteBoard(
            @PathVariable Long boardId,
            @PathVariable Long userId) {

        boardService.boardDelete(boardId, userId);

        return ResponseEntity.status(SUCCESS_DELETE_BOARD.getHttpStatus())
                .body(
                        GlobalApiResponse.of(
                                SUCCESS_DELETE_BOARD.getMessage(),
                                null
                        )
                );
    }

}
