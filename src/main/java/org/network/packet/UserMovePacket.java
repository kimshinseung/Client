package org.network.packet;

import java.awt.*;
import java.awt.geom.Point2D;

public class UserMovePacket extends Packet{
    public Point direction;
    public UserMovePacket(int id, String username,Point direction) {
        super(id, PacketType.USER_MOVE, username);
        this.direction = direction;
    }
}
