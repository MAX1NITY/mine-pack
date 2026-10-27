package com.max1nity.ascension;

import javax.annotation.Nullable;

/** The three progression paths every player can advance in. */
public enum AscensionPath {
    TECH("tech"),
    ARCANE("arcane"),
    WANDERER("wanderer");

    private final String id;

    AscensionPath(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTranslationKey() {
        return "path." + AscensionCore.MODID + "." + id;
    }

    @Nullable
    public static AscensionPath fromId(String id) {
        for (AscensionPath path : values()) {
            if (path.id.equalsIgnoreCase(id)) {
                return path;
            }
        }
        return null;
    }
}
