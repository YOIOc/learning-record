package org.example.shardingjdbc.responses;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Result<T> {
    private Integer code; //状态码(0：操作成功，1：操作失败)
    private String message; // 提示信息
    private T data; // 响应数据

    // 操作成功(带有响应数据)，<T>的作用是声明此时的泛型T是方法级别的泛型
    public static <T> Result<T> succeed(T data){
        return new Result<T>(0, "操作成功", data);
    }

    // 操作成功(无响应数据)
    public static Result succeed(){
        return new Result(0, "操作成功", null);
    }

    // 操作失败
    public static Result error(String message){
        return new Result(1, "操作失败", message);
    }
}
