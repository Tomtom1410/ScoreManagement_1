package com.scoremanagement.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
public class ResponseObject {
    private String message;
    private Object data;

}
