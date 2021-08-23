package com.example.uploadfile_demo.controller;


import com.example.uploadfile_demo.exception.FileStorageException;
import com.example.uploadfile_demo.message.ResponseFile;
import com.example.uploadfile_demo.message.ResponseMessage;
import com.example.uploadfile_demo.model.FileDB;
import com.example.uploadfile_demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    

    //ONLY ALLOW JPG,JPEG,PNG
    //backend/controller -> https://stackoverflow.com/questions/45978598/spring-boot-upload-file-allow-only-images
    //frontend ->https://www.youtube.com/watch?v=-Xf_fbKQit8 (11:21)


    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("files")MultipartFile[] files,
            RedirectAttributes redirectAttributes, Model model) {

            List<String>fileNames=new ArrayList<>();
            Arrays.asList(files).stream().forEach(
                    file -> {
                        try {
                        storageService.store(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again.");
                        }});
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded");
        return "redirect:/uploadform";
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
