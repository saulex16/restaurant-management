package at.technikum_wien.restaurant_management.vnd_type;

public class VipTableVndType extends TableVndType {

    private final static String VIP = "vip";

    @Override
    protected static String getTableType() {
        return VIP;
    }
}
