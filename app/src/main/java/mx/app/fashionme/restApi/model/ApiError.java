package mx.app.fashionme.restApi.model;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by heriberto on 14/03/18.
 */

public class ApiError {

    private int code;
    private String error;
    private String message;

    public ApiError(int code, String error, String message) {
        this.code = code;
        this.error = error;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ApiError fromResponseBody (ResponseBody responseBody) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(responseBody.string(), ApiError.class);
        } catch (IOException | JsonIOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
