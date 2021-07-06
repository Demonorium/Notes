package com.demonorium.utils;

public enum GroupFlags {
    DEFAULT,
    FIXED_LEVEL_ROOT,
    FIXED_USER,
    NO_RENAME,
    NO_ADD;

    public Integer flag() {
        return 1 << this.ordinal();
    }
}
