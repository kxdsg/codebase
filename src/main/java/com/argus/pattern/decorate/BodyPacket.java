package com.argus.pattern.decorate;

/**
 * 核心功能实现
 * Created by xingding on 18/3/24.
 */
public class BodyPacket implements IPacket {
    @Override
    public String handleContent() {
        return "body";
    }
}
