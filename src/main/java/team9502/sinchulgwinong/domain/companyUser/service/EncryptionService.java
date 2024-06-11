package team9502.sinchulgwinong.domain.companyUser.service;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final BasicTextEncryptor textEncryptor;

    public EncryptionService(@Value("${encryption.secretKey}") String secretKey) {
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(secretKey);
    }

    public String encryptCpNum(String cpNum) {
        return textEncryptor.encrypt(cpNum);
    }

    public String decryptCpNum(String encryptedCpNum) {
        return textEncryptor.decrypt(encryptedCpNum);
    }
}
