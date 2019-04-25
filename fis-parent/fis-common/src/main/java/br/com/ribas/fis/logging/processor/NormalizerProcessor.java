package br.com.ribas.fis.logging.processor;

import br.com.ribas.fis.logging.engine.FixedLogParamEngine;
import br.com.ribas.fis.logging.model.FixedLogParam;
import br.com.ribas.fis.util.SubStringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Pattern;

public class NormalizerProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(NormalizerProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        FixedLogParam fixedLogParam = new FixedLogParamEngine().gettingFixedLogParams();

        Message message = exchange.getIn();

        StringBuilder bodyFormatted = new StringBuilder();

        bodyFormatted.append("\"requisitionId\": \"");
        bodyFormatted.append(message.getHeader("REQUISITION_ID", String.class));
        bodyFormatted.append("\", ");

        bodyFormatted.append("\"breadcrumbId\": \"");
        bodyFormatted.append(exchange.getIn().getHeader(Exchange.BREADCRUMB_ID));
        bodyFormatted.append("\", ");

        bodyFormatted.append("\"header\": ");
        bodyFormatted.append(extractHeaders(exchange.getIn().getHeaders()));
        bodyFormatted.append(", ");

        bodyFormatted.append("\"payload\": ");
        String body;
        try {
            body = message.getBody(String.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            body = null;
        }
        if (body == null || "".equals(body)) {
            Object object = message.getBody();
            if (object != null) {
                if (object instanceof MessageContentsList) {
                    MessageContentsList messageContentsList = (MessageContentsList) object;
                    if (!messageContentsList.isEmpty()) {
                        body = messageContentsList.get(0).toString();
                    }
                }
            } else {
                body = "";
            }
        }
        if (body != null && !body.isEmpty()) {
            if (isJson(body)) {
                bodyFormatted.append(SubStringUtil.removeExtraLines(body));
            } else {
                bodyFormatted.append("\"");
                bodyFormatted.append(SubStringUtil.replaceDoubleQuotes(SubStringUtil.removeExtraLines(body)));
                bodyFormatted.append("\"");
            }
        } else {
            bodyFormatted.append("null");
        }

        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        if (exception != null) {
            bodyFormatted.append(", \"exception\": {");

            bodyFormatted.append("\"message\": \"");
            bodyFormatted.append(SubStringUtil.replaceDoubleQuotes(SubStringUtil.removeExtraLines(exception.getMessage())));
            bodyFormatted.append("\", ");

            StringWriter stacktracePW = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stacktracePW);
            exception.printStackTrace(printWriter);

            bodyFormatted.append("\"stacktrace\": \"");
            bodyFormatted.append(SubStringUtil.replaceDoubleQuotes(SubStringUtil.removeExtraLines(stacktracePW.toString())));
            bodyFormatted.append("\"");
            bodyFormatted.append("}");
            exchange.setProperty("hasErrors", true);
        }

        Long startTime = exchange.getProperty("startTime", Long.class);

        if (startTime != null) {
            bodyFormatted.append(", \"timeSpent\": ");
            bodyFormatted.append(System.currentTimeMillis() - startTime);
            bodyFormatted.append(" ");
        }

        bodyFormatted.append(", \"server\":{");
        bodyFormatted.append("\"hostAddress\": \"");
        bodyFormatted.append(fixedLogParam.getHostAddress());
        bodyFormatted.append("\", \"hostName\": \"");
        bodyFormatted.append(fixedLogParam.getHostName());
        bodyFormatted.append("\", \"serverName\": \"");
        bodyFormatted.append(fixedLogParam.getServerName());
        bodyFormatted.append("}");

        message.setBody(bodyFormatted.toString());
    }

    @SuppressWarnings("rawtypes")
    private String extractHeaders(Map<String, Object> headers) throws JsonProcessingException {
        Map<String, Object> normalizedHeaders = new HashMap<>();
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                if (value instanceof Collection) {
                    Collection col = (Collection) value;
                    value = Arrays.toString(col.toArray());
                } else {
                    value = value.toString();
                }
            }
            normalizedHeaders.put(entry.getKey(), value);
        }
        return new ObjectMapper().writeValueAsString(normalizedHeaders);
    }

    private boolean isJson(String candidateJson) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(candidateJson);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("unused")
    private Object filterHeaders(Map<String, Object> headers) {
        Pattern camelHeaders = Pattern.compile("Camel.*");
        Set<Map.Entry<String, Object>> filteredHeaders = new HashMap<>(headers).entrySet();
        filteredHeaders.removeIf(entry -> camelHeaders.matcher(entry.getKey()).matches());
        return filteredHeaders;
    }
}
