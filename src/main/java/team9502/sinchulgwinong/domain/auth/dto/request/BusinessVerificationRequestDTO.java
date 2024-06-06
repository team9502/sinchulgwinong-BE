package team9502.sinchulgwinong.domain.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BusinessVerificationRequestDTO {

    private List<BusinessInfo> businesses;

    @Setter
    @Getter
    public static class BusinessInfo {
        private String b_no;
        private String start_dt;
        private String p_nm;
        private String p_nm2;
        private String b_nm;
        private String corp_no;
        private String b_sector;
        private String b_type;
        private String b_adr;

    }
}