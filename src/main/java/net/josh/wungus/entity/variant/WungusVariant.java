package net.josh.wungus.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum WungusVariant {
    DEFAULT(0),
    WHITE(1);

    private static final WungusVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(WungusVariant::getId)).toArray(WungusVariant[]::new);
    private final int id;

    WungusVariant(int id) { this.id = id; }

    public int getId() { return this.id; }

    public static WungusVariant byId(int id) { return BY_ID[id % BY_ID.length]; }
}
