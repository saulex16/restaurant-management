package at.technikum_wien.restaurant_management.vnd_type;

public class VndType {

    private static final String PREFIX = "application/vnd.";
    private static final String FORMAT = "+json";
    private static final String VERSION = ".v1";

    private static final String TABLE = "table.";
    public static final String VIP_TABLE_VND_TYPE = PREFIX + TABLE + "vip" + VERSION + FORMAT;
    public static final String BASIC_TABLE_VND_TYPE = PREFIX + TABLE + "basic" + VERSION + FORMAT;

    private static final String ORDER = "order.";
    public static final String ORDER_QUEUE_VND_TYPE = PREFIX + ORDER + "queue" + VERSION + FORMAT;
    public static final String ORDER_DELIVER_VND_TYPE = PREFIX + ORDER + "deliver" + VERSION + FORMAT;
    public static final String ORDER_BILL_VND_TYPE = PREFIX + ORDER + "bill" + VERSION + FORMAT;
}
