package com.maga.admin.util;

import com.maga.admin.entity.ApiResult;
import net.sf.json.JSONObject;

/**
 * 数据接口返回信息构建
 *
 * @author zhangsl
 * @date 2018-01-24
 */
public class ApiResultBuilder {

    public static ApiResult success(String message) {
        return success(message, null);
    }

    public static ApiResult failure(String message) {
        ApiResult result = new ApiResult();
        result.setSuccess(false);
        result.setMessage(message);
        result.setData(new JSONObject());
        return result;
    }

    public static ApiResult success(String message, JSONObject jsonObject) {
        ApiResult result = new ApiResult();
        result.setSuccess(true);
        result.setMessage(message);
        if (jsonObject != null) {
            result.setData(jsonObject);
        }else{
            result.setData(new JSONObject());
        }
        return result;
    }

}
