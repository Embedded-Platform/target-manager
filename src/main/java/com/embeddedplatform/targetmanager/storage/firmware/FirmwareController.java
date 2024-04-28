package com.embeddedplatform.targetmanager.storage.firmware;

import com.embeddedplatform.targetmanager.config.controller.IdentityMapping;
import com.embeddedplatform.targetmanager.util.validatior.FileValidator;
import com.embeddedplatform.targetmanager.util.validatior.VersionValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/{idProject}/firmware")
public class FirmwareController {
    @Autowired
    @IdentityMapping
    private FirmwareService firmwareService;

    @GetMapping("/{idDeviceType}")
    public ResponseEntity<InputStreamResource> pullFirmware(
            @PathVariable UUID idProject,
            @PathVariable UUID idDeviceType,
            @VersionValidator @RequestParam(defaultValue = "latest", required = false) String version){

        this.firmwareService.setIdProject(idProject);
        this.firmwareService.setIdDeviceType(idDeviceType);

        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=firmware.bin")
                .body(this.firmwareService.downloadFirmware(version));
    }

    @PostMapping("/{idDeviceType}")
    public void pushFirmware(@PathVariable UUID idProject,
                             @PathVariable UUID idDeviceType,
                             @FileValidator(extensions = {".bin"}) @RequestParam("firmware") MultipartFile firmware,
                             @VersionValidator @RequestParam(defaultValue = "latest", required = false) String version) throws IOException, ResponseStatusException {

        this.firmwareService.setIdProject(idProject);
        this.firmwareService.setIdDeviceType(idDeviceType);
        this.firmwareService.uploadFirmware(firmware, version);
    }

    @DeleteMapping("/{idDeviceType}")
    public void deleteFirmware(@PathVariable UUID idProject,
                               @PathVariable UUID idDeviceType,
                               @VersionValidator @RequestParam String version){

        this.firmwareService.setIdProject(idProject);
        this.firmwareService.setIdDeviceType(idDeviceType);
        this.firmwareService.deleteFirmware(version);
    }
}
