package com.qelery.TaskRestApi.config;

import com.qelery.TaskRestApi.model.enums.Priority;
import org.springframework.core.convert.converter.Converter;

public class PriorityEnumConverter implements Converter<String, Priority> {

    @Override
    public Priority convert(String source) {
        try {
            return Priority.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
