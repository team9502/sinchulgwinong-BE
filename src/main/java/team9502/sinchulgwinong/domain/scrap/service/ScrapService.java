package team9502.sinchulgwinong.domain.scrap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.board.dto.response.BoardListResponseDTO;
import team9502.sinchulgwinong.domain.board.dto.response.BoardResponseDTO;
import team9502.sinchulgwinong.domain.board.entity.Board;
import team9502.sinchulgwinong.domain.board.repository.BoardRepository;
import team9502.sinchulgwinong.domain.companyUser.dto.response.CpUserProfileResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardListResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.dto.response.JobBoardResponseDTO;
import team9502.sinchulgwinong.domain.jobBoard.entity.JobBoard;
import team9502.sinchulgwinong.domain.jobBoard.repository.JobBoardRepository;
import team9502.sinchulgwinong.domain.scrap.dto.response.CpUserScrapListResponseDTO;
import team9502.sinchulgwinong.domain.scrap.entity.BoardScrap;
import team9502.sinchulgwinong.domain.scrap.entity.CpUserScrap;
import team9502.sinchulgwinong.domain.scrap.entity.JobScrap;
import team9502.sinchulgwinong.domain.scrap.repository.BoardScrapsRepository;
import team9502.sinchulgwinong.domain.scrap.repository.CpUserScrapRepository;
import team9502.sinchulgwinong.domain.scrap.repository.JobScrapRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final BoardScrapsRepository boardScrapsRepository;
    private final BoardRepository boardRepository;
    private final JobBoardRepository jobBoardRepository;
    private final JobScrapRepository jobScrapRepository;
    private final CompanyUserRepository companyUserRepository;
    private final CpUserScrapRepository cpUserScrapRepository;

    @Transactional
    public boolean scrapCreateBoard(Long boardId, User user) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if (boardScrapsRepository.existsByUser_UserIdAndBoard_BoardId(user.getUserId(), boardId)) {

            BoardScrap boardScrap = boardScrapsRepository.findByUser_UserIdAndBoard_BoardId(user.getUserId(), boardId);

            boardScrapsRepository.delete(boardScrap);
            return false;
        } else {

            BoardScrap boardScrap = new BoardScrap();

            boardScrap.setBoard(board);
            boardScrap.setUser(user);

            boardScrapsRepository.save(boardScrap);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public BoardListResponseDTO getAllScraps(User user, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<BoardScrap> boardScrapPage = boardScrapsRepository.findByUser_UserId(user.getUserId(), pageable);

        List<BoardResponseDTO> boardResponseDTOS = boardScrapPage.stream()
                .map(boardScrap -> {
                    Board board = boardScrap.getBoard();
                    return new BoardResponseDTO(board);
                }).toList();

        return new BoardListResponseDTO(
                boardResponseDTOS,
                boardScrapPage.getTotalElements(),
                boardScrapPage.getNumber(),
                boardScrapPage.getTotalPages(),
                boardScrapPage.getSize());

    }

    @Transactional
    public boolean scrapCreateJobBoard(Long jobBoardId, User user) {

        JobBoard jobBoard = jobBoardRepository.findById(jobBoardId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOARD_NOT_FOUND));

        if (jobScrapRepository.existsByJobBoard_JobBoardIdAndUser_UserId(jobBoardId, user.getUserId())) {

            JobScrap jobScrap = jobScrapRepository.findByJobBoard_JobBoardIdAndUser_UserId(jobBoardId, user.getUserId());

            jobScrapRepository.delete(jobScrap);

            return false;

        } else {

            JobScrap jobScrap = new JobScrap();

            jobScrap.setJobBoard(jobBoard);
            jobScrap.setUser(user);

            jobScrapRepository.save(jobScrap);

            return true;
        }
    }

    @Transactional(readOnly = true)
    public JobBoardListResponseDTO getAllJobBoards(User user, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<JobScrap> jobScrapPage = jobScrapRepository.findByUser_UserId(user.getUserId(), pageable);

        List<JobBoardResponseDTO> jobBoardResponseDTOS = jobScrapPage.stream()
                .map(jobScrap -> {
                    JobBoard jobBoard = jobScrap.getJobBoard();
                    return new JobBoardResponseDTO(jobBoard);
                }).toList();

        return new JobBoardListResponseDTO(
                jobBoardResponseDTOS,
                jobScrapPage.getTotalElements(),
                jobScrapPage.getNumber(),
                jobScrapPage.getTotalPages(),
                jobScrapPage.getSize()
        );
    }


    @Transactional
    public boolean cpUserScrapCreate(User user, Long cpUserId) {

        CompanyUser companyUser = companyUserRepository.findById(cpUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));

        if (cpUserScrapRepository.existsByCompanyUser_CpUserIdAndUser_UserId(cpUserId, user.getUserId())) {

            CpUserScrap cpUserScrap = cpUserScrapRepository.findByCompanyUser_CpUserIdAAndUser_UserId(cpUserId, user.getUserId());

            cpUserScrapRepository.delete(cpUserScrap);

            return false;

        } else {

            CpUserScrap cpUserScrap = new CpUserScrap();

            cpUserScrap.setCompanyUser(companyUser);
            cpUserScrap.setUser(user);

            cpUserScrapRepository.save(cpUserScrap);

            return true;
        }
    }

    @Transactional(readOnly = true)
    public CpUserScrapListResponseDTO getAllCpUserScrap(User user, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<CpUserScrap> cpUserScrapPage = cpUserScrapRepository.findByUser_UserId(user.getUserId(), pageable);

        List<CpUserProfileResponseDTO> cpUserProfileResponseDTOS = cpUserScrapPage.stream()
                .map(cpUserScrap -> {
                    CompanyUser companyUser = cpUserScrap.getCompanyUser();
                    return new CpUserProfileResponseDTO(
                            companyUser.getCpUserId(),
                            companyUser.getCpName(),
                            companyUser.getCpEmail(),
                            companyUser.getCpPhoneNumber(),
                            companyUser.getCpUsername(),
                            companyUser.getHiringStatus(),
                            companyUser.getEmployeeCount(),
                            companyUser.getFoundationDate(),
                            companyUser.getDescription(),
                            companyUser.getCpNum(),
                            companyUser.getAverageRating(),
                            companyUser.getReviewCount()
                    );
                }).toList();

        return new CpUserScrapListResponseDTO(
                cpUserProfileResponseDTOS,
                cpUserScrapPage.getTotalElements(),
                cpUserScrapPage.getNumber(),
                cpUserScrapPage.getTotalPages(),
                cpUserScrapPage.getSize()
        );
    }
}
