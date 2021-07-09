package com.hsbc.data.message;

/**
 * @program: FileOperateMessages
 * @description: File Operate Messages
 * @author: wangjx
 * @create: 2021-07-07 22:06
 **/
public enum InputMesages {

    /**********输入提示信息枚举************/
    PROMPT_INFO_DEVIDING_LINE(false, 1000, "----------"),
    PROMPT_INFO_WELCOME(false, 1001, "Thank you for using my little program."),
    PROMPT_INFO_TYPE_SELECT(false, 1002, "Please select your input type first. 1 for colsole input and 2 for file " +
                                   "input. (1 or 2?) :"),
    PROMPT_INFO_INPUT(false, 1003, "Please input according to \"currency amount\" format, separate by one space"),
    PROMPT_INFO_FILENAME(false, 1004, "Please input the filename to load."),
    PROMPT_INFO_TYPE_SELECT_ERROR(false, 1005, "Input Error, Only 1 or 2 is permitted."),
    PROMPT_INFO_CRRENCY_FORMAT_ERROR(false, 1006, "Please use 3 large characters for short name of currency."),
    PROMPT_INFO_AMOUNT_FORMAT_ERROR(false, 1007, "The amount should be numeric."),
    PROMPT_INFO_AMOUNT_INPUT_ERROR(false, 1008, "You should input according to the \"currency amount\" format, " +
                            "separate by one space.");


    //响应是否成功
    private Boolean status;
    //返回码
    private Integer code;
    //返回消息
    private String message;

    InputMesages(Boolean status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
