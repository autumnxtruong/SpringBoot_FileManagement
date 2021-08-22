package com.example.uploadfile_demo.repository;

import com.example.uploadfile_demo.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

//JpaRepository has many methods that we can use (implement or override)
//save(FileDB)
//findById(id)
//findAll()
}
