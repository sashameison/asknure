package asknure.narozhnyi.core.service;

import static org.springframework.util.StringUtils.getFilenameExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import asknure.narozhnyi.core.exceptions.InternalServiceErrorException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3Client amazonS3Client;
  @Value("${bucket-name}")
  private String bucketName;

  public String uploadFile(MultipartFile multipartFile) {
    var filenameExtension = getFilenameExtension(multipartFile.getOriginalFilename());
    var key = UUID.randomUUID() + "." + filenameExtension;

    var metadata = new ObjectMetadata();
    metadata.setContentLength(multipartFile.getSize());
    metadata.setContentType(multipartFile.getContentType());
    try {
      amazonS3Client.putObject(bucketName, key, multipartFile.getInputStream(), metadata);
    } catch (IOException ex) {
      throw new InternalServiceErrorException();
    }

    amazonS3Client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
    return amazonS3Client.getResourceUrl(bucketName, key);
  }

  public List<String> uploadFiles(List<MultipartFile> multipartFiles) {
    List<String> urls = new ArrayList<>();
    multipartFiles.forEach(multipartFile -> urls.add(uploadFile(multipartFile)));
    return urls;
  }

  public String getPresignedUrl(String filename) {
    var key = filename.replaceAll(".+.com/", "");
    var request = new GeneratePresignedUrlRequest(bucketName, key)
        .withMethod(HttpMethod.GET);
    var url = amazonS3Client.generatePresignedUrl(request);
    return url.toString();
  }
}
