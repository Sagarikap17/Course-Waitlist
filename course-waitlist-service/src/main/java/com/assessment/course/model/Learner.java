package com.assessment.course.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class Learner {
    private final String id;
    private final String name;
    private final Set<String> completedPrerequisites;

    public Learner(String id, String name, Set<String> completedPrerequisites) {
        this.id = Objects.requireNonNull(id, "Learner ID must not be null");
        this.name = Objects.requireNonNull(name, "Learner name must not be null");
        this.completedPrerequisites = completedPrerequisites == null ? Collections.emptySet() : Set.copyOf(completedPrerequisites);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getCompletedPrerequisites() {
        return completedPrerequisites;
    }

    public boolean hasPrerequisite(String prereqId) {
        return prereqId == null || prereqId.isEmpty() || completedPrerequisites.contains(prereqId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Learner learner = (Learner) o;
        return Objects.equals(id, learner.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
