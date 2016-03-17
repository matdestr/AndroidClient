package be.kdg.teame.kandoe.session.game;

import java.util.List;

import be.kdg.teame.kandoe.models.cards.CardPosition;

public interface DataListener {
    void onReceiveData(List<CardPosition> cardPositions);
}
