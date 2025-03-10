package at.technikum_wien.restaurant_management.vnd_type;

public class VndType {

    private static final String PREFIX = "application/vnd.";
    private static final String FORMAT = "+json";
    private static final String VERSION = ".v1";

    private static final String TABLE = "table.";
    public static final String VIP_TABLE_VND_TYPE = PREFIX + TABLE + "vip" + VERSION + FORMAT;
    public static final String BASIC_TABLE_VND_TYPE = PREFIX + TABLE + "basic" + VERSION + FORMAT;
}
