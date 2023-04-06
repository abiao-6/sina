package com.abiao.sina.entity;

import lombok.Data;

import java.util.List;

@Data
public class ReponseArr<T> {
    private List<T> data;
}
