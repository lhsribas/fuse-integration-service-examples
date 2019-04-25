package br.com.ribas.fis.util;

import java.util.regex.Pattern;

public class SubStringUtil {

    private static Pattern patternExtraLines = Pattern.compile("\\n|\\r+");
    private static Pattern patternDoubleQuotes = Pattern.compile("\"");

    public static String removeExtraLines(String headers) {
        if (headers != null) {
            return patternExtraLines.matcher(headers).replaceAll(" ");
        }
        return headers;
    }

    public static String replaceDoubleQuotes(String body) {
        if (body != null) {
            body = patternDoubleQuotes.matcher(body).replaceAll("\\\\\"");
        }
        return body;
    }

}
