package com.github.michaelsteven.archetype.springboot.webflux.items.configuration;

import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * The Class MdcContextConfig.
 */
@Configuration
public class MdcContextConfig {
    
    /** The mdc context reactor key. */
    private String MDC_CONTEXT_REACTOR_KEY = MdcContextConfig.class.getName();

    /**
     * Context operator hook.
     */
    @PostConstruct
    private void contextOperatorHook() {
        Hooks.onEachOperator(MDC_CONTEXT_REACTOR_KEY,
                Operators.lift((scannable, coreSubscriber) -> new MdcContextSubscriber<>(coreSubscriber))
        );
    }

    /**
     * Cleanup hook.
     */
    @PreDestroy
    private void cleanupHook() {
        Hooks.resetOnEachOperator(MDC_CONTEXT_REACTOR_KEY);
    }
}
