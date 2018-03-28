package com.argus.pattern.decorate;

/**
 * Created by xingding on 18/3/24.
 */
public class FooterPacketDecorator extends PacketDecorator {

    public FooterPacketDecorator(IPacket packet) {
        super(packet);
    }

    @Override
    public String handleContent() {
        StringBuffer sb = new StringBuffer();
        sb.append(packet.handleContent());
        sb.append("\nfooter");
        return sb.toString();
    }
}
