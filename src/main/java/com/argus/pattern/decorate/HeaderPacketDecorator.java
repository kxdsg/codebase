package com.argus.pattern.decorate;

/**
 * Created by xingding on 18/3/24.
 */
public class HeaderPacketDecorator extends PacketDecorator {
    public HeaderPacketDecorator(IPacket packet){
        super(packet);
    }

    @Override
    public String handleContent() {
        StringBuffer sb = new StringBuffer();
        sb.append("header\n");
        sb.append(packet.handleContent());
        return sb.toString();
    }
}
