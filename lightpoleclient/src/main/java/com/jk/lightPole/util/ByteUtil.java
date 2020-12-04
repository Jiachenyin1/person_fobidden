package com.jk.lightPole.util;

import java.nio.ByteBuffer;

public class ByteUtil {

    public static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }

    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];

        int i = 0;
        for (byte b : bytesPrim)
            bytes[i++] = b; // Autoboxing

        return bytes;
    }

    public static byte[] merger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static byte[] mergerMore(byte[]... args) {
        byte[] bt3 = null;

        for (byte[] b : args) {

            if (null == bt3)
                bt3 = b;
            else {
                byte[] temp = new byte[bt3.length + b.length];
                System.arraycopy(bt3, 0, temp, 0, bt3.length);
                System.arraycopy(b, 0, temp, bt3.length, b.length);
                bt3 = temp;
            }

        }

        return bt3;
    }

    public static String Byte2HexString(byte[] b) {
        String a = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);

            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            a = a + hex;
        }

        return a;
    }

    /**
     * 截取byte数组
     *
     * @param source     数据源
     * @param startIndex 起始位置
     * @param length     截取长度
     * @return
     */
    public static byte[] freeInterception(byte[] source, int startIndex, int length) {
        byte[] result = new byte[length];
        System.arraycopy(source, startIndex, result, 0, length);
        return result;
    }

    public static byte[] freeInterception(byte[] source, int startIndex) {
        byte[] result = new byte[source.length - startIndex];
        System.arraycopy(source, startIndex, result, 0, source.length - startIndex);
        return result;
    }

    /// 1、byte与int转换
    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        // Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

    // 2、byte[]与int转换
    public static int byteArrayToInt(byte[] b) throws Exception {
        if (b.length < 4) {

            switch (b.length) {
                case 0:
                    throw new Exception("no byte to int!");
                case 1:
                    b = merger(new byte[]{0x00, 0x00, 0x00}, b);
                    break;
                case 2:
                    b = merger(new byte[]{0x00, 0x00}, b);
                    break;
                case 3:
                    b = merger(new byte[]{0x00}, b);
                    break;
                default:
                    break;
            }
        }
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }

    public static int byteArrayToInt1(byte[] b) {
        return b[0] & 0xFF;
    }

    /*
     * public static byte[] intToByteArray(int a,int lowLength) { return new
     * byte[]{(byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8)
     * & 0xFF), (byte) (a & 0xFF)};
     *
     * }
     */

    public static byte[] intToByteArray(int a, int size) {
        switch (size) {
            case 1:
                return new byte[]{(byte) (a & 0xFF)};
            case 2:
                return new byte[]{(byte) ((a >> 8) & 0xFF), (byte) (a & 0xFF)};
            case 3:
                return new byte[]{(byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF), (byte) (a & 0xFF)};
            default:
                return new byte[]{(byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF),
                        (byte) (a & 0xFF)};
        }

    }

    public static byte[] toByteArray(String arg) {
        if (arg != null) {
            /* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
            /* 将char数组中的值转成一个实际的十进制数组 */
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
                /* 将 每个char的值每两个组成一个16进制数据 */
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[]{};
    }

    // byte 数组与 long 的相互转换
    public static byte[] longToBytes(long x) {

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();// need flip
        return buffer.getLong();
    }

    // char转换为byte[2]数组
    public static byte[] getByteArray(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xff00) >> 8);
        b[1] = (byte) (c & 0x00ff);
        return b;
    }

    // 从byte数组的index处的连续两个字节获得一个char
    public static char getChar(byte[] arr, int index) {
        return (char) (0xff00 & arr[index] << 8 | (0xff & arr[index + 1]));
    }

    // short转换为byte[2]数组
    public static byte[] getByteArray(short s) {
        byte[] b = new byte[2];
        b[0] = (byte) ((s & 0xff00) >> 8);
        b[1] = (byte) (s & 0x00ff);
        return b;
    }

    // 从byte数组的index处的连续两个字节获得一个short
    public static short getShort(byte[] arr, int index) {
        return (short) (0xff00 & arr[index] << 8 | (0xff & arr[index + 1]));
    }

    // int转换为byte[4]数组
    public static byte[] getByteArray(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) ((i & 0xff000000) >> 24);
        b[1] = (byte) ((i & 0x00ff0000) >> 16);
        b[2] = (byte) ((i & 0x0000ff00) >> 8);
        b[3] = (byte) (i & 0x000000ff);
        return b;
    }

    // 从byte数组的index处的连续4个字节获得一个int
    public static int getInt(byte[] arr, int index) {
        return (0xff000000 & (arr[index + 0] << 24)) |
                (0x00ff0000 & (arr[index + 1] << 16)) |
                (0x0000ff00 & (arr[index + 2] << 8)) |
                (0x000000ff & arr[index + 3]);
    }

    // float转换为byte[4]数组
    public static byte[] getByteArray(float f) {
        int intbits = Float.floatToIntBits(f);//将float里面的二进制串解释为int整数
        return getByteArray(intbits);
    }

    // 从byte数组的index处的连续4个字节获得一个float
    public static float getFloat(byte[] arr, int index) {
        return Float.intBitsToFloat(getInt(arr, index));
    }

    // long转换为byte[8]数组
    public static byte[] getByteArray(long l) {
        byte b[] = new byte[8];
        b[0] = (byte) (0xff & (l >> 56));
        b[1] = (byte) (0xff & (l >> 48));
        b[2] = (byte) (0xff & (l >> 40));
        b[3] = (byte) (0xff & (l >> 32));
        b[4] = (byte) (0xff & (l >> 24));
        b[5] = (byte) (0xff & (l >> 16));
        b[6] = (byte) (0xff & (l >> 8));
        b[7] = (byte) (0xff & l);
        return b;
    }

    // 从byte数组的index处的连续8个字节获得一个long
    public static long getLong(byte[] arr, int index) {
        return (0xff00000000000000L & ((long) arr[index + 0] << 56)) |
                (0x00ff000000000000L & ((long) arr[index + 1] << 48)) |
                (0x0000ff0000000000L & ((long) arr[index + 2] << 40)) |
                (0x000000ff00000000L & ((long) arr[index + 3] << 32)) |
                (0x00000000ff000000L & ((long) arr[index + 4] << 24)) |
                (0x0000000000ff0000L & ((long) arr[index + 5] << 16)) |
                (0x000000000000ff00L & ((long) arr[index + 6] << 8)) |
                (0x00000000000000ffL & (long) arr[index + 7]);
    }

    // double转换为byte[8]数组
    public static byte[] getByteArray(double d) {
        long longbits = Double.doubleToLongBits(d);
        return getByteArray(longbits);
    }

    // 从byte数组的index处的连续8个字节获得一个double
    public static double getDouble(byte[] arr, int index) {
        return Double.longBitsToDouble(getLong(arr, index));
    }

    public static double ieee754ToDouble(String hexStr) {
        StringBuffer binaryStr = new StringBuffer();
        for (int i = 0; i < hexStr.length(); i += 2) {
            String a = hexStr.substring(i, i + 2);
            int c = Integer.parseInt(a, 16);
            String item = String.format("%08d", Integer.parseInt(Integer.toBinaryString(c)));
            binaryStr.append(item);
        }
        int n = Integer.parseInt(binaryStr.substring(1, 9), 2);
        String mStr = binaryStr.substring(9, binaryStr.length() - 1);
        double sum = 0;
        for (int i = 1; i <= mStr.length(); i++) {
            if (mStr.charAt(i - 1) == '1') {
                sum = sum + Math.pow(0.5, i);
            }
        }
        double a = (Math.pow(2, n - 127)) * (1 + sum);
        return a;
    }

    /**
     * 单精度浮点数16进制转换
     * 保证精度前提下最长7位(包含小数位)
     *
     * @param hexStr
     * @return
     */
    public static float ieee754ToFloat(String hexStr) {
        return Float.intBitsToFloat(Integer.parseInt(hexStr, 16));
    }

    /**
     * 字节数组转int,适合转高位在前低位在后的byte[]
     *
     * @param bytes
     * @return
     */
    public static long byteArrayToLong(byte[] bytes) {
        long result = 0;
        int len = bytes.length;
        if (len == 1) {
            byte ch = (byte) (bytes[0] & 0xff);
            result = ch;
        } else if (len == 2) {
            int ch1 = bytes[0] & 0xff;
            int ch2 = bytes[1] & 0xff;
            result = (short) ((ch1 << 8) | (ch2 << 0));
        } else if (len == 4) {
            int ch1 = bytes[0] & 0xff;
            int ch2 = bytes[1] & 0xff;
            int ch3 = bytes[2] & 0xff;
            int ch4 = bytes[3] & 0xff;
            result = (int) ((ch1 << 24) | (ch2 << 16) | (ch3 << 8) | (ch4 << 0));
        } else if (len == 8) {
            long ch1 = bytes[0] & 0xff;
            long ch2 = bytes[1] & 0xff;
            long ch3 = bytes[2] & 0xff;
            long ch4 = bytes[3] & 0xff;
            long ch5 = bytes[4] & 0xff;
            long ch6 = bytes[5] & 0xff;
            long ch7 = bytes[6] & 0xff;
            long ch8 = bytes[7] & 0xff;
            result = (ch1 << 56) | (ch2 << 48) | (ch3 << 40) | (ch4 << 32) | (ch5 << 24) | (ch6 << 16) | (ch7 << 8) | (ch8 << 0);
        }
        return result;
    }

    /**
     * int转byte[]，高位在前低位在后
     *
     * @param value
     * @return
     */
    public static byte[] varIntToByteArray(long value) {
        Long l = new Long(value);
        byte[] valueBytes = null;
        if (l == l.byteValue()) {
            valueBytes = toBytes(value, 1);
        } else if (l == l.shortValue()) {
            valueBytes = toBytes(value, 2);
        } else if (l == l.intValue()) {
            valueBytes = toBytes(value, 4);
        } else if (l == l.longValue()) {
            valueBytes = toBytes(value, 8);
        }
        return valueBytes;
    }

    private static byte[] toBytes(long value, int len) {
        byte[] valueBytes = new byte[len];
        for (int i = 0;i < len;i++) {
            valueBytes[i] = (byte) (value >>> 8 * (len - i - 1));
        }
        return valueBytes;
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(byteArrayToInt(toByteArray("e3")));
        System.out.println(Byte2HexString(intToByteArray(-2, 2)));
        System.out.println(byteArrayToLong(toByteArray("fffe")));
//        byte[] data = longToBytes(1);
//        System.out.println(data);
//        System.out.println(new BigDecimal(ieee754ToDouble("BF800000") + "").toPlainString());
//        System.out.println(getFloat(new byte[]{(byte) 0xc7, (byte) 0xc3, 0x4f, (byte) 0xff}, 0));
//        System.out.println(new BigDecimal(Double.longBitsToDouble(Integer.parseInt("411FD70A",16))+"").toPlainString());
        System.out.println(byteArrayToInt(toByteArray("fffe")));
//        System.out.println(Float.intBitsToFloat(Integer.parseInt("C11FD70A", 16)));
//        System.out.println(Byte2HexString(new byte[]{0x0a, (byte) 0xd7, 0x1f, (byte) 0xc1}));
    }

}