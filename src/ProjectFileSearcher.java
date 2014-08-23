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
        String fileName = q;
        int lineNo = 0;

        if (fileName.contains(":")) {
            String[] parts = fileName.split(":");
            fileName = parts[0];
            try {
                lineNo = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        PsiFile[] files = PsiShortNamesCache.getInstance(project).getFilesByName(fileName);
        if (files.length > 0) {
            ApplicationManager.getApplication().invokeLater(new OpenFile(files[0], lineNo));
        }
    }

    private class OpenFile implements Runnable {
        private final PsiFile file;
        private final int lineNo;

        public OpenFile(PsiFile file, int lineNo) {
            this.file = file;
            this.lineNo = lineNo;
        }

        @Override
        public void run() {
            OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, file.getVirtualFile(), lineNo, 0);
            openFileDescriptor.navigate(true);
            WindowManagerEx.getInstance().findVisibleFrame().requestFocus();
        }
    }
}
