package com.robson.upload.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "files", schema = "public")
public class File implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String pathFile;
    private String originalName;
    private Long byteSize;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "document_id")
    private Document document;

    public File() {
    }

    public File(String pathFile, String originalName, Long byteSize) {
        this.id = id;
        this.pathFile = pathFile;
        this.originalName = originalName;
        this.byteSize = byteSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Long getByteSize() {
        return byteSize;
    }

    public void setByteSize(Long byteSize) {
        this.byteSize = byteSize;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}


