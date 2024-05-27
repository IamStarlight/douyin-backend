package com.bjtu.movie.handler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonListTypeHandler extends JacksonTypeHandler {

    private final Class<?> type;
    public JsonListTypeHandler(Class<?> type) {
        super(type);
        this.type = type;
    }

    @Override
    protected Object parse(String json){
        try {
            return getObjectMapper().readValue(json,this.type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
