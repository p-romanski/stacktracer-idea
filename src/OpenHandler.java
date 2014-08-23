import com.intellij.openapi.application.ApplicationManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenHandler implements HttpHandler {

    private List<Searcher> searchers;

    public OpenHandler() {
        this.searchers = new ArrayList<Searcher>();
    }

    public void addSearcher(Searcher searcher) {
        searchers.add(searcher);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());

        if (!params.containsKey("q")) {
            return;
        }

        ApplicationManager.getApplication().runReadAction(new Runnable() {
            @Override
            public void run() {
                for (Searcher s : searchers) {
                    s.search(params.get("q"));
                }
            }
        });

        String response = "This is the response";
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
