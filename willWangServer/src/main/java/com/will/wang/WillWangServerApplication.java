package com.will.wang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.will.wang.demo.dao") // mybatis扫描路径，针对的是接口Mapper类
public class WillWangServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WillWangServerApplication.class, args);
	}
}
