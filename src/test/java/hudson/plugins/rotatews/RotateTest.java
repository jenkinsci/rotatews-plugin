package hudson.plugins.rotatews;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;

public class RotateTest {

    @Rule public JenkinsRule r = new JenkinsRule();
    
    @Test public void workflow() throws Exception {
        WorkflowJob p = r.jenkins.createProject(WorkflowJob.class, "p");
        p.setDefinition(new CpsFlowDefinition("node {writeFile file: 'f.txt', text: 'OK'; step([$class: 'Rotate'])}"));
        WorkflowRun b = r.assertBuildStatusSuccess(p.scheduleBuild2(0));
        WorkspaceBrowser browser = b.getAction(WorkspaceBrowser.class);
        assertNotNull(browser);
        assertEquals(b, browser.getParent());
        assertTrue(browser.isAvailable());
        assertEquals("p-1", browser.buildWorkspace.getName());
        assertEquals("OK", r.createWebClient().goTo(b.getUrl() + browser.getUrlName() + "/f.txt", "text/plain").getWebResponse().getContentAsString());
    }

}
