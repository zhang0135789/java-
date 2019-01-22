package com.zz.util;

/**
 * Created by Clare lau (Clarelau61803@gmail.com) on 2018/6/28 0028.
 */
public class ConfigUtils {

    public static int getParameterForInteger(String param, int defaultValue) {
        try {
            if (StringUtils.isNotEmpty(param)
                    && ValidateUtils.isNum(param)) {
                defaultValue = Integer.parseInt(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return defaultValue;
    }
    public static double getParameterForDouble(String param, double defaultValue) {
        try {
            if (StringUtils.isNotEmpty(param)
                    && ValidateUtils.isNum(param)) {
                defaultValue = Double.parseDouble(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return defaultValue;
    }

    public static int getParameterForInteger(String param) {
        throw new RuntimeException("missing default param value.");
    }

    public static double getParameterForDouble(String param) {
        throw new RuntimeException("missing default param value.");
    }

    public static String getIpParameter(String param) throws Exception {
        if (!ValidateUtils.isIp(param)) {
            throw new RuntimeException("invalid format ip param value.");
        }
        return param;
    }

    public static String getPortParameter(String param) throws Exception {
        if (!ValidateUtils.isPort(param)) {
            throw new RuntimeException("invalid format port param value.");
        }
        return param;
    }
}
