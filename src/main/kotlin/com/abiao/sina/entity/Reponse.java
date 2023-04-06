package com.abiao.sina.entity;

import lombok.Data;

@Data
public class Reponse<T> {
    private T data;
}
