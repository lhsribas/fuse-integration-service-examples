package br.com.ribas.fis.logging.processor;

import br.com.ribas.fis.exception.CommonException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingProcessor implements Processor {

    private static Logger logger = LoggerFactory.getLogger(LoggingProcessor.class);

    @Override
    public void process(Exchange exchange) throws CommonException {
        logger.debug("[LOGP] Logging request...");
        exchange.setProperty("startTime", System.currentTimeMillis());
    }
}
