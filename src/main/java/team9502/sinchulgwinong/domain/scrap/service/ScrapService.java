package team9502.sinchulgwinong.domain.scrap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.board.repository.BoardRepository;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.jobBoard.repository.JobBoardRepository;
import team9502.sinchulgwinong.domain.scrap.dto.response.JobScrapResponseDTO;
import team9502.sinchulgwinong.domain.scrap.dto.response.ScrapResponseDTO;
import team9502.sinchulgwinong.domain.scrap.entity.JobScrap;
import team9502.sinchulgwinong.domain.scrap.entity.Scrap;
import team9502.sinchulgwinong.domain.scrap.repository.JobScrapRepository;
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
    private final JobBoardRepository jobBoardRepository;
    private final JobScrapRepository jobScrapRepository;

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

    @Transactional(readOnly = true)
    public List<ScrapResponseDTO> getAllScraps(User user){

        return scrapsRepository.findByUser_UserId(user.getUserId()).stream()
                .map(scrap -> {
                    Board board = scrap.getBoard();
                    return new ScrapResponseDTO(scrap, board);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean scrapCreateJobBoard(Long jobBoardId, User user){

        JobBoard jobBoard = jobBoardRepository.findById(jobBoardId)
                .orElseThrow(()-> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if(jobScrapRepository.existsByJobBoard_JobBoardIdAndUser_UserId(jobBoardId, user.getUserId())){

            JobScrap jobScrap = jobScrapRepository.findByJobBoard_JobBoardIdAndUser_UserId(jobBoardId, user.getUserId());

            jobScrapRepository.delete(jobScrap);

            return false;

        }
        else{

            JobScrap jobScrap = new JobScrap();

            jobScrap.setJobBoard(jobBoard);
            jobScrap.setUser(user);

            jobScrapRepository.save(jobScrap);

            return true;
        }
    }

    @Transactional(readOnly = true)
    public List<JobScrapResponseDTO> getAllJobBoards(User user){

        return jobScrapRepository.findByUser_UserId(user.getUserId()).stream()
                .map(jobScrap ->{
                    JobBoard jobBoard = jobScrap.getJobBoard();
                    return  new JobScrapResponseDTO(jobScrap,jobBoard);
                })
                .collect(Collectors.toList());
        }

    }
