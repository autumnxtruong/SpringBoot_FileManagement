package com.example.uploadMultipleFile_demo;

import com.example.uploadMultipleFile_demo.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

//We need to run init() method of FilesStorageService (and also deleteAll() if necessary).
//So open SpringBootUploadMultipleFilesApplication.java and implement CommandLineRunner for run() method
@SpringBootApplication
public class UploadMultipleFileDemoApplication implements CommandLineRunner {

	@Resource
	FileStorageService storageService;

	public static void main(String[] args) {

		SpringApplication.run(UploadMultipleFileDemoApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}
}
