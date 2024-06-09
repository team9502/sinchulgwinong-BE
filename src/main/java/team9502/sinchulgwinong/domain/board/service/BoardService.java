package team9502.sinchulgwinong.domain.board.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.board.dto.request.BoardRequestDTO;
import team9502.sinchulgwinong.domain.board.dto.response.BoardResponseDTO;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.board.repository.BoardRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDTO boardCreate(User user, BoardRequestDTO boardRequestDTO) {

        validation(boardRequestDTO);

        Board board = new Board();

        board.setUser(user);
        board.setTitle(boardRequestDTO.getTitle());
        board.setContent(boardRequestDTO.getContent());
        board.setCreatedAt(LocalDateTime.now());
        board.setModifiedAt(LocalDateTime.now());

        boardRepository.save(board);

        return new BoardResponseDTO(board);
    }

    public List<BoardResponseDTO> getAllBoard() {

        return boardRepository.findAll().stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDTO getBoardById(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        return new BoardResponseDTO(board);
    }

    @Transactional
    public BoardResponseDTO boardUpdate(Long boardId, User user, BoardRequestDTO boardRequestDTO) {

        validation(boardRequestDTO);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if (!board.getUser().getUserId().equals(user.getUserId())) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        board.setTitle(boardRequestDTO.getTitle());
        board.setContent(boardRequestDTO.getContent());
        board.setModifiedAt(LocalDateTime.now());

        boardRepository.save(board);

        return new BoardResponseDTO(board);
    }

    @Transactional
    public void boardDelete(Long boardId, User user) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if (!board.getUser().getUserId().equals(user.getUserId())) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        boardRepository.delete(board);
    }

    private void validation(BoardRequestDTO boardRequestDTO) {

        if (boardRequestDTO.getTitle().isEmpty()) {
            throw new ApiException(ErrorCode.TITLE_REQUIRED);
        }
        if (boardRequestDTO.getContent().isEmpty()) {
            throw new ApiException(ErrorCode.CONTENT_REQUIRED);
        }
        if (boardRequestDTO.getTitle().length() > 100) {
            throw new ApiException(ErrorCode.TITLE_TOO_LONG);
        }
        if (boardRequestDTO.getContent().length() > 1000) {
            throw new ApiException(ErrorCode.CONTENT_TOO_LONG);
        }
    }

}
