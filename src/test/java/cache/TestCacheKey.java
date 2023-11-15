package cache;

import lombok.Getter;

@Getter
public enum TestCacheKey {
    LOGIN("login"),
    PASSWORD("password"),
    SCREEN_NAME("screenName"),
    ID("id"),
    AGE("age");

    @Getter
    private final String key;

    TestCacheKey(String key) {
        this.key = key;
    }
}
