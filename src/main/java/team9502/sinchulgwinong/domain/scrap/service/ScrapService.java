package team9502.sinchulgwinong.domain.scrap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.board.repository.BoardRepository;
import team9502.sinchulgwinong.domain.scrap.dto.response.ScrapResponseDTO;
import team9502.sinchulgwinong.domain.scrap.entity.Scrap;
import team9502.sinchulgwinong.domain.scrap.repository.ScrapsRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapsRepository scrapsRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public boolean scrapCreateBoard(Long boardId, User user){

        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if(scrapsRepository.existsByUser_UserIdAndBoard_BoardId(user.getUserId(), boardId)){

            Scrap scrap = scrapsRepository.findByUser_UserIdAndBoard_BoardId(user.getUserId(),boardId);

            scrapsRepository.delete(scrap);
            return false;
        }
        else{

            Scrap scrap = new Scrap();

            scrap.setBoard(board);
            scrap.setUser(user);

            scrapsRepository.save(scrap);
            return true;
        }
    }

    @Transactional
    public List<ScrapResponseDTO> getAllScraps(User user){

        return scrapsRepository.findByUser_UserId(user.getUserId()).stream()
                .map(scrap -> {
                    Board board = scrap.getBoard();
                    return new ScrapResponseDTO(scrap, board);
                })
                .collect(Collectors.toList());
    }

}
