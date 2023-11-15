package util;

import lombok.Getter;

@Getter
public enum StatusCode {
    CONTINUE("100"),
    SWITCHING_PROTOCOLS("101"),
    PROCESSING("102"),
    CHECKPOINT("103"),
    OK("200"),
    CREATED("201");

    @Getter
    private final String key;

    StatusCode(String key) {
        this.key = key;
    }

}
