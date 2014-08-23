import com.intellij.openapi.components.ApplicationComponent;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerComponent implements ApplicationComponent {
    private OpenHandler openHandler;

    public ServerComponent() {
        this.openHandler = new OpenHandler();
    }

    public void initComponent() {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/open", openHandler);
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "ServerComponent";
    }

    public void addSearcher(Searcher searcher) {
        openHandler.addSearcher(searcher);
    }
}
