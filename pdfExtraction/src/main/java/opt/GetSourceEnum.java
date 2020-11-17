package opt;

public enum GetSourceEnum {
    GET(0, "资料来源在下页面"),

    NOGET(1, "资料来源不在下页面");

    private int code;
    private String name;

    GetSourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static GetSourceEnum valueOfCode(Integer code) throws Exception {
        for (GetSourceEnum e : GetSourceEnum.values()
                ) {
            if( e.getCode().equals(code)) {
                return e;
            }
        }
        throw new Exception("There is no match Enum. Enum code: " + code);
    }
}

