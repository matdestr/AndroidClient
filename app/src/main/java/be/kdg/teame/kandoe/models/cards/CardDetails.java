package be.kdg.teame.kandoe.models.cards;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardDetails {
    private int cardDetailsId;
    private String text;
    private String imageUrl;
    private boolean active;
}
