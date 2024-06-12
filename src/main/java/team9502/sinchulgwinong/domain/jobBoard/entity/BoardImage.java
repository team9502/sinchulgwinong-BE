package team9502.sinchulgwinong.domain.jobBoard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class BoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_board_id")
    private JobBoard jobBoard;

    private String originName; // 이미지 파일의 본래 이름

    private String storedName; // 이미지 파일이 S3에 저장될때 사용되는 이름

    @Setter
    private String accessUrl; // S3 내부 이미지에 접근할 수 있는 URL

    public BoardImage(String originName) {
        this.originName = originName;
        this.storedName = getFileName(originName);
        this.accessUrl = "";
    }

    // 이미지 파일의 확장자를 추출하는 메소드
    public String extractExtension(String originName) {
        int index = originName.lastIndexOf('.');
        if (index == -1) {
            return ""; // 확장자가 없으면 빈 문자열 반환
        }
        return originName.substring(index + 1);
    }

    // 이미지 파일의 이름을 저장하기 위한 이름으로 변환하는 메소드
    public String getFileName(String originName) {
        return UUID.randomUUID() + "." + extractExtension(originName);
    }
}