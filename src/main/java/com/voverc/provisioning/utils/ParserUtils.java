package com.voverc.provisioning.utils;

import com.sun.javaws.exceptions.InvalidArgumentException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;


/**
 * @author Nazar Lelyak.
 */
@Slf4j
@UtilityClass
public class ParserUtils {

    public String removeAllNonDigits(String line) {
        Validate.notEmpty(line, "line can't be null or empty");
        return line.replaceAll("[^0-9]", "").trim();
    }

    public String getPropertyValue(String line) {
        Validate.notEmpty(line, "line can't be null or empty");

        String result;
        if (isPropertyFormat(line)) {
            String[] array = line.split("=");
            result = array[1].trim();
        } else {
            throw new IllegalArgumentException("wrong property format for: " + line);
        }
        return result;
    }

    public boolean isPropertyFormat(String line) {
        Validate.notEmpty(line, "line can't be null or empty");
        return line.contains("=");
    }
}
