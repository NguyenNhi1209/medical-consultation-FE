package com.example.DNFrontEnd.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class DepartmentService {
    @Value("${admin.rest.url:}")
    private String adminUrl;

    private Logger logger = Logger.getLogger(getClass().getName());


}
