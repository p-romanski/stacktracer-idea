import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class SearchComponent implements ProjectComponent {

    private Searcher searcher;

    public SearchComponent(Project project) {
        this.searcher = new ProjectFileSearcher(project);
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "SearchComponent";
    }

    public void projectOpened() {
        ServerComponent server = ApplicationManager.getApplication().getComponent(ServerComponent.class);
        server.addSearcher(searcher);
    }

    public void projectClosed() {
        // called when project is being closed
    }
}
