package com.github.michaelsteven.archetype.springboot.webflux.items.configuration;

import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

/**
 * The Class AuditorAwareImpl.
 * see: https://medium.com/swlh/data-auditing-with-spring-data-r2dbc-5d428fc94688
 */
@Component
public class AuditorAwareImpl implements ReactiveAuditorAware<String> {

	/**
	 * Gets the current auditor.
	 *
	 * @return the current auditor
	 */
	@Override
	public Mono<String> getCurrentAuditor() {
		// SecurityContextHolder.getContext().getAuthentication().getName();
		return Mono.just("unknown");
	}
}
