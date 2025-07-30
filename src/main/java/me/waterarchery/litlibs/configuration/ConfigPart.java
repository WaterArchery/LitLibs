package me.waterarchery.litlibs.configuration;


import lombok.Getter;

import java.util.*;

@Getter
public class ConfigPart {

    public static ConfigPart of(String path, Object value, List<String> comments) {
        return new ConfigPart(path, value, comments);
    }

    public static ConfigPart noComment(String path, Object value) {
        return new ConfigPart(path, value, null);
    }

    private final String path;
    private final Object value;
    private final List<String> comments;

    public ConfigPart(String path, Object value, List<String> comments) {
        this.path = path;
        this.value = value;
        this.comments = comments;
    }

}
