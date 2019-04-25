package br.com.ribas.fis.route.rest;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class LoggingRestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //@formatter:off
        rest("logging/")

                .consumes("application/json")
                .produces("application/json")

                .description("Samples Logging RouteBuilder")

                .get("/user")
                    .route()
                    .routeId("get-logging-id")
                    .routeDescription("This route get a example of logging")

                    .to("direct:internal-logging")
                .endRest();
        //@formatter:on

    }

}
