package com.pmo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> implements Serializable {
    private List<com.pmo.common.dto.ErrorInfoDTO> errors;
    private T data;
}
