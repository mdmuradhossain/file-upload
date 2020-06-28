package com.murad.file.upload.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murad.file.upload.model.FileDto;
import com.murad.file.upload.repository.FileRepository;

@Service
public class FileService {

	@Autowired
	private FileRepository fileRepository;

	public FileDto saveFile(FileDto fileDto) {
		return fileRepository.save(fileDto);
	}
	
	public List<FileDto> getFiles(){
		return fileRepository.findAll();
	}
}
