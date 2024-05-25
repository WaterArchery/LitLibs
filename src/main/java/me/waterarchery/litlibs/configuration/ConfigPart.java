package me.waterarchery.litlibs.configuration;


import java.util.List;

public class ConfigPart {

    public static ConfigPart of(String path, Object value, List<String> comments) {
        return new ConfigPart(path, value, comments);
    }

    public static ConfigPart noComment(String path, Object value) {
        return new ConfigPart(path, value, null);
    }

    private final String path;
    private Object value;
    private List<String> comments;

    public ConfigPart(String path, Object value, List<String> comments) {
        this.path = path;
        this.value = value;
        this.comments = comments;
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }

    public List<String> getComments() {
        return comments;
    }

}
