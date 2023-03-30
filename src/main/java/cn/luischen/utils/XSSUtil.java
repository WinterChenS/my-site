package cn.luischen.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author winterchen
 * @version 1.0
 * @date 2023/3/30 11:19
 * @description TODO
 **/
public class XSSUtil {
    private static final Logger LOG = LoggerFactory.getLogger(XSSUtil.class);

    private XSSUtil() {
        // empty private constructor
    }

    private static final Pattern NULL_CHAR = Pattern.compile("\0");
    private static final Pattern[] PATTERNS =
            new Pattern[] {
                    // Avoid anything in a <script> type of expression
                    Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
                    // Avoid anything in a src='...' type of expression
                    Pattern.compile(
                            "src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
                            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                    Pattern.compile(
                            "src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
                            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                    // Remove any lonesome </script> tag
                    Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
                    // Avoid anything in a <iframe> type of expression
                    Pattern.compile("<iframe>(.*?)</iframe>", Pattern.CASE_INSENSITIVE),
                    // Remove any lonesome <script ...> tag
                    Pattern.compile(
                            "<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                    // Remove any lonesome <img ...> tag
                    Pattern.compile(
                            "<img(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                    // Avoid eval(...) expressions
                    Pattern.compile(
                            "eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                    // Avoid expression(...) expressions
                    Pattern.compile(
                            "expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                    // Avoid javascript:... expressions
                    Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
                    // Avoid vbscript:... expressions
                    Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
                    // Avoid onload= expressions
                    Pattern.compile(
                            "on(load|error|mouseover|submit|reset|focus|click)(.*?)=",
                            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
            };

    /**
     * Method tests whether a string contains malicious XSS script or not.
     *
     * @param value decoded string to test
     *
     * @return true if string matches at least one XSS pattern, or false otherwise
     */
    public static boolean containsXSS(String value) {
        if (value != null) {

            // Avoid null characters
            String cleanValue = NULL_CHAR.matcher(value).replaceAll("");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : PATTERNS) {
                Matcher matcher = scriptPattern.matcher(cleanValue);
                if (matcher.find()) {
                    LOG.warn("Potentially malicious XSS script found: {}", cleanValue);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Strip all instances of all XXS patterns that match.
     *
     * @param value
     * @return
     */
    public static String stripXSS(String value) {

        if (value == null) {
            return null;
        }

        // Avoid null characters
        String cleanValue = NULL_CHAR.matcher(value).replaceAll("");
        if (StringUtils.isBlank(cleanValue)) {
            return cleanValue;
        }

        // Remove all sections that match a pattern
        for (Pattern scriptPattern : PATTERNS) {
            Matcher matcher = scriptPattern.matcher(cleanValue);
            cleanValue = matcher.replaceAll("");
        }
        return cleanValue;
    }
}