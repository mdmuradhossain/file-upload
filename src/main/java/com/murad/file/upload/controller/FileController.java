package com.murad.file.upload.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.murad.file.upload.model.FileDto;
import com.murad.file.upload.service.FileService;

@Controller
public class FileController {

	@Autowired
	private FileService fileService;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("showFileForm")
	public String showFileForm(Model model) {
		FileDto fileDto = new FileDto();
		model.addAttribute("fileDto", fileDto);
		return "showForm";
	}

	@PostMapping("/uploadFile")
	public String uploadFile(@ModelAttribute("fileDto") FileDto fileDto, @RequestParam("file") MultipartFile file)
			throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		fileDto.setFileName(fileName);
		fileService.saveFile(fileDto);

		Path uploadPath = Paths.get("uploads/");
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = file.getInputStream()) {
			Path imagePath = uploadPath.resolve(file.getOriginalFilename());
			System.out.println(imagePath.toFile().getAbsolutePath());
			Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("could not save");
		}

		return "redirect:/displayFile";

	}

	@GetMapping("/displayFile")
	public String displayFile(Model model) {
		model.addAttribute("files", fileService.getFiles());
		return "displayFile";
	}
	
	@RequestMapping(value = "getFile/{file}",method = RequestMethod.GET,produces = MediaType.ALL_VALUE)
	@ResponseBody
	public ResponseEntity<ByteArrayResource> getFile(@PathVariable("file") String file) {
		if (!file.equals("") || file != null) {
			try {
				Path fileName = Paths.get("uploads", file);
				byte[] buffer = Files.readAllBytes(fileName);
				ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
				return ResponseEntity.ok().body(byteArrayResource);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ResponseEntity.badRequest().build();
	}
}
