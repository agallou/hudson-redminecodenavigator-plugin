package com.coravy.hudson.plugins.redminecodenavigator;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public final class RedminecodenavigatorProjectProperty extends
        JobProperty<AbstractProject<?, ?>> {

    /**
     * This will the URL to the project main branch.
     */
    private String projectUrl;

    @DataBoundConstructor
    public RedminecodenavigatorProjectProperty(String projectUrl) {
        this.projectUrl = new RedminecodenavigatorUrl(projectUrl).baseUrl();
    }

    public RedminecodenavigatorUrl getProjectUrl() {
        return new RedminecodenavigatorUrl(projectUrl);
    }

    @Override
    public Action getJobAction(AbstractProject<?, ?> job) {
        if (null != projectUrl) {
            return new RedminecodenavigatorLinkAction(this);
        }
        return null;
    }
    @Extension
    public static final class DescriptorImpl extends JobPropertyDescriptor {

        public DescriptorImpl() {
            super(RedminecodenavigatorProjectProperty.class);
            load();
        }

        public boolean isApplicable(Class<? extends Job> jobType) {
            return AbstractProject.class.isAssignableFrom(jobType);
        }

        public String getDisplayName() {
            return "Redmine project page";
        }

        @Override
        public JobProperty<?> newInstance(StaplerRequest req,
                JSONObject formData) throws FormException {
            RedminecodenavigatorProjectProperty tpp = req.bindJSON(
                    RedminecodenavigatorProjectProperty.class, formData);
            if (tpp.projectUrl == null) {
                tpp = null; // not configured
            }
            return tpp;
        }

    }
}
