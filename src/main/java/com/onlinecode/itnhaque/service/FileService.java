package com.onlinecode.itnhaque.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlinecode.itnhaque.domain.Course;
import com.onlinecode.itnhaque.domain.Filestore;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
import com.onlinecode.itnhaque.repository.CourseRepository;
import com.onlinecode.itnhaque.repository.FilestoreRepository;

@Service
public class FileService {
    @Value("${upload-file.base-uri}")
    private String baseURI;
    private final FilestoreRepository filestoreRepository;
    private final CourseRepository courseRepository;

    public FileService(FilestoreRepository filestoreRepository, CourseRepository courseRepository) {
        this.filestoreRepository = filestoreRepository;
        this.courseRepository = courseRepository;
    }

    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + tmpDir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    public String store(MultipartFile file, String folder) throws URISyntaxException, IOException {
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(baseURI + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        Filestore filestore = new Filestore();
        filestore.setName(finalName);
        filestore.setFolder(folder);
        this.filestoreRepository.save(filestore);
        return finalName;
    }

    public ResultPaginationDTO fetchFilesPagination(Specification<Filestore> spec, Pageable pageable) {
        Page<Filestore> pFiles = this.filestoreRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pFiles.getTotalPages());
        mt.setTotal(pFiles.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pFiles.getContent());
        return rs;
    }

    public Filestore findById(int id) {
        Optional<Filestore> opFilestore = this.filestoreRepository.findById(id);
        if (opFilestore.isPresent())
            return opFilestore.get();
        return null;
    }

    public boolean deleteFile(String fileName, String folder, int id) {
        try {
            // Tạo đường dẫn đầy đủ của file cần xóa
            URI uri = new URI(baseURI + folder + "/" + fileName);
            Path path = Paths.get(uri);

            // Xóa file trên hệ thống file
            Files.delete(path);
            System.out.println(">>> File deleted successfully, PATH = " + path);

            this.filestoreRepository.deleteById(id);

            Optional<Course> opCourse = this.courseRepository.findByImage(fileName);
            if (opCourse.isPresent()) {
                Course course = opCourse.get();
                course.setImage(null);
                this.courseRepository.save(course);
            }

            return true;
        } catch (NoSuchFileException e) {
            System.err.println(">>> No such file exists to delete: " + e.getMessage());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}