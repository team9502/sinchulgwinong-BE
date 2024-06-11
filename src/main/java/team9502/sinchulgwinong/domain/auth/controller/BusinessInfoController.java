package team9502.sinchulgwinong.domain.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team9502.sinchulgwinong.domain.auth.dto.request.BusinessStatusRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.BusinessVerificationRequestDTO;
import team9502.sinchulgwinong.domain.auth.service.BusinessInfoService;

import java.net.URISyntaxException;

@RestController
@AllArgsConstructor
@RequestMapping("/business")
@Tag(name = "BusinessInfo", description = "사업자 번호 관련 API")
public class BusinessInfoController {

    private final BusinessInfoService businessInfoService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyBusiness(
            @RequestBody BusinessVerificationRequestDTO request) throws URISyntaxException {

        String result = businessInfoService.verifyBusinessInfo(request);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/status")
    public ResponseEntity<?> checkStatus(
            @RequestBody BusinessStatusRequestDTO request) throws URISyntaxException {

        String result = businessInfoService.checkBusinessStatus(request);

        return ResponseEntity.ok(result);
    }
}
