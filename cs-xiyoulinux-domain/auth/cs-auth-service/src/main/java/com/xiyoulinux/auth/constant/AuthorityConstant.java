package com.xiyoulinux.auth.constant;

/**
 * 授权需要使用的一些常量信息
 *
 * @author qkm
 */
public final class AuthorityConstant {

    /**
     * RSA 私钥, 除了授权中心以外, 不暴露给任何客户端
     */
    public static final String PRIVATE_KEY = "MIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgE" +
            "AAoIBAQCYxndpR1y0foUX78lxzpt2M6KCUFmwHkRgJXLF7Eww0FL6CB2sZ4GIky7N0WMnD7UjfILa" +
            "YW639p32cpqOHAIFeGPaHvfobPArMke3xL2NeAzziq2YPzn2plvGHwDa4Gb5BOdRsE2ggXN4f+97S" +
            "Sk3FR/vsFGnpQPczmG23PBXxEVL3bYaFda6GuLSf2lJwnjJ98sOhXTfbkvx2tVs2KWcuLpm9t6z9C" +
            "Fmzm1Ju2cMKeg+jRO6Z5xbWGuuzo2FhYZRGJj9OV+IG6t876WYIsGudwAyykT2sLcMuZ8uns2SnyGx" +
            "I88PFdysCbcWIfVh9Y0PGs5PZAlXWQ7F6SG0mHaXAgMBAAECgf9Ac3ns8C4r9zQu5PoXCRKMyzvDviI" +
            "YKqyt4x6IYSfDcB+RW9so4IHFHFKigbNSx02jquOJPZF4CpKZZb/ONAKmTCPT4Ty00UCA+LVI0bcl93" +
            "9/PwH9OK87XvNYobu6mvDWHTt5OueEzxfnvhFJ8ZNxqXfPppLMJtF6VZbjjlHWtE45s2hOE66FmYWpw" +
            "ZIA3i7P/JRAjLXlPu1l14bH/Jff5Gf5muGqwdkrUNOmkj8ldEc3a4KGiqAa+NIHhsDVnwVehWMsOGhW" +
            "Ppa/OQysO1qrnAk26ryMH7EnsGYRY+qj1uYy0vypj87LJUbiXv02SR2bZk2fd04RZFqyfv58lcECgYE" +
            "A4YYrOpmy0tLXjh8spTlZVuTItpU7EtIRCCWTF0ou+ez0QOTHkM5Yx/4Jhtps3O5Ak/hJoowaUxmvE2R" +
            "xc9OQW+0X12sSLdp0xypyoU0WxGWT6OztE3IkqBgrKVVuZabOvf4V38Sa+NwQHg96P3r4JYl4GbgwAUb" +
            "+6jBnDnQYyJkCgYEArWubpieI+QCrhdr1xKJVultM93adbzgibPBh/wdfTTNbg04Cm3ltFzaaPjfYQ6z" +
            "XuvsgiQODcmPNE4vpd6eWsYZGEuOs7fyy63QerzUL7OyHV7Cka60srRvzEt5uEoQYl8HuriNA4vhhui" +
            "PvHy/R9f+sZlCjnCGpvBN+EOwpxq8CgYEAtz4O7eRomkhagM3vtqgsYSAvvbrvbtFkqVvuciQCm2ve4" +
            "sOBK7WeCZHuJ7ZecfzcV96L5hk0YoUh7f3U+SnTx/2TO6Nx9/PiotlonotGjnmCfuXTk9NQ2YwZtdI" +
            "Gi23H2jQTjefvi3kzV1l4GRBPhCB4h11TFBadlaeSdPn8ETECgYAbJDlTEuL0Ha+6nl9SrBmAtiNgo5" +
            "bWPlHc/O5JbMz0lzIeZcowrRtuw6P2UuUtClMlN4KLDxq2f3osMfyTNo52ME6tTCbXhHI4OY9H2qSIKy" +
            "ivh7zkAVF5smMjs95EVZpHvhlt9aSFIVyiNleJaQGSX7aKuNoyaG5ZOi/D1EJhjwKBgAUonGOzn4ssY2" +
            "pSV87+ir+ccVYy8ukx9khAk7Gz+IvdokKLwTbiCxErV1LCFYOaFrqJ4ZrezaVIPZrU5knLClbkMBPIpI" +
            "ZSdh3KLrwu4Ux8+/78rttnf/YQDhreLU6F87Wvn8SMiVZgYP6wq+tV89TkrijYdW5RY0zldfcOgazH";

    /**
     * 默认的 refresh Token 超时时间, 一天
     */
    public static final long DEFAULT_REFRESH_EXPIRE_DAY = 6 * 3600 * 1000;


    public static final long DEFAULT_EXPIRE_DAY = 2 * 3600 * 1000;
}
