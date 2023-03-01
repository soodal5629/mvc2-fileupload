package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName; // 업로드한 파일명
    private String storeFileName; // 시스템에 저장한(서버 내부에서 관리하는) 파일명(중복 안되도록)

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
