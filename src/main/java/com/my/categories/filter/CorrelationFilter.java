package com.my.categories.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * HTTP Request Correlation Servlet Filter
 */
@Component
public class CorrelationFilter implements Filter {

    private static final String HTTP_METHOD = "Http-Method";
    public static final String REQUEST_CORRELATION_ID = "correlation-id";
    private static final String CORRELATION_ID = "correlationId";
    private static final String REQUEST_SOURCE = "request-source";
    private static final String SOURCE = "source";
    private static final String CONTEXT_PATH = "Context-Path";
    private static final Logger LOGGER = LoggerFactory.getLogger(CorrelationFilter.class);
    private static final String CORRELATIONID_REGEX = "[a-zA-Z0-9-]+";

    @Override
    public void init(FilterConfig filterConfig) {
        //Implementation Not Required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String queryParams = httpRequest.getQueryString();
        String correlationId = httpRequest.getHeader(REQUEST_CORRELATION_ID);
        // source represents the origin of the request. Eg: Web or Mobile
        String source = httpRequest.getHeader(REQUEST_SOURCE);

        if (StringUtils.isBlank(correlationId)) {
            LOGGER.info("No Correlation ID found in the HTTP request. Generating a new ID.");
            correlationId = UUID.randomUUID().toString();
        }else if(!correlationId.matches(CORRELATIONID_REGEX)){
            String newCorrelationId = UUID.randomUUID().toString();
            LOGGER.info("Invalid Correlation ID {} received in HTTP request. Generating a new ID {}", correlationId, newCorrelationId);
            correlationId = newCorrelationId;   
        }

        MDC.put(CORRELATION_ID, correlationId);
        MDC.put(SOURCE, source);
        MDC.put(HTTP_METHOD, httpRequest.getMethod());

        //null check for endpoints without query params
        if (queryParams == null) {
            MDC.put(CONTEXT_PATH, httpRequest.getRequestURI());
        } else {
            MDC.put(CONTEXT_PATH, httpRequest.getRequestURI().concat("?").concat(queryParams));
        }

        LOGGER.debug("MDC Context has been set for the request");

        ((HttpServletResponse) response).setHeader(REQUEST_CORRELATION_ID, correlationId);

        try {
            chain.doFilter(request, response);
        } finally {
            LOGGER.debug("Clearing the MDC Context for the request");
            MDC.remove(CORRELATION_ID);
            MDC.remove(SOURCE);
            MDC.remove(HTTP_METHOD);
            MDC.remove(CONTEXT_PATH);
        }
    }

    @Override
    public void destroy() {
        //Implementation Not Required
    }
}
