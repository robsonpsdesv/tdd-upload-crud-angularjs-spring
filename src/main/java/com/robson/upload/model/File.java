package com.robson.upload.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "files", schema = "public")
public class File {

    @Id
    @GeneratedValue
    private Long id;
    private String pathFile;
    private String originalName;
    private Long byteSize;

    public File() {
    }

    public File(String pathFile, String originalName, Long byteSize) {
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

}


