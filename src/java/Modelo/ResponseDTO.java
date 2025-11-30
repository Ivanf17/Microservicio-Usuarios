/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author flori
 */

public class ResponseDTO {
    private boolean success;
    private String message;
    private Object data;
    private String error;

    public ResponseDTO() {
    }

    public ResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseDTO(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ResponseDTO success(String message) {
        return new ResponseDTO(true, message);
    }

    public static ResponseDTO success(String message, Object data) {
        return new ResponseDTO(true, message, data);
    }

    public static ResponseDTO error(String message) {
        ResponseDTO response = new ResponseDTO(false, message);
        response.setError(message);
        return response;
    }

    public static ResponseDTO error(String message, String error) {
        ResponseDTO response = new ResponseDTO(false, message);
        response.setError(error);
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
