package com.nhohantu.tcbookbe.common.model.enums;

public enum ArticleTypeEnum {
    NEWS("NEWS"),
    ACTIVITY("ACTIVITY"),
    POLITICAL("POLITICAL"),
    LAW("LAW"),
    DOCUMENT("DOCUMENT");

    public final String value;

    ArticleTypeEnum(String i) {
        value = i;
    }
}
