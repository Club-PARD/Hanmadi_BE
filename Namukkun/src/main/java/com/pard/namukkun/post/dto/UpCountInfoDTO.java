package com.pard.namukkun.post.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.user.dto.UserInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UpCountInfoDTO {
    private int upCount;
    private List<Long> upList;

    public UpCountInfoDTO(List<Long> upList, int upCount) {
        this.upList = upList;
        this.upCount = upCount;
    }
}
