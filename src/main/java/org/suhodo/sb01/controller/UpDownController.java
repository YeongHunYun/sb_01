package org.suhodo.sb01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.suhodo.sb01.dto.upload.UploadFileDTO;
import org.suhodo.sb01.dto.upload.UploadResultDTO;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2

public class UpDownController {

    @Value("${org.suhodo.upload.path}")
    private String uploadPath;

    @ApiOperation(value = "Upload POST", notes="POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){
        log.info(uploadFileDTO);

        if (uploadFileDTO.getFiles() != null) {

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalFilename = multipartFile.getOriginalFilename();
                log.info(originalFilename);

                String uuid =UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid + "_" + originalFilename);

                boolean image = false;

                try {
                    multipartFile.transferTo(savePath);
                    if(Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbFile = new File(uploadPath, "s_"+ uuid + "_" + originalFilename);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalFilename)
                        .img(image).build()
                );

            }); // end each

                return list;

        } // end if

        return null;
    }

    // 클라이언트에서 이미지를 조회/나타내고 싶을 때
    @ApiOperation(value="view 파일", notes = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName){
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));

        } catch(Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @ApiOperation(value="remove 파일", notes="DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fimeName}")
    public Map<String, Boolean> removeFile(@PathVariable String fimeName){
        Resource resource = new FileSystemResource(uploadPath + File.separator + fimeName);
        String resourceName = resource.getFilename();
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if(contentType.startsWith("image")) {
                File thumbnailFile = new File(uploadPath + File.separator + "s_"+ fimeName);
                thumbnailFile.delete();
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        resultMap.put("result", removed);
        return resultMap;
    }

}
