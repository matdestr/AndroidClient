package be.kdg.teame.kandoe.data.websockets;

import be.kdg.teame.kandoe.data.websockets.stomp.Stomp;

public interface StompService {
    void onConnected(Stomp client);
}
