package hudson.plugins.rotatews;

import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.FilePath;
import hudson.model.BuildBadgeAction;
import hudson.model.DirectoryBrowserSupport;
import hudson.model.Item;
import hudson.model.Run;
import jenkins.model.RunAction2;

public class WorkspaceBrowser implements BuildBadgeAction, RunAction2 {
	transient Run<?, ?> parent;
	
	FilePath buildWorkspace;
	
	public WorkspaceBrowser(FilePath ws) {
		this.buildWorkspace = ws;
	}
	
	public Run<?, ?> getParent() {
		return parent;
	}
	
	public Run<?, ?> getOwner() {
		return parent;
	}
	
	public boolean isAvailable() {
		try {
			return buildWorkspace.exists();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getDisplayName() {
		return "Workspace";
	}

	public String getIconFileName() {
		return (isAvailable() ? "folder.gif" : null);
	}

	public String getUrlName() {
		return "ws";
	}

    /**
     * Serves the workspace files.
     */
    public DirectoryBrowserSupport doDynamic( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException, InterruptedException {
        parent.checkPermission(Item.WORKSPACE);
        FilePath ws = buildWorkspace;
        if ((ws == null) || (!ws.exists())) {
            // if there's no workspace, report a nice error message
            req.getView(this,"noWorkspace.jelly").forward(req,rsp);
            return null;
        } else {
            return new DirectoryBrowserSupport(parent, ws, getDisplayName()+" workspace", "folder.gif", true);
        }
    }
    @Override
    public void onAttached(Run<?, ?> r) {
        parent = r;
    }
    @Override
    public void onLoad(Run<?, ?> r) {
        parent = r;
    }

}
