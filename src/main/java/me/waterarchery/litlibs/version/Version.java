package me.waterarchery.litlibs.version;

public enum Version {

    v1_7 (1, "1.7"),
    v1_8 (2, "1.8"),
    v1_9 (3, "1.9"),
    v1_10 (4, "1.10"),
    v1_11 (5, "1.11"),
    v1_12 (6, "1.12"),
    v1_13 (7, "1.13"),
    v1_14 (8, "1.14"),
    v1_15 (9, "1.15"),
    v1_16 (10, "1.16"),
    v1_17 (11, "1.17"),
    v1_18 (12, "1.18"),
    v1_19 (13, "1.19"),
    v1_20_1 (14, "1.20.1"),
    v1_20_2 (15, "1.20.2"),
    v1_20_3 (16, "1.20.3"),
    v1_20_4 (17, "1.20.4"),
    v1_20_5 (18, "1.20.5"),
    v1_20_6 (19, "1.20.6"),
    v1_21 (20, "1.21"),
    v1_21_1 (20, "1.21.1"),
    UNKNOWN (999, "UNKNOWN");

    private final int versionNumber;
    private final String text;

    Version(int i, String text) {
        versionNumber = i;
        this.text = text;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String toString() {
        return text;
    }

}
