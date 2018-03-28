package com.argus.pattern.decorate;

/**
 * 装饰者，持有被装饰者IPacket
 * Created by xingding on 18/3/24.
 */
public abstract class PacketDecorator implements IPacket {
    IPacket packet;

    public PacketDecorator(IPacket packet) {
        this.packet = packet;
    }
}
