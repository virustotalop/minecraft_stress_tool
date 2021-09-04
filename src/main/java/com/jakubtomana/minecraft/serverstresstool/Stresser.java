package com.jakubtomana.minecraft.serverstresstool;

import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.ClientListener;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.tcp.TcpClientSession;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Stresser {

    //Final variables
    private final int threadsNum;
    private final String serverAddress;
    private final int port;
    private final String nick;
    private final int delay;
    private final ScheduledExecutorService pool;

    /**
     * Creates new stresser object (with register and login)
     */
    public Stresser(String serverAdress, int port, int threadsNum, String nick, int delay) {
        this.serverAddress = serverAdress;
        this.port = port;
        this.nick = nick;
        this.threadsNum = threadsNum;
        this.delay = delay;
        this.pool = Executors.newScheduledThreadPool(this.threadsNum);
    }

    /**
     * Start stressing server
     */
    public void startStressTest() {
        for (int i = 0; i < threadsNum; i++) {
            System.out.println("Started new bot:" + i);
            joinserver(i);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void joinserver(int id) {
        String username = this.nick + id;
        SessionService sessionService = new SessionService();
        sessionService.setProxy(Proxy.NO_PROXY);
        MinecraftProtocol protocol = new MinecraftProtocol(username);
        System.out.println(this.serverAddress + ":" + this.port);
        Session client = new TcpClientSession(this.serverAddress, this.port, protocol);
        client.connect();
        protocol.newClientSession(client);
    }
}