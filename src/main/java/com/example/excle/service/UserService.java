package com.example.excle.service;

import com.example.excle.pojo.User;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public interface UserService {

    void saveAll(List list) throws  Exception;

    List<User> findAll() throws Exception;

    Object getCellValue(Cell cell);
}
