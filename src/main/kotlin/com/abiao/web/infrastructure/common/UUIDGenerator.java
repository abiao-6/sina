//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.abiao.web.infrastructure.common;

import java.util.UUID;

public class UUIDGenerator implements IdGenerator {
    public UUIDGenerator() {
    }

    public String newId() {
        return UUID.randomUUID().toString();
    }
}
