package Operations.Histogram;

public enum TypeOfHistogram {
    RED("Red Channel"),
    GREEN("Green Channel"),
    BLUE("Blue Channel"),
    Y("Luminance (Y)"),
    V("Value (V)"),
    I("Intensity (I)");

    private final String displayName;

    TypeOfHistogram(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}