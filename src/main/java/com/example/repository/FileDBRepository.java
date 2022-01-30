package com.example.repository;

import com.example.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {
    @Query("SELECT f FROM FileDB f WHERE f.name='default.png'")
    FileDB getDefaultImage();
}
