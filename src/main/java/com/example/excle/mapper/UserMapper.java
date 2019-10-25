package com.example.excle.mapper;

import com.example.excle.pojo.User;

import java.util.List;

public interface UserMapper {

    void saveAll(List list) throws  Exception;

    List<User> findAll() throws Exception;

}
