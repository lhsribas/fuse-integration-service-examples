package br.com.ribas.fis.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestPropertyDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestRouteConfig extends RouteBuilder {

    private List<RestPropertyDefinition> corsHeaders;
    private RestPropertyDefinition rPD;

    @Value("{api.doc.name}")
    public String apiDocName;

    @Value("{api.doc.version}")
    public String apiDocVersion;

    public RestRouteConfig() {
        corsHeaders = new ArrayList<>();
    }

    /**
     * Inject the ServletRegistrationBean
     *
     * @return
     */
    @Bean
    private ServletRegistrationBean camelServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/r/api/v1/*");
        registration.setName("CamelServlet");
        return registration;
    }

    /**
     * Create a configuration to endpoints rest and inject the cors and  api cors
     *
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {
        //@formatter:off
        restConfiguration()
                .dataFormatProperty("prettyPrint", "true")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", apiDocName)
                .apiProperty("api.version", apiDocVersion)
                .apiProperty("cors", "true")
                .enableCORS(true)
                .setCorsHeaders(corsHeaders());
        //@formatter:on
    }

    /**
     * Create a List of Cors Configuration
     *
     * @return
     */
    private final List<RestPropertyDefinition> corsHeaders() {
        rPD = new RestPropertyDefinition();
        rPD.setKey("Access-Control-Allow-Headers");
        rPD.setValue("Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
        corsHeaders.add(rPD);
        return corsHeaders;
    }

}
