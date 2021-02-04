package com.github.michaelsteven.archetype.springboot.webflux.items.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.michaelsteven.archetype.springboot.webflux.items.aspect.LoggerAspect;

/**
 * The Class AspectConfig.
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

	private ObjectMapper objectMapper;
	
	/**
	 * Constructor.
	 *
	 * @param objectMapper the object mapper
	 * @param messageSource the message source
	 */
	public AspectConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	/**
	 * Logger aspect.
	 *
	 * @param environment the environment
	 * @return the logger aspect
	 */
	@Bean
	public LoggerAspect loggerAspect(Environment environment) {
		return new LoggerAspect(objectMapper);
	}
	
}