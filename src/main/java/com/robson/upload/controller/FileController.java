package com.robson.upload.controller;

import com.robson.upload.model.Document;
import com.robson.upload.model.File;
import com.robson.upload.model.RequestResponse;
import com.robson.upload.repository.DocumentRepository;
import com.robson.upload.repository.FileRepository;
import com.robson.upload.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    private final FileRepository fileRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public FileController(FileRepository fileRepository, DocumentRepository documentRepository) {
        this.fileRepository = fileRepository;
        this.documentRepository = documentRepository;
    }

    @GetMapping("/files")
    public List<File> list() {
        List<File> list = new ArrayList<>();

        for (File file : fileRepository.findAll()) {
            list.add(file);
        }
        return list;
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<File> show(@PathVariable Long id) {
        Optional<File> optional = fileRepository.findById(id);
        if(optional.isPresent()){
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/files/{id}/remove")
    public ResponseEntity<RequestResponse> remove(@PathVariable Long id) {
        Optional<File> optional = fileRepository.findById(id);
        if(optional.isPresent()) {
            File file = optional.get();
            if(fileService.remove(file)) {
                fileRepository.deleteById(id);

                return ResponseEntity.ok(new RequestResponse(true, "Arquivo removido com sucesso!"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestResponse(false, "O arquivo não existe no servidor"));
    }

    @PostMapping("/files")
    public ResponseEntity<RequestResponse> upload(@RequestParam("file")MultipartFile file) {
        File fileContent = fileService.saveUpload(file);
        if(fileContent != null){
            fileRepository.save(fileContent);
            return ResponseEntity.ok(new RequestResponse(true, "Upload realizado com sucesso!"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new RequestResponse(false, "Erro ao realizar o upload!"));
        }
    }

    @PostMapping("/files2/{documentId}")
    public ResponseEntity<RequestResponse> upload2(@RequestParam("file")MultipartFile file, @PathVariable Long documentId){
        Optional<Document> optDoc = documentRepository.findById(documentId);
        if(optDoc.isPresent()){
            File fileContent = fileService.saveUpload(file);
            if (file != null) {
                fileContent.setDocument(optDoc.get());
                fileRepository.save(fileContent);
                return ResponseEntity.ok(new RequestResponse(true, "Upload realizado com sucesso!"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new RequestResponse(false, "Houve um erro ao fazer upload!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestResponse(false, "Registro de documento não encontrado!"));
        }
    }

    @GetMapping("/files/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id, HttpServletRequest request) {
        Optional<File> optional = fileRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        File fileContent = optional.get();
        Resource resource = fileService.getResourceForDownload(fileContent);
        try {
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if(contentType == null){
                contentType = "application/octet-stream";
            }

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .headers(header)
                    .body(resource);
        } catch (IOException ex) {
            return null;
        }
    }

}
