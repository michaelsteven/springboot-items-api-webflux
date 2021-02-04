package com.github.michaelsteven.archetype.springboot.webflux.items.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * The Class MessageLocalizationConfig.
 */
@Component
public class MessageLocalizationConfig {
	
	/** The message bundle base name. */
	private final String MESSAGE_BUNDLE_BASE_NAME = "messages";
	
	/**
	 * Message source.
	 *
	 * @return the message source
	 */
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(MESSAGE_BUNDLE_BASE_NAME);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
	/**
	 * Locale resolver.
	 *
	 * @return the locale resolver
	 */
	@Bean
	public LocaleResolver localeResolver() {
		return new LocaleResolver();
	}
	
}
