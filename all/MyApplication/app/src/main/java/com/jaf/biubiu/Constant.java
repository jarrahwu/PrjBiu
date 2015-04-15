package com.jaf.biubiu;

/**
 * Created by jarrah on 2015/4/14.
 */
public interface Constant {

    public interface BottomTabBar {
        public static final int TAB_COUNT = 4;
        public static final int FIRST_TAB = R.id.tab0;
        public static final int TAB_TITLES[] = new int[] {
                R.string.nearby, R.string.topic, R.string.message, R.string.me
        };
    }

    public static final String API = "http://183.131.76.109/cgi/user_svc.php";
    public static final String VER = "2.1";

    public interface CMD {
        public static final int USER_REG = 0x1001;
        public static final int USER_INFO = 0x1004;
        public static final int USER_LOC = 0x1005;

        public static final int LIST_NEARBY = 0X2004;

        public static final int REPORT_ABUSE = 0X5001;

        public static final int LIST_TOPIC = 0X2102;
    }
}
