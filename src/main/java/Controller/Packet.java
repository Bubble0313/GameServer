package Controller;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Packet implements Serializable {
    private MessageType type;
    private byte[] payload;
    private static final long serialVersionUID = 1L;

    public Packet(MessageType type, byte[] payload) {
        this.type = type;
        this.payload = payload;
    }
}
