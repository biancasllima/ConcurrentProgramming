package com.edu.ufcg.question5.impl;


import com.edu.ufcg.question5.interfaces.Channel;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChannelImpl implements Channel {

    private BlockingQueue<String> channelBuffer;

    public ChannelImpl(int buffer) {
        boolean isFifo = true;
        this.channelBuffer = new ArrayBlockingQueue<String>(buffer, isFifo);
    }

    public void putMessage(String message) {
        if (message != null){
            channelBuffer.offer(message);
        }
    }

    public String takeMessage() {
        if (!channelBuffer.isEmpty()) {
            try {
                return channelBuffer.take();
            } catch (InterruptedException e) {
                // should not happen
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}
