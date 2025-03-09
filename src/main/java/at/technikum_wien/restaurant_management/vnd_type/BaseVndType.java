package at.technikum_wien.restaurant_management.vnd_type;

public abstract class BaseVndType {

    private static final String BASE = "application/vnd.";
    private static final String FORMAT = "json";
    private static final String VERSION = "v1";

    protected abstract static String getObjectType();

    public static String getVndType() {
        final String objectType = this.getObjectType();
        StringBuilder builder = new StringBuilder();
        builder
                .append(BASE)
                .append(objectType)
                .append(".")
                .append(VERSION)
                .append("+")
                .append(FORMAT);
        return builder.toString();
    }
}
