package com.pard.namukkun.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ReturnFileNameDTO {
    private List<String> fileNames = new ArrayList<>();

    public ReturnFileNameDTO(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}
