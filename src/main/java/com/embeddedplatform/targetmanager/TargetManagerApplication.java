package com.embeddedplatform.targetmanager;

import com.embeddedplatform.targetmanager.config.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableConfigurationProperties
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class TargetManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TargetManagerApplication.class, args);
	}

}
