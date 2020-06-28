package com.murad.file.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.murad.file.upload.model.FileDto;

public interface FileRepository extends JpaRepository<FileDto, Integer> {

}
