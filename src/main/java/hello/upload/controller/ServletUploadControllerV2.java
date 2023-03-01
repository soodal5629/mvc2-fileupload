package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV2(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);
        String itemName = request.getParameter("itemName");

        log.info("itemName={}", itemName);
        Collection<Part> parts = request.getParts();

        log.info("parts={}", parts);

        for (Part part : parts) {
            log.info("==== PART ====");
            log.info("name={}", part.getName());
            // parts도 헤더와 바디로 구분됨
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {} : {}", headerName, part.getHeader(headerName));
            }
            // 편의 메소드
            // content-disposition; filename
            log.info("submittedFilename={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize()); // part의 body size

            // 데이터 읽기
            InputStream inputStream = part.getInputStream();
            // 문자 <-> 바이너리 데이터 : Charset을 정해줘야 함
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body={}", body);

            // 파일에 저장하기
            if(StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath={}", fullPath);
                part.write(fullPath);
            }
            inputStream.close(); // 얘 없으면 파일 경로에 저장은 되지만 이상한 에러가 남(인프런 질문 보니 나랑 비슷한 경우가 많은듯 해보임)
        }
        return "upload-form";
    }
}
