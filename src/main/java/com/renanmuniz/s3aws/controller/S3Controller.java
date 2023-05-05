package com.renanmuniz.s3aws.controller;

import com.amazonaws.services.s3.model.ObjectListing;
import com.renanmuniz.s3aws.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

@RestController
public class S3Controller {

    @Autowired
    S3Service service;

    @PostMapping("upload")
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        service.upload(file);
    }

    @GetMapping("list")
    public List<String> list() {
        return service.listObjectsInBucket();
    }

    @GetMapping(value = "download")
    public String download(@RequestParam String objectName, @RequestParam String downloadToFolder) {
        try {
            service.download(objectName, downloadToFolder);
        } catch (IOException e) {
            return "Error downloading file.";
        }
        return "File " + objectName + " downloaded to folder " + downloadToFolder;
    }

    @DeleteMapping(value = "delete")
    public void delete(@RequestParam String objectName) {
        service.delete(objectName);
    }

}
