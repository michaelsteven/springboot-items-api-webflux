package com.github.michaelsteven.archetype.springboot.webflux.items.configuration;

import java.util.Locale;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;

/**
 * The Class LocaleResolver.
 */
public class LocaleResolver implements LocaleContextResolver {

  /**
   * Resolve locale context.
   *
   * @param exchange the exchange
   * @return the locale context
   */
  @Override
  public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
    Locale targetLocale = Locale.getDefault();
	String language = exchange.getRequest().getHeaders().getFirst("Accept-Language");
    if ( StringUtils.isEmpty(language)) {
      targetLocale = Locale.forLanguageTag(language);
    }
    return new SimpleLocaleContext(targetLocale);
  }

  /**
   * Sets the locale context.
   *
   * @param exchange the exchange
   * @param localeContext the locale context
   */
  @Override
  public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
    throw new UnsupportedOperationException("Not Supported");
  }
}
