package com.vzard.doutu.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseTemplate {

    Integer code;

    String msg;

    Object data;


}
