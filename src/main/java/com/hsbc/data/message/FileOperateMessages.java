package com.hsbc.data.message;

/**
 * @program: FileOperateMessages
 * @description: File Operate Messages
 * @author: wangjx
 * @create: 2021-07-07 22:13
 **/
public enum FileOperateMessages {

    /**********聊天模块枚举************/
    OUT_PUT_FORMAT(false, 2000, "CUR AMOUNT"),
    FILE_NOT_EXIST(false, 2001, "Input error,file not exist."),
    FILE_CURRENCY_ERROR(false, 2002, "Currency type error in line "),
    FILE_AMOUNT_FORMAT_ERROR(false, 2003, "The amount should be numeric in line "),
    FILE_AMOUNT_INPUT_ERROR(false, 2004, "There's no currency amount in line ");


    //响应是否成功
    private Boolean status;
    //返回码
    private Integer code;
    //返回消息
    private String message;

    FileOperateMessages(Boolean status, Integer code, String message) {
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
