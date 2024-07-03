package com.pard.namukkun.attachment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class S3AttachmentCreateDTO {
    private String fileUrl;
    private Long attachmentId;
}
