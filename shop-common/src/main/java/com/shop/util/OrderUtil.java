package com.shop.util;

import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OrderUtil {
    /** 开始时间截 (2020-2-11) */
    private final long twepoch = 1581414602033L;

    /** 机器id所占的位数 */
    private final long workerIdBits = 5L;

    /** 数据标识id所占的位数 */
    private final long datacenterIdBits = 5L;

    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** 支持的最大数据标识id，结果是31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /** 序列在id中占的位数 */
    private final long sequenceBits = 22L;

    /** 机器ID向左移22位 */
    private final long workerIdShift = sequenceBits;

    /** 数据标识id向左移27位(22+5) */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /** 时间截向左移32位(5+5+22) */
    private final long timestampLeftShift = sequenceBits + workerIdBits
            + datacenterIdBits;

    /** 生成序列的掩码，这里为4194303 */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 工作机器ID(0~31) */
    private long workerId;

    /** 数据中心ID(0~31) */
    private long datacenterId;

    /** 秒内序列(0~4194303) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private int lastTimestamp = -1;

    // ==============================Constructors=====================================
    /**
     * 构造函数
     *
     * @param workerId
     *            工作ID (0~31)
     * @param datacenterId
     *            数据中心ID (0~31)
     */
    public OrderUtil(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format(
                    "worker Id can't be greater than %d or less than 0",
                    maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format(
                    "datacenter Id can't be greater than %d or less than 0",
                    maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        int timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，秒内序列重置
        else {
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;
        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    /**
     * 阻塞到下一个秒，直到获得新的时间戳
     *
     * @param lastTimestamp
     *            上次生成ID的时间截
     * @return 当前时间戳
     */
    protected int tilNextMillis(int lastTimestamp) {
        int timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以秒为单位的当前时间
     *
     * @return 当前时间(秒)
     */
    protected int timeGen() {
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        return Integer.parseInt(timestamp);
    }

    private static OrderUtil instance = new OrderUtil(1, 1);

    public static Long getOrderNumber() {
        return instance.nextId();
    }
}
