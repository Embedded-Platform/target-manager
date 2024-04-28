package com.embeddedplatform.targetmanager.storage.firmware;

import com.embeddedplatform.targetmanager.storage.StorageService;
import com.embeddedplatform.targetmanager.storage.firmware.model.PushResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FirmwareService extends StorageService {


    FirmwareService(){
        super.storageName = "firmware";
    }

    public void uploadFirmware(MultipartFile firmware) throws IOException {
        this.uploadFirmware(firmware, "latest");
    }
    public PushResponse uploadFirmware(MultipartFile firmware, String version) throws IOException {
        super.objectType = "firmware.bin";
        return super.uploadObject(firmware, version);
    }
    public InputStreamResource downloadFirmware(){
        return downloadFirmware("latest");
    }
    public InputStreamResource downloadFirmware(String version){
        super.objectType = "firmware.bin";
        return  super.downloadObject(version);
    }
    public  void  deleteFirmware(String version){
        super.objectType = "firmware.bin";
        super.deleteObject(version);
    }
}
