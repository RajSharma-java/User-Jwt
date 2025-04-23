package com.user.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;


@JsonInclude(Include.NON_NULL)
@Data
public class BaseDto
{
    private boolean success;
    private String errorCode;
    private String message;
    private String description;
}
