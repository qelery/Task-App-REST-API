package com.qelery.TaskRestApi.config;

import com.qelery.TaskRestApi.model.enums.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StatusEnumConverter implements Converter<String, Status> {

    @Override
    public Status convert(String source) {
        try {
            return Status.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
