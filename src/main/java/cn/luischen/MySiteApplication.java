package cn.luischen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("cn.luischen.dao")
@EnableCaching
public class MySiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySiteApplication.class, args);
	}
}
