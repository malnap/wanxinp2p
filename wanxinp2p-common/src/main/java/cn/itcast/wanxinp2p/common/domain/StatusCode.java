package cn.itcast.wanxinp2p.common.domain;

/**
 * 开户状态枚举
 */
public enum StatusCode {

    /**
     * 发/同步失败
     */
    STATUS_FAIL(2, "发/同步失败"),

    /**
     * 已发/同步
     */
    STATUS_IN(1, "已发/同步"),

    /**
     * 未发/同步
     */
    STATUS_OUT(0,"未发/同步");

    private Integer code;

    private String desc;

    StatusCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
