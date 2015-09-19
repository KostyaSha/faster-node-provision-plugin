package org.jenkinsci.plugins.fasternodeprovision;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

/**
 * @author Kanstantsin Shautsou
 */
@Extension
public class FasterNodeProvisionGlobalConfig extends GlobalConfiguration {
    boolean disabled = false;

    public FasterNodeProvisionGlobalConfig() {
        load();
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        save();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json);
        return true;
    }

}
