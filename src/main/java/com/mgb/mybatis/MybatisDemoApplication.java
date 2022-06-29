package com.mgb.mybatis;

import com.mgb.mybatis.config.MyBatisConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.Arrays;

@SpringBootApplication
@MapperScan(basePackages = "com.mgb.mybatis.mapper")
public class MybatisDemoApplication implements WebMvcConfigurer {

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

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry){
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		resolver.setViewClass(JstlView.class);
		registry.viewResolver(resolver);
	}

}
