package be.kdg.teame.kandoe.models.cards;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardPosition {
    private CardDetails cardDetails;
    private int priority;
}
