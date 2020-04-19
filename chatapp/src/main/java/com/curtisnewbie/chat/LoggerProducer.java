package com.curtisnewbie.chat;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

/**
 * Producer of Logger
 */
@ApplicationScoped
public class LoggerProducer {

    @Produces
    public Logger logger(InjectionPoint injectPt) {
        return Logger.getLogger(injectPt.getBean().getBeanClass());
    }
}