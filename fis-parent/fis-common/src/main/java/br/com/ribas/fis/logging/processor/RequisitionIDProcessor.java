package br.com.ribas.fis.logging.processor;

import br.com.ribas.fis.exception.CommonException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.UUID;

public class RequisitionIDProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws CommonException {
        UUID randomUUID = UUID.randomUUID();
        exchange.getIn().setHeader("REQUISITION_ID", randomUUID);
    }

}
