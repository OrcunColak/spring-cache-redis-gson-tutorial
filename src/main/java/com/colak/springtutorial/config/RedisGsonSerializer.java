package com.colak.springtutorial.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

@Slf4j
public class RedisGsonSerializer<T> implements RedisSerializer<T> {

    private final Gson gson;
    private final Class<T> type;

    public RedisGsonSerializer(Class<T> type) {
        log.info("<< GSON Init >>...type: {}", type);
        this.type = type;

        this.gson = new GsonBuilder()
                .create();
    }

    @Override
    public byte[] serialize(T value) throws SerializationException {
        log.info("<< GSON Serialization >>...{}", value);
        return gson.toJson(value).getBytes();
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        try {
            String string = new String(bytes, StandardCharsets.UTF_8);
            log.info("<< GSON DeSerializaion >>...{} / {}", type, string);
            return gson.fromJson(string, this.type);

        } catch (JsonSyntaxException e) {
            throw new SerializationException(e.getMessage());
        }
    }

}
