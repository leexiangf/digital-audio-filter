package com.zj.result;

import com.zj.enumc.ResultCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @ClassName : JsonResult
 * @Description mvc全局返回
 * @Date 2022/3/24 23:58
 * @Created lxf
 */
public class JsonResult<T>  implements Serializable {

    private long code;

    private String message;

    private T data;

    public JsonResult() {
        this.setCode(ResultCode.SUCCESS);
        this.setMessage("成功！");
    }

    public JsonResult(ResultCode code) {
        this.setCode(code);
        this.setMessage(code.msg());
    }

    public JsonResult(ResultCode code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public JsonResult(long code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }



    public JsonResult(ResultCode code, String message, T data) {

        this.setCode(code);
        this.setMessage(message);
        this.setData(data);

    }

    public JsonResult(ResultCode code, T data) {

        this.setCode(code);
        this.setMessage(code.msg());
        this.setData(data);

    }

    public String toString() {

        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("message", message);
            json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    public long getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code.val();
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
