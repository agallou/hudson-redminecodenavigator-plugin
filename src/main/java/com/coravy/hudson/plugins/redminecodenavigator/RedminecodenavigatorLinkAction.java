package com.coravy.hudson.plugins.redminecodenavigator;

import hudson.model.Action;

public final class RedminecodenavigatorLinkAction implements Action {

    private final transient RedminecodenavigatorProjectProperty projectProperty;

    public RedminecodenavigatorLinkAction(RedminecodenavigatorProjectProperty rcnProjectProperty) {
        this.projectProperty = rcnProjectProperty;
    }

    public String getDisplayName() {
        return "Redmine";
    }

    public String getIconFileName() {
        return "/plugin/redminecodenavigator/logov3.png";
    }

    public String getUrlName() {
        return projectProperty.getProjectUrl().baseUrl();
    }

}
