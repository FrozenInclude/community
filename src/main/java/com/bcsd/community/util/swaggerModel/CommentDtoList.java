package com.bcsd.community.util.swaggerModel;

import com.bcsd.community.controller.dto.response.CommentResponseDto;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;

import java.util.ArrayList;
import java.util.List;

public class CommentDtoList {
    public List<CommentResponseDto> json=new ArrayList<>();;
}
