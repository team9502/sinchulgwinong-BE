package team9502.sinchulgwinong.domain.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team9502.sinchulgwinong.domain.auth.dto.request.BusinessStatusRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.BusinessVerificationRequestDTO;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class BusinessInfoService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private static final String BASE_URL = "https://api.odcloud.kr/api/nts-businessman/v1";

    public String verifyBusinessInfo(BusinessVerificationRequestDTO request) throws URISyntaxException {

        URI uri = new URI(BASE_URL + "/validate?serviceKey=" + serviceKey + "&returnType=JSON");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BusinessVerificationRequestDTO> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject(uri, entity, String.class);
    }


    public String checkBusinessStatus(BusinessStatusRequestDTO request) throws URISyntaxException {

        URI uri = new URI(BASE_URL + "/status?serviceKey=" + serviceKey + "&returnType=JSON");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BusinessStatusRequestDTO> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(uri, entity, String.class);
    }
}