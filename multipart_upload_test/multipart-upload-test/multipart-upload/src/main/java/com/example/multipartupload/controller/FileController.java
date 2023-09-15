package com.example.multipartupload.controller;

import com.example.multipartupload.domain.FileUpload;
import com.example.multipartupload.service.FileService;
import com.example.multipartupload.service.FileStorageService;
import com.example.multipartupload.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Enumeration;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileService fileService;

    @PostMapping("/upload-file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader HttpHeaders headers) throws IOException {
        if(file.isEmpty())
        {
            throw new NoSuchFileException("해당 파일이 존재하지 않습니다.");
        }
        String fileName = fileStorageService.storeFile(file);
        System.out.println("fileName -->>"+fileName + "\n");
        System.out.println("header-->>" + headers+"\n");
        System.out.println("input type -->> " + file.getContentType() + "\n");
        String fileExt = fileName.replaceAll("^.*\\.(.*)$", "$1");

        String fileOriginalName = StringUtils.cleanPath(file.getOriginalFilename());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/downloadfile/")
                .path(fileName)
                .toUriString();
        FileUpload fileDto = new FileUpload(fileName, fileOriginalName, fileExt, fileDownloadUri);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

}
