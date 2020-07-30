package com.schooldevops.monolithic.demomonolithic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication는 스프링 부트 어플리케이션의 시작 포인트를 나타낸다.
 * 		Config 값을 찾고, 자동 Config 를 수행하며, 어플리케이션에서 사용할 컴포넌트를 검색하는 역할등을 수행한다.
 */
@SpringBootApplication
public class DemoMonolithicApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMonolithicApplication.class, args);
	}

}
