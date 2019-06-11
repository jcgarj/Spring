package mx.com.aion.data.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateRegularExp {

    public boolean validateRegExp(String tagValue, String regExp) {

        final Pattern pattern = Pattern.compile(regExp, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(tagValue);

        while (matcher.find()) {
            if (matcher.group(0) != tagValue) {
                return false;
            }
        }
        return true;
    }
}
