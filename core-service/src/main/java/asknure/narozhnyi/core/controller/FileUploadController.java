package asknure.narozhnyi.core.controller;

import java.util.List;

import asknure.narozhnyi.core.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/v1/files")
public class FileUploadController {

  private final S3Service s3Service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String uploadFile(@RequestParam("file") MultipartFile multipartFile) {
    return s3Service.uploadFile(multipartFile);
  }

  @PostMapping("/bulk")
  @ResponseStatus(HttpStatus.CREATED)
  public List<String> uploadFiles(@RequestParam("file") List<MultipartFile> multipartFiles) {
    return s3Service.uploadFiles(multipartFiles);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String getPresignedUrl(@RequestParam("key") String key) {
    return s3Service.getPresignedUrl(key);
  }
}
