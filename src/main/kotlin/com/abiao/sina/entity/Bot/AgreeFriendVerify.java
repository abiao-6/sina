package com.abiao.sina.entity.Bot;

import lombok.Data;

@Data
public class AgreeFriendVerify extends Bot{
    private String v1;
    private String v2;
    private Integer type;
}
