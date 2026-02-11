package generadorclaves;

import java.io.Serializable;
public class Packet implements Serializable{
    byte[] message;
    byte[] hash;
    public Packet(byte[] m, byte[] k){
        message = m;
        hash = k;
    }

    public byte[] getMessage() {
        return message;
    }

    public byte[] getHash() {
        return hash;
    }
}
