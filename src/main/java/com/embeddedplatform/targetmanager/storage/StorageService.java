package com.embeddedplatform.targetmanager.storage;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.embeddedplatform.targetmanager.bucket.S3Service;
import com.embeddedplatform.targetmanager.util.VersionUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StorageService {

    @Autowired
    private S3Service s3Service;

    @Setter
    private UUID idProject;
    @Setter
    private UUID idDeviceType;

    protected String objectType;
    protected String storageName;

    private String getPrefixPath(){
        return String.format("%s/%s/%s/", this.idProject, this.storageName, this.idDeviceType);
    }
    private String getBucketPath(String version){
        return String.format("%s%s/%s", this.getPrefixPath(), version, this.objectType);
    }

    private List<String> getAllTagsVersion(){
        try {
            return s3Service.getDirectoryList(this.getPrefixPath()).stream()
                    .map(prefix -> {
                        String[] parts = prefix.split("/");
                        return parts[parts.length - 1];
                    })
                    .filter(VersionUtil::isVersionTag)
                    .toList();
        }catch (AmazonS3Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage());
        }


    }


    protected void uploadObject(MultipartFile object,String version) throws IOException {
        if(version.equals("latest")){
            Optional<String> lastTagVersionOptional = VersionUtil.getLastTagVersion(getAllTagsVersion());
            version = lastTagVersionOptional
                    .map(s -> VersionUtil.addVersion(s, new Integer[]{1, 0, 0}))
                    .orElse("1.0.0");
        }
        try {
            this.s3Service.uploadFile(this.getBucketPath(version), object);
        }catch (AmazonS3Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage());
        }

    }

    protected InputStreamResource downloadObject(String version){
        if(version.equals("latest")){
            Optional<String> lastTagVersionOptional = VersionUtil.getLastTagVersion(getAllTagsVersion());

            if(lastTagVersionOptional.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Don't have available versions");

            version = lastTagVersionOptional.get();
        }
        try{
            S3Object object = this.s3Service.getFile(this.getBucketPath(version));
            return  new InputStreamResource(object.getObjectContent());
        }catch(AmazonS3Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage());
        }

    }

    protected void deleteObject(String version){
        try{
            if(version.equals("*"))
                this.s3Service.deleteDirectory(this.getPrefixPath());

            this.s3Service.deleteFile(version);
        }catch(AmazonS3Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage());
        }

    }
}
