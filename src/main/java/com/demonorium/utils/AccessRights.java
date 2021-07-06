package com.demonorium.utils;

public enum AccessRights {
    BASIC,
    WRITE,
    REMOVE;

    public Integer flag() {
        return 1 << this.ordinal();
    }
}
