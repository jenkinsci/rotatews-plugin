package hudson.plugins.rotatews;

import java.io.IOException;


import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;

public class Rotate extends Notifier implements SimpleBuildStep {

    @DataBoundConstructor
    public Rotate() {}

	@Override
	public boolean needsToRunAfterFinalized() {
		return true;
	}

	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Override
	public void perform(Run<?, ?> build, FilePath ws, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
		String name = ws.getName();
		FilePath target = ws.getParent().child(name + "-" + build.getNumber());
		ws.renameTo(target);
		build.addAction(new WorkspaceBrowser(target));
	}

	@Extension
	public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		@Override
		public String getDisplayName() {
			return "Workspace Rotation";
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

	}
}