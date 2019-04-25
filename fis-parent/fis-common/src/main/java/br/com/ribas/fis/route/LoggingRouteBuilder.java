package br.com.ribas.fis.route;

import org.apache.camel.builder.RouteBuilder;

public abstract class LoggingRouteBuilder extends RouteBuilder {

    public static final String LOGGING_BEGIN = "direct:logger-wiretap-begin";
    public static final String LOGGING_END = "direct:logger-wiretap-end";
    public static final String LOGGING_MSG = "direct:logger-wiretap-msg";

}
