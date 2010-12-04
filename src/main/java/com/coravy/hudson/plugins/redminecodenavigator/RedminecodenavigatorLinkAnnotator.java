package com.coravy.hudson.plugins.redminecodenavigator;

import hudson.Extension;
import hudson.MarkupText;
import hudson.MarkupText.SubText;
import hudson.model.AbstractBuild;
import hudson.plugins.git.GitChangeSet;
import hudson.scm.ChangeLogAnnotator;
import hudson.scm.ChangeLogSet.Entry;

import java.util.regex.Pattern;

@Extension
public class RedminecodenavigatorLinkAnnotator extends ChangeLogAnnotator {

    @Override
    public void annotate(AbstractBuild<?, ?> build, Entry change,
            MarkupText text) {
        final RedminecodenavigatorProjectProperty p = build.getProject().getProperty(
                RedminecodenavigatorProjectProperty.class);
        if (null == p || null == p.getProjectUrl()) {
            return;
        }
        annotate(p.getProjectUrl(), text, change);
    }

    void annotate(final RedminecodenavigatorUrl url, final MarkupText text, final Entry change) {
        final String base = url.baseUrl();
        for (LinkMarkup markup : MARKUPS) {
            markup.process(text, base);
        }
        
        if(change instanceof GitChangeSet) {
            GitChangeSet cs = (GitChangeSet)change;
            text.wrapBy("", " (<a href='"+url.commitId(cs.getId())+"'>commit: "+cs.getId()+"</a>)");
        }
    }

    private static final class LinkMarkup {
        private final Pattern pattern;
        private final String href;

        LinkMarkup(String pattern, String href) {
            // \\\\d becomes \\d when in the expanded text.
            pattern = NUM_PATTERN.matcher(pattern).replaceAll("(\\\\d+)");
            pattern = ANYWORD_PATTERN.matcher(pattern).replaceAll(
                    "((?:\\\\w|[._-])+)");
            this.pattern = Pattern.compile(pattern);
            this.href = href;
        }

        void process(MarkupText text, String url) {
            for (SubText st : text.findTokens(pattern)) {
                st.surroundWith("<a href='" + url + href + "'>", "</a>");
            }
        }

        private static final Pattern NUM_PATTERN = Pattern.compile("NUM");
        private static final Pattern ANYWORD_PATTERN = Pattern
                .compile("ANYWORD");
    }

    private static final LinkMarkup[] MARKUPS = new LinkMarkup[] { new LinkMarkup(
            "(?:C|c)lose(?:s?)\\s(?<!\\:)(?:#)NUM", // "Closes #123"
            "issues/$1/find") };
}
