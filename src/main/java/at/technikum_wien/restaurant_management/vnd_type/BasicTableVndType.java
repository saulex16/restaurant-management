package at.technikum_wien.restaurant_management.vnd_type;

public class BasicTableVndType extends TableVndType {

    private final static String BASIC = "basic";

    @Override
    protected static String getTableType() {
        return BASIC;
    }
}
