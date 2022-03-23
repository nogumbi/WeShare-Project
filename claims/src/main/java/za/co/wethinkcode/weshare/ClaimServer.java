package za.co.wethinkcode.weshare;

import io.javalin.Javalin;

public class ClaimServer {
    private final Javalin server;

    public ClaimServer() {
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json";
        });

        // Claims endpoint
        this.server.get("/claim/{id}", context -> ClaimsApiHandler.getOne(context));
        this.server.post("/claim", context -> ClaimsApiHandler.create(context));
        this.server.put("/claim",  context -> ClaimsApiHandler.update(context));

        // Settlement enpoint
        this.server.post("/settlement", context -> ClaimsApiHandler.settle(context));
    }

    public static  void main(String[] args){
        ClaimServer server = new ClaimServer();
        server.start(7070);
    }

    public void start(int port) {
        this.server.start(port);
    }

    public  void  stop(){
        this.server.stop();
    }
}
