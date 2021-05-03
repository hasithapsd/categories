package com.my.categories.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.ClassUtils;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

@Plugin(name = "CustomJsonLayout", category = "Core", elementType = "layout", printObject = true)
public class CustomJsonLayout extends AbstractStringLayout {

    // Appender includes the location information in the log4j.xml file
    private final boolean locationInfo;
    private final int maxStackTraceLimit;
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private CustomJsonLayout(Charset aCharset, boolean locationInfo, int maxStackTraceLimit) {
        super(aCharset);
        this.locationInfo = locationInfo;
        this.maxStackTraceLimit = maxStackTraceLimit;
    }

    @PluginFactory
    public static CustomJsonLayout createLayout(
            @PluginAttribute(value = "charset", defaultString = "US-ASCII") final Charset charset, @PluginAttribute(value = "locationInfo",
            defaultBoolean = false) final boolean locationInfo,
            @PluginAttribute(value = "maxStackTraceLimit", defaultInt = 10) final int maxStackTraceLimit) {
        return new CustomJsonLayout(charset, locationInfo, maxStackTraceLimit);
    }

    @Override
    public boolean requiresLocation() {
        return locationInfo;
    }

    @Override
    public String toSerializable(final LogEvent event) {
        StringBuilder builder = new StringBuilder("{");
        Date date = new Date(event.getTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat(CustomJsonLayout.DATE_FORMAT);
        writeStringField(builder, "@timestamp", dateFormat.format(date));

        if (locationInfo) {
            final StackTraceElement source = event.getSource();
            writeStringField(builder, "classname", source.getClassName());
            writeStringField(builder, "method", source.getMethodName());
            writeStringField(builder, "file", source.getFileName());
            writePrimitiveField(builder, "line", source.getLineNumber());
        }

        formatExceptionField(builder, event);

        writeStringField(builder, "thread", event.getThreadName());
        writeStringField(builder, "logger_name", event.getLoggerName());
        writeMessageOrArgField(builder, "message", event.getMessage().getFormattedMessage());
        writeStringField(builder, "level", event.getLevel().toString());
        if (event.getMessage() != null && event.getMessage().getParameters() != null) {
            writeObjectField(builder, event.getMessage().getParameters());
        }

        final Set<String> contextKeys = new TreeSet<>(event.getContextData().toMap().keySet());

        for (final String key : contextKeys) {
            writeStringField(builder, key, event.getContextData().getValue(key));
        }

        // Remove last character (,) of the Json String
        if (builder.length() > 0 && builder.charAt(builder.length() -1) == ',') {
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append("}");
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    /**
     * Append String fields to the builder
     */
    private void writeStringField(StringBuilder builder, String fieldName, Object value) {
        builder.append("\"").append(fieldName).append("\":").append("\"").append(value).append("\",");
    }

    private void writeMessageOrArgField(StringBuilder builder, String fieldName, Object value) {
        try {
            builder.append("\"").append(fieldName).append("\":").append(CustomJsonLayout.MAPPER.
                    writeValueAsString(value)).append(",");
        } catch (Exception e) {
            builder.append("\"\"").append(",");
            LOGGER.error("Error occurred while Json formatting field : "+fieldName, e);
        }
    }

    /**
     * Append int fields to the builder
     */
    private void writePrimitiveField(StringBuilder builder, String fieldName, Object value) {
        builder.append("\"").append(fieldName).append("\":").append(value).append(",");
    }

    /**
     * Only use Jackson parser if the type is Object, if not use StringBuilder
     */
    private void writeObjectField(StringBuilder builder, Object[] objects) {
        int i = 0;
        for (Object obj : objects) {
            if (obj != null) {
                String argName = "arg" + i;
                if (ClassUtils.isPrimitiveOrWrapper(obj.getClass())) {
                    writePrimitiveField(builder, argName, obj);
                } else {
                    // Append the Object directly since using Jackson parser
                    writeMessageOrArgField(builder, argName, obj);
                }
                i++;
            }
        }
    }

    /**
     * Logging throwable exception, cause and stacktrace
     */
    private void formatExceptionField(final StringBuilder builder, final LogEvent event) {
        if (event.getThrownProxy() != null) {
            final ThrowableProxy thrownProxy = event.getThrownProxy();
            final Throwable throwable = thrownProxy.getThrowable();
            if (throwable == null) {
                return;
            }
            final String exceptionsClass = throwable.getClass().getCanonicalName();
            if (exceptionsClass != null) {
                writeStringField(builder, "exception", exceptionsClass);
            }

            final String exceptionMessage = throwable.getMessage();
            if (exceptionMessage != null) {
                writeMessageOrArgField(builder, "cause", exceptionMessage);
            }
            writeStackTrace(throwable, builder);
        }
    }

    private void writeStackTrace(Throwable throwable, final StringBuilder builder) {
        StringBuilder stackTraceStringBuilder = new StringBuilder();
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            StackTraceElement stackTraceElement;
            final int actualLength = Math.min(stackTraceElements.length, maxStackTraceLimit);
            for (int i = 0; i < actualLength; i++) {
                stackTraceElement = stackTraceElements[i];
                stackTraceStringBuilder.append(stackTraceElement);
            }
        }
        writeStringField(builder, "stacktrace", stackTraceStringBuilder.toString());
    }
}