import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;

public class ProjectFileSearcher implements Searcher {
    Project project;

    ProjectFileSearcher(Project project) {
        this.project = project;
    }

    @Override
    public void search(String q) {
        final PsiFile[] files = PsiShortNamesCache.getInstance(project).getFilesByName(q);

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
}
