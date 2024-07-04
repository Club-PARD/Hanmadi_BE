package com.pard.namukkun.attachment.dto;

import com.pard.namukkun.attachment.entity.S3Attachment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class S3AttachmentReadDTO {
    private String fileUrl;
    private Long attachmentId;

    public S3AttachmentReadDTO(S3Attachment s3Attachment) {
        fileUrl = s3Attachment.getFileUrl();
        attachmentId = s3Attachment.getAttachmentId();
    }
}
