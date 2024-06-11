package team9502.sinchulgwinong.domain.aws;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class awsTest {

    @GetMapping("/aws")
    public ResponseEntity<Object> awstest() {

        return ResponseEntity.ok().body(null);
    }

}
