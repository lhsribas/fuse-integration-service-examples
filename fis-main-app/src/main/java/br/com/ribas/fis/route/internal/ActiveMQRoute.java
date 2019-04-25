package br.com.ribas.fis.route.internal;

import br.com.ribas.fis.service.ActiveMQConfigurableService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQRoute extends RouteBuilder {

    @Autowired
    private ActiveMQConfigurableService activeMQConfigurableService;

    @Override
    public void configure() throws Exception {

        from("direct:active-mq")
                .routeId("direct-active-mq-id")
                .autoStartup(true)
                .log(">>>>>>>>>>>>>>>>>>>>>>>".concat(activeMQConfigurableService.amqURL));
    }

}
