package com.github.bestheroz.standard.context.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import com.github.bestheroz.standard.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import studio.raptor.sqlparser.SQLUtils;

public class LoggingLayout extends LayoutBase<ILoggingEvent> {
    private static final String TOTAL_LENGTH = "Total length : ";
    private static final String STR_SKIP_TOO_LONG_TEXT = "<!-- skip too long text -->";

    @Override
    public String doLayout(final ILoggingEvent event) {
        final StringBuffer sbuf = new StringBuffer(1024);

        if (StringUtils.endsWith(event.getLoggerName(), "TraceLoggingInAOP")) {
            this.getMessageOnylyFormattedMessage(event, sbuf);
        } else if (StringUtils.startsWithAny(event.getLoggerName(), "org.jdbcdslog.StatementLogger", "org.jdbcdslog.ResultSetLogger")) {
            if (StringUtils.startsWithAny(event.getFormattedMessage(), "java.sql.ResultSet.next")) {
                this.getMessageOnylyFormattedMessage(event, sbuf);
            } else {
                this.getSql(event, sbuf);
            }
        } else if (StringUtils.endsWithAny(event.getLoggerName(), "RequestResponseBodyMethodProcessor")) {
            this.getMessageStartWithMethod(event, sbuf);
        } else if (StringUtils.endsWith(event.getLoggerName(), "TraceLoggingInAOP") && StringUtils.equals(event.getLevel().levelStr, "WARN") && event.getCallerData()[0].getLineNumber() == 43) {
            this.getExceptionMessage(event, sbuf);
        } else if (StringUtils.endsWith(event.getLoggerName(), "SqlForTableVO") && StringUtils.equals(event.getLevel().levelStr, "DEBUG") &&
                StringUtils.equalsAny(event.getCallerData()[0].getMethodName(), "selectOneTableVO", "selectTableVO", "countTableVO", "insertTableVO", "updateTableVO", "deleteTableVO")) {
            this.getMessageStartWithMethod(event, sbuf);
        } else if (StringUtils.endsWith(event.getLoggerName(), "SqlSessionTemplateOverride") && StringUtils.equals(event.getLevel().levelStr, "DEBUG") &&
                StringUtils.equals(event.getCallerData()[0].getMethodName(), "writeLog")) {
            this.getMessageStartWithMethod(event, sbuf);
        } else if (StringUtils.startsWithAny(event.getFormattedMessage(), "==>  Preparing:", "==> Parameters:", "<==    Updates:", "<==      Total:")) {
            this.getMessageOnylyFormattedMessage(event, sbuf);
        } else {
            this.getMessageByStdPattern(event, sbuf);
        }

        return sbuf.toString();
    }

    private void getMessageByStdPattern(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        sbuf.append(event.getLoggerName());
        sbuf.append(".");
        sbuf.append(event.getCallerData()[0].getMethodName());
        sbuf.append("(");
        sbuf.append(event.getCallerData()[0].getLineNumber());
        sbuf.append(")");
        sbuf.append(" === ");
        sbuf.append(this.skipLogText(event.getFormattedMessage()));
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }

    private void getMessageStartWithMethod(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        sbuf.append(event.getCallerData()[0].getMethodName());
        sbuf.append(" === ");
        sbuf.append(this.skipLogText(event.getFormattedMessage()));
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }

    private void getExceptionMessage(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        sbuf.append("<");
        sbuf.append("[Throw Exception]");
        sbuf.append("> ");
        sbuf.append(this.skipLogText(event.getFormattedMessage()));
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }

    private void getMessageOnylyFormattedMessage(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        sbuf.append(this.skipLogText(event.getFormattedMessage()));
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }

    private void getSql(final ILoggingEvent event, final StringBuffer sbuf) {
        this.getMessageHeader(event, sbuf);
        String formattedMessage = event.getFormattedMessage();
        formattedMessage = StringUtils.remove(formattedMessage, "java.sql.Statement.execute: ");
        formattedMessage = StringUtils.remove(formattedMessage, "java.sql.PreparedStatement.execute: ");
        if (StringUtils.length(formattedMessage) > 20000) {
            sbuf.append("\n").append(StringUtils.abbreviate(formattedMessage, 20000)).append("\n");
        } else {
            sbuf.append("\n").append(SQLUtils.formatOracle(formattedMessage)).append("\n");
        }
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }

    private void getMessageHeader(final ILoggingEvent event, final StringBuffer sbuf) {
        sbuf.append(DateUtils.getString(event.getTimeStamp(), "HH:mm:ss.SSS"));
        sbuf.append(" [");
        sbuf.append(StringUtils.rightPad(event.getLevel().levelStr, 5));
        sbuf.append("] ");
    }

    private String skipLogText(final String formattedMessage) {
        String logString = formattedMessage;
        if (StringUtils.isNotEmpty(logString)) {
            // ???????????? ?????? ?????? ...(?????? request)
            String[] substringsBetween = StringUtils.substringsBetween(logString, "src=\\\"data:image/", "\">");
            if (substringsBetween != null) {
                for (final String str : substringsBetween) {
                    logString = StringUtils.replace(logString, str, STR_SKIP_TOO_LONG_TEXT);
                }
            }
            // ????????? ??????... (?????? response)
            substringsBetween = StringUtils.substringsBetween(logString, "src=&quot;data:image/", "&quot;&gt;");
            if (substringsBetween != null) {
                for (final String str : substringsBetween) {
                    logString = StringUtils.replace(logString, str, STR_SKIP_TOO_LONG_TEXT);
                }
            }
            // sql ????????????.. (?????? jdbcdslog)
            substringsBetween = StringUtils.substringsBetween(logString, "src='||chr(38)||'quot;data:image/", "chr(38)||'quot;'||chr(38)||'gt;'");
            if (substringsBetween != null) {
                for (final String str : substringsBetween) {
                    logString = StringUtils.replace(logString, str, STR_SKIP_TOO_LONG_TEXT);
                }
            }
            if (logString.length() > 60000) {
                logString = StringUtils.abbreviate(logString, STR_SKIP_TOO_LONG_TEXT, 60000).concat(TOTAL_LENGTH).concat(String.valueOf(logString.length()));
            }
        }
        return logString;
    }
}
