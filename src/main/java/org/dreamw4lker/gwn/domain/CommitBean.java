package org.dreamw4lker.gwn.domain;

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
public class CommitBean {
    private String id;
    private String message;
    private String url;
    private PersonBean author;
    private String timestamp;
    private List<String> added;
    private List<String> removed;
    private List<String> modified;

    public void setAdded(List<String> added) {
        this.added = Objects.requireNonNullElseGet(added, ArrayList::new);
    }

    public void setRemoved(List<String> removed) {
        this.removed = Objects.requireNonNullElseGet(removed, ArrayList::new);
    }

    public void setModified(List<String> modified) {
        this.modified = Objects.requireNonNullElseGet(modified, ArrayList::new);
    }
}
