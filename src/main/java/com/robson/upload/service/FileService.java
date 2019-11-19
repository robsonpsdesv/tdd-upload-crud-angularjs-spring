package com.robson.upload.service;

import com.robson.upload.model.File;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FileService {

    private final Path rootDir = Paths.get(System.getProperty("user.dir")+"/files");

    public void init() {
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório raiz!");
        }
    }

    public File saveUpload(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();

            Path path = this.rootDir.resolve(generateNewFileName(originalName));
            Files.copy(file.getInputStream(), path);

            return new File(path.toString(), originalName, file.getSize());
        } catch (IOException ex){
            throw new RuntimeException("Erro ao salvar o arquivo!");
        }
    }

    public Resource getResourceForDownload(File file) {
        try {
            Path fileContent = rootDir.resolve(file.getPathFile());
            Resource resource = new UrlResource(fileContent.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("O arquivo informado não existe ou não pode ser lido!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Caminho inválido!");
        }
    }

    public boolean remove(File file) {
        if (file != null) {
            Path path = Paths.get(file.getPathFile());
            try {
                return Files.deleteIfExists(path);
            } catch (IOException ex) {
                Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public void removeAll() {
        FileSystemUtils.deleteRecursively(rootDir.toFile());
    }

    private String generateNewFileName(String originalName) {
        int index = originalName.lastIndexOf(".");
        String name = originalName.substring(0, index);
        String extension = originalName.substring(index + 1);

        StringBuilder newName = new StringBuilder(name);
        newName.append("_");
        newName.append(getActualHourFormated());
        newName.append("."+extension);

        return newName.toString();
    }

    private String getActualHourFormated(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();

        return formatter.format(now);
    }

}
