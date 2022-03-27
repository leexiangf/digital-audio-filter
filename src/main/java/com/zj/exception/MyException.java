package com.zj.exception;

/**
 * @ClassName : MyException
 * @Description TODO
 * @Date 2022/3/25 0:27
 * @Created lxf
 */
public class MyException  extends  Exception{


    private long code;
    private String msg;

    public MyException(Long code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MyException() {
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyException(Throwable cause) {
        super(cause);
    }

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
