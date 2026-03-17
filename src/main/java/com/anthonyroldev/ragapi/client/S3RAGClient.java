package com.anthonyroldev.ragapi.client;

import com.anthonyroldev.ragapi.config.S3Properties;
import com.anthonyroldev.ragapi.exception.RAGStorageException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

@Service
@AllArgsConstructor
public class S3RAGClient {
    private final S3Client s3Client;
    private S3Properties s3Properties;

    public void uploadDocument(MultipartFile file, String key) {
        try {
            s3Client.putObject(builder -> builder.bucket(s3Properties.bucket())
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (S3Exception | IOException e) {
            throw RAGStorageException.documentUploadFailed(file.getOriginalFilename());
        } catch (SdkClientException e) {
            throw RAGStorageException.clientSideError(key);
        }
    }

    public ResponseInputStream<GetObjectResponse> downloadDocument(String key) {
        try {
            return s3Client.getObject(builder -> builder.bucket(s3Properties.bucket())
                    .key(key)
                    .build());
        } catch (S3Exception e) {
            throw RAGStorageException.documentDownloadFailed(key);
        } catch (SdkClientException e) {
            throw RAGStorageException.clientSideError(key);
        }
    }
}
