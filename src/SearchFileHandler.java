import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class SearchFileHandler implements HttpHandler {

    private Project project;

    public SearchFileHandler(Project project) {
        this.project = project;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());

        ApplicationManager.getApplication().runReadAction(new Runnable() {
            @Override
            public void run() {
                final PsiFile[] files = params.containsKey("q") ?
                        PsiShortNamesCache.getInstance(project).getFilesByName(params.get("q")) :
                        new PsiFile[]{};

                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (files.length > 0) {
                            OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, files[0].getVirtualFile(), 0, 0);
                            openFileDescriptor.navigate(true);
                            WindowManagerEx.getInstance().findVisibleFrame().requestFocus();
                        }
                    }
                });
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
