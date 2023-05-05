package com.renanmuniz.s3aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class S3Service {

    @Autowired
    AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public void upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }

        String path = String.format("%s/%s", bucketName, "docs");
        String fileName = String.format("%s", file.getOriginalFilename());

        amazonS3.putObject(path, fileName, file.getInputStream(), null);
    }

    public List<String> listObjectsInBucket() {
        ObjectListing objectListing = amazonS3.listObjects(bucketName);

        List<String> objects = new ArrayList<>();
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            objects.add(os.getKey());
        }

        return objects;
    }

    public void download(String fileName, String downloadToFolder) throws IOException {
        S3Object s3object = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        FileUtils.copyInputStreamToFile(
                inputStream,
                new File(downloadToFolder + fileName.substring(fileName.lastIndexOf("/") + 1)));
    }

    public void delete(String file) {
        amazonS3.deleteObject(bucketName,file);
    }


}
