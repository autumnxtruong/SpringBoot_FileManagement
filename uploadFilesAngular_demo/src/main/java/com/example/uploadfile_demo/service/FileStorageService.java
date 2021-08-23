package com.example.uploadfile_demo.service;


import com.example.uploadfile_demo.model.FileDB;
import com.example.uploadfile_demo.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    //will use FileDBRepository to provide the methods
    // (1) store(file): receives MultipartFile object, transform to FileDB object and save it to Database
    // (2) getFile(id): returns a FileDB object by provided Id
    // (3) getAllFiles(): returns all stored files as list of code>FileDB objects
    @Autowired
    FileDBRepository fileDBRepository;

    // (1) store(file): receives MultipartFile object, transform to FileDB object and save it to Database
    public FileDB store(MultipartFile file) throws IOException {
        //clean up the file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //create an object of FileDB to put into DB
        FileDB fileDB = new FileDB(fileName, file.getContentType(),file.getBytes());
        //WOW use save method from fileDBRepo
        return fileDBRepository.save(fileDB);
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

}
