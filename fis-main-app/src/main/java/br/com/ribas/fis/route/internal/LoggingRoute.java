package br.com.ribas.fis.route.internal;

import br.com.ribas.fis.model.User;
import br.com.ribas.fis.route.LoggingRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class LoggingRoute extends LoggingRouteBuilder {

    @Override
    public void configure() throws Exception {
        //@formatter:off
        from("direct:internal-logging")
                .routeId("internal-logging-id")
                .routeDescription("This route returns a simple User")
                .to(LOGGING_BEGIN)
                .process(exchange -> {
                    User user = new User(1L, "lribas", "lribas@redhat.com", true);
                    exchange.getIn().setBody(user);
                })
                .to(LOGGING_END)
                .end();
        //@formatter:on
    }
}
