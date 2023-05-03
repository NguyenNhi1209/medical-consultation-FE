package com.example.DNFrontEnd.Model.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class BasePaginationResponse<T>{

    private int code;

    private String message;

    private String messageCode;

    public long currentPage;

    public int pageIndex;

    public int pageSize;

    public long totalPages;

    public long totalItems;

    private List<T> data;
}
