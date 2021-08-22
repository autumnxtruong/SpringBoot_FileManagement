package com.example.uploadfile_demo.controller;


import com.example.uploadfile_demo.message.ResponseFile;
import com.example.uploadfile_demo.message.ResponseMessage;
import com.example.uploadfile_demo.model.FileDB;
import com.example.uploadfile_demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:8081") //@CrossOrigin is for configuring allowed origins.
public class FileController {

    //We use @Autowired to inject implementation of FileStorageService bean to local variable.
    @Autowired
    FileStorageService storageService;

//    POST /upload: uploadFile()
//    GET /files: getListFiles()
//    GET /files/[id]: getFile()

    @GetMapping("/uploadform")
    public String uploadForm(){

        return "uploadform";
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("files")MultipartFile[] files) {
        String message = "";
        try {

            List<String>fileNames=new ArrayList<>();
            Arrays.asList(files).stream().forEach(file -> {
                try {
                    storageService.store(file);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            message = "Uploaded successfully!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file! ";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }


    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }









}
