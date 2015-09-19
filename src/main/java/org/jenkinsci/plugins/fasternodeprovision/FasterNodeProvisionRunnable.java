package org.jenkinsci.plugins.fasternodeprovision;

import hudson.model.Label;
import jenkins.model.Jenkins;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kanstantsin Shautsou
 */
public class FasterNodeProvisionRunnable implements Runnable {
    private final static Logger LOG = Logger.getLogger(FasterNodeProvisionRunnable.class.getName());

    private boolean haveFreeUnlabeled;
    private Label assignedLabel;

    public FasterNodeProvisionRunnable(boolean haveFreeUnlabeled, Label assignedLabel) {
        this.haveFreeUnlabeled = haveFreeUnlabeled;
        this.assignedLabel = assignedLabel;
    }

    public void run() {
        try {
            // because too frequent calls < 100, will be ignored
            Thread.sleep(50);
            final Jenkins instance = Jenkins.getActiveInstance();
            // unlabeled provisioner wins even if label assigned
            if (haveFreeUnlabeled || assignedLabel == null) {
                LOG.info("Kicking suggest review for unlabeled provisioner");
                instance.unlabeledNodeProvisioner.suggestReviewNow();
            } else {
                if (LOG.isLoggable(Level.INFO)) { // TODO replace with sl4j
                    LOG.info("Kicking suggest review for label " + assignedLabel.getName());
                }
                assignedLabel.nodeProvisioner.suggestReviewNow();
            }
        } catch (InterruptedException ignored) {
        }
    }
}
