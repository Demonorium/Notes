package com.demonorium.utils;

public enum AccessRights {
    WRITE,
    REMOVE,
    SHARE;

    public Integer flag() {
        return 1 << this.ordinal();
    }
}
