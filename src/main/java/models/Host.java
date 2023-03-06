package models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Host {
    String ip;
    int port;

    public Host(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
