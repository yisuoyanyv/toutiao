package cn.zjlspace;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan("cn.zjlspace.dao")
@SpringBootApplication
public class ToutiaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToutiaoApplication.class,args);
    }
}
