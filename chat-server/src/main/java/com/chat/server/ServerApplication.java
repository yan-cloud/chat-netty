package com.chat.server;

import com.chat.server.dao.UserInfoMapper;
import com.chat.server.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class ServerApplication {
    @Autowired
    UserInfoMapper userInfoMapper;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @GetMapping("query")
    public List query(){
        return userInfoMapper.selectAll();
    }
}
