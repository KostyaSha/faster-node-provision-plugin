package org.jenkinsci.plugins.fasternodeprovision;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Computer;
import hudson.model.Label;
import hudson.model.Node;
import hudson.model.Queue;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author Kanstantsin Shautsou
 */
@Extension
public class FasterNodeProvisionQueueDecisionHandler extends Queue.QueueDecisionHandler {
    private final static Logger LOG = Logger.getLogger(FasterNodeProvisionQueueDecisionHandler.class.getName());

    @Override
    public boolean shouldSchedule(Queue.Task p, List<Action> actions) {
        final FasterNodeProvisionGlobalConfig config = GlobalConfiguration.all().get(FasterNodeProvisionGlobalConfig.class);
        if (config == null || config.isDisabled()) {
            return true;
        }

        final Label assignedLabel = p.getAssignedLabel();
        final Jenkins jenkins = Jenkins.getActiveInstance();
        boolean suggest = false;
        boolean haveFreeUnlabeled = false;

        for (Node n : jenkins.getNodes()) {
            final Computer computer = n.toComputer();
            if (n.getMode() == Node.Mode.NORMAL && computer != null && computer.isIdle()) {
                haveFreeUnlabeled = true;
                break;
            }
        }

        if (assignedLabel == null) {
            suggest = !haveFreeUnlabeled;
        } else {
            if (assignedLabel.getIdleExecutors() < 1) {
                suggest = true;
            }
        }

        if (suggest) {
            Computer.threadPoolForRemoting.submit(
                    new FasterNodeProvisionRunnable(haveFreeUnlabeled, assignedLabel)
            );
        }

        return true;
    }
}
