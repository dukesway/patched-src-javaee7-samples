package org.javaee7.websocket.endpoint.programmatic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

/**
 * @author Arun Gupta
 */
@ClientEndpoint
public class MyEndpointBinaryClient {
    public static CountDownLatch latch;
    public static byte[] response;

    @OnOpen
    public void onOpen(Session session) {
        try {
            latch.countDown();
            session.getBasicRemote().sendBinary(ByteBuffer.wrap("Hello World!".getBytes()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
