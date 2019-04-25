package br.com.ribas.fis.route.internal;

import br.com.ribas.fis.route.LoggingRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TimerRoute extends LoggingRouteBuilder {

    @Override
    public void configure() throws Exception {
        //@formatter:off
        from("timer:internal-timer?fixedRate=true&period=3000")
                .routeId("timer-internal-timer-id")
                .autoStartup(false)
                .log("write >>>>>>>>>>>>>>>>>>>>>>>> ");

        //@formatter:on
    }

}
