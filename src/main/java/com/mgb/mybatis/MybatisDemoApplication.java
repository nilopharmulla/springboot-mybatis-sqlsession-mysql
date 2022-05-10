package com.mgb.mybatis;

import com.mgb.mybatis.config.MyBatisConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@MapperScan(basePackages = "com.mgb.mybatis.mapper")
public class MybatisDemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(MybatisDemoApplication.class, args);
		AnnotationConfigApplicationContext ctx = new
				 AnnotationConfigApplicationContext(MyBatisConfig.class);

		String beanNames[] = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);

	 	 for(String beanName : beanNames) {
			  System.out.println(" Beans :" + beanName);
	 	 }
		 ctx.close();
	}

}
