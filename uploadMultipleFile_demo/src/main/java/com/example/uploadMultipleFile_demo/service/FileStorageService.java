package com.example.uploadMultipleFile_demo.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

//First we need an interface that will be autowired in the Controller.
//These methods will be defined in FileStorageServiceImpl and used in Controller
public interface FileStorageService {
    public void init();
    public void save(MultipartFile file);
    public Resource load(String filename);
    public void deleteAll();
    public Stream<Path> loadAll();
}
