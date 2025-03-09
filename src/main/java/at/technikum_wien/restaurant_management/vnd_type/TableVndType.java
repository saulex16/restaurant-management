package at.technikum_wien.restaurant_management.vnd_type;

public abstract class TableVndType extends BaseVndType {

    private final static String BASE = "table";

    protected abstract static String getTableType();

    @Override
    protected static String getObjectType() {
        final String tableType = this.getTableType();
        StringBuilder builder = new StringBuilder();
        builder
                .append(BASE)
                .append(".")
                .append(tableType);
        return builder.toString();
    }
}
