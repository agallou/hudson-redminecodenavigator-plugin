package com.coravy.hudson.plugins.redminecodenavigator;

import org.apache.commons.lang.StringUtils;

public final class RedminecodenavigatorUrl {

    private static String normalize(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        // Strip "tree/..."
        if (url.contains("tree/")) {
            url = url.replaceFirst("tree/.*$", "");
        }
        if (!url.endsWith("/")) {
            url += '/';
        }
        return url;
    }

    private final String baseUrl;

    RedminecodenavigatorUrl(final String input) {
        this.baseUrl = normalize(input);
    }
    @Override
    public String toString() {
        return this.baseUrl;
    }

    public String baseUrl() {
        return this.baseUrl;
    }

    public String commitId(final String id) {
        return new StringBuilder().append(baseUrl).append("repository/revisions/").append(id).toString();
    }

}
