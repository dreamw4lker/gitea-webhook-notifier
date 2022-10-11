package org.dreamw4lker.gwn.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WebhookBean {
    private String ref;

    @JsonProperty("compare_url")
    private String compareUrl;

    private List<CommitBean> commits;
    private RepositoryBean repository;

    public void setCommits(List<CommitBean> commits) {
        this.commits = Objects.requireNonNullElseGet(commits, ArrayList::new);
    }
}
