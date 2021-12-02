package cn.itcast.wanxinp2p.transaction.common.constant;

/**
 * 标的 标的类型 枚举类
 */
public enum ProjectTypeCode {

    /**
     * 新增标
     */
    TYPE_CODE_NEW("NEW", "新增标"),

    /**
     * 存量标
     */
    TYPE_CODE_STOCK("STOCK", "存量标");

    private String code;

    private String desc;

    ProjectTypeCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
