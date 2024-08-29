package org.suhodo.sb01.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.suhodo.sb01.dto.UploadFileDTO;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@Log4j2

public class UpDownController {

    @Value("${org.suhodo.upload.path}")
    private String uploadPath;

    @ApiOperation(value = "Upload POST", notes="POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public String upload(UploadFileDTO uploadFileDTO){
        log.info(uploadFileDTO);

        if (uploadFileDTO.getFiles() != null) {
            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalFilename = multipartFile.getOriginalFilename();
                log.info(originalFilename);

                String uuid =UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid + "_" + originalFilename);

                try { multipartFile.transferTo(savePath.toFile()); } catch (Exception e) { e.printStackTrace(); }
            });

        }

        return null;
    }

}
