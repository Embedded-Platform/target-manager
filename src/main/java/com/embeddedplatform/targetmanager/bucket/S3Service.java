package com.embeddedplatform.targetmanager.bucket;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
public class S3Service {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);
    @Autowired
    private AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;



    public void uploadFile(String keyName, MultipartFile file) throws IOException {
        log.info("Upload object: {}", keyName);
        var putObjectResult = s3client.putObject(bucketName, keyName, file.getInputStream(), null);
        System.out.println(putObjectResult.getMetadata());

    }

    public S3Object getFile(String keyName) {
        return s3client.getObject(bucketName, keyName);
    }
    public void deleteFile(String keyname) {
        this.s3client.deleteObject(this.bucketName,keyname);
    }
    public void deleteDirectory(String path){
        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request();
        listObjectsRequest.setBucketName(this.bucketName);
        listObjectsRequest.setDelimiter(path);

        ListObjectsV2Result listObjectsV2Result = this.s3client.listObjectsV2(listObjectsRequest);
        List<String> keys = listObjectsV2Result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .toList();

        keys.forEach(key -> {
            this.s3client.deleteObject(this.bucketName, key);
        });

    }
    public List<String> getDirectoryList(String prefix){

        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request();
        listObjectsRequest.setBucketName(this.bucketName);
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setDelimiter("/");

        ListObjectsV2Result listObjectsV2Result = this.s3client.listObjectsV2(listObjectsRequest);

        return listObjectsV2Result.getCommonPrefixes();
    }

}
