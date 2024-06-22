package team9502.sinchulgwinong.domain.board.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.board.dto.request.BoardRequestDTO;
import team9502.sinchulgwinong.domain.board.dto.request.BoardUpdateRequestDTO;
import team9502.sinchulgwinong.domain.board.dto.response.BoardListResponseDTO;
import team9502.sinchulgwinong.domain.board.dto.response.BoardResponseDTO;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.board.repository.BoardRepository;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PointService pointService;

    @Transactional
    public BoardResponseDTO boardCreate(User user, BoardRequestDTO boardRequestDTO) {

        validation(boardRequestDTO);

        Board board = new Board();

        board.setUser(user);
        board.setBoardTitle(boardRequestDTO.getBoardTitle());
        board.setBoardContent(boardRequestDTO.getBoardContent());

        boardRepository.save(board);

        pointService.earnPoints(user, SpType.BOARD);

        return new BoardResponseDTO(board);
    }

    @Transactional(readOnly = true)
    public BoardListResponseDTO getAllBoard(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boardPage = boardRepository.findAll(pageable);

        List<BoardResponseDTO> boards = boardPage.stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());

        return new BoardListResponseDTO(
                boards,
                boardPage.getTotalElements(),
                boardPage.getNumber(),
                boardPage.getTotalPages(),
                boardPage.getSize());
    }

    @Transactional(readOnly = true)
    public BoardResponseDTO getBoardById(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        return new BoardResponseDTO(board);
    }

    @Transactional
    public BoardResponseDTO boardUpdate(Long boardId, User user, BoardUpdateRequestDTO boardUpdateRequestDTO) {

        validation(boardUpdateRequestDTO);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if (!board.getUser().getUserId().equals(user.getUserId())) {
            throw new ApiException(ErrorCode.FORBIDDEN_WORK);
        }

        if (boardUpdateRequestDTO.getBoardTitle() != null)
            board.setBoardTitle(boardUpdateRequestDTO.getBoardTitle());
        if (boardUpdateRequestDTO.getBoardContent() != null)
            board.setBoardContent(boardUpdateRequestDTO.getBoardContent());

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

    @Transactional(readOnly = true)
    public BoardListResponseDTO getAllMyBoard(User user, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boardPage = boardRepository.findByUser_UserId(user.getUserId(), pageable);

        List<BoardResponseDTO> boardResponseDTOS = boardPage.stream()
                .map(BoardResponseDTO::new)
                .toList();

        return new BoardListResponseDTO(
                boardResponseDTOS,
                boardPage.getTotalElements(),
                boardPage.getNumber(),
                boardPage.getTotalPages(),
                boardPage.getSize());
    }

    @Transactional(readOnly = true)
    public BoardListResponseDTO getAllFindBoards(String boardTitle, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boardPage = boardRepository.findByBoardTitleContaining(
                boardTitle, pageable);

        List<BoardResponseDTO> boardResponseDTOS = boardPage.stream()
                .map(BoardResponseDTO::new)
                .toList();

        return new BoardListResponseDTO(
                boardResponseDTOS,
                boardPage.getTotalElements(),
                boardPage.getNumber(),
                boardPage.getTotalPages(),
                boardPage.getSize()
        );
    }

    private void validation(BoardRequestDTO boardRequestDTO) {

        if (boardRequestDTO.getBoardTitle().length() > 100) {
            throw new ApiException(ErrorCode.TITLE_TOO_LONG);
        }
        if (boardRequestDTO.getBoardContent().length() > 1000) {
            throw new ApiException(ErrorCode.CONTENT_TOO_LONG);
        }
    }

    private void validation(BoardUpdateRequestDTO boardUpdateRequestDTO) {

        if (boardUpdateRequestDTO.getBoardTitle().length() > 100) {
            throw new ApiException(ErrorCode.TITLE_TOO_LONG);
        }
        if (boardUpdateRequestDTO.getBoardContent().length() > 1000) {
            throw new ApiException(ErrorCode.CONTENT_TOO_LONG);
        }
    }

}
