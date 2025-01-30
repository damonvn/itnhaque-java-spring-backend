package com.onlinecode.itnhaque.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.onlinecode.itnhaque.domain.Filestore;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
import com.onlinecode.itnhaque.domain.response.file.ResUploadFileDTO;
import com.onlinecode.itnhaque.service.FileService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;
import com.onlinecode.itnhaque.util.error.StorageException;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${upload-file.base-uri}")
    private String baseURI;
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder

    ) throws URISyntaxException, IOException, StorageException {
        // skip validate
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please upload a file.");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png",
                "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValid) {
            throw new StorageException("Invalid file extension. only allows " +
                    allowedExtensions.toString());
        }
        // create a directory if not exist
        this.fileService.createDirectory(baseURI + folder);
        String uploadFile = this.fileService.store(file, folder);
        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/file")
    @ApiMessage("Fetch files pagination")
    public ResponseEntity<ResultPaginationDTO> fetchFiles(@Filter Specification<Filestore> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.fileService.fetchFilesPagination(spec, pageable));
    }

    @DeleteMapping("/file/{id}")
    @ApiMessage("Delete a file")
    public ResponseEntity<Boolean> delete(@PathVariable("id") int id) throws IdInvalidException {
        Filestore file = this.fileService.findById(id);
        if (file == null) {
            throw new IdInvalidException("File with id = " + id + " does not exist in database");
        }
        return ResponseEntity.ok().body(this.fileService.deleteFile(file.getName(), file.getFolder(), file.getId()));
    }
}
