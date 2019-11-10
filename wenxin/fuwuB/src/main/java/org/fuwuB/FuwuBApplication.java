package org.fuwuB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** 
* @author 作者 liangjq: 
* @version 创建时间：2019年11月9日  
*/
@SpringBootApplication(scanBasePackages = {"org.ljq","org.fuwuB"})
public class FuwuBApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FuwuBApplication.class, args);
	}

}
