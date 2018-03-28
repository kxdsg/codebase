package com.argus.pattern.decorate;

/**
 * Created by xingding on 18/3/24.
 */
public class Main {
    public static void main(String[] args) {
        IPacket packet = new FooterPacketDecorator(new HeaderPacketDecorator(new BodyPacket()));
        System.out.println(packet.handleContent());
    }
}
