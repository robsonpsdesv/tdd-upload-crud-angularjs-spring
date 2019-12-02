package com.robson.upload.controller;

import com.robson.upload.model.Document;
import com.robson.upload.model.RequestResponse;
import com.robson.upload.repository.DocumentRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DocumentController {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentController(DocumentRepository documentRepository){
        this.documentRepository = documentRepository;
    }

    @GetMapping("/documents")
    public List<Document> listDocument(){
        List<Document> listDocument = new ArrayList<>();

        for (Document file : documentRepository.findAll()) {
            listDocument.add(file);
        }
        return listDocument;
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<Document> showDocument(@PathVariable Long id) {
        Optional<Document> optional = documentRepository.findById(id);
        if(optional.isPresent()){
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/documents/{id}/remove")
    public ResponseEntity<RequestResponse> removeDocument(@PathVariable Long id){
        Optional<Document> optional = documentRepository.findById(id);
        if (optional.isPresent()) {
            documentRepository.deleteById(id);
            Document file = optional.get();

            return ResponseEntity.ok(new RequestResponse(true, "Documento removido com sucesso!"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestResponse(false, "O documento não existe no servidor!"));
    }

    @PostMapping("/documents")
    public ResponseEntity<Document> saveDocument(@RequestBody Document document) {

        Document doc = null;
        Optional<Document> opt = documentRepository.findById(document.getId());
        if(opt.isPresent()) {
            doc = opt.get();
        } else {
            doc = new Document();
        }

        doc.setName(document.getName());
        doc = documentRepository.save(doc);

        return ResponseEntity.ok(doc);

    }

}
