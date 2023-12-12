package de.garrafao.phitag.computationalannotator.common.query;

import de.garrafao.phitag.domain.core.Query;
import de.garrafao.phitag.domain.core.QueryComponent;
import de.garrafao.phitag.domain.instance.usepairinstance.query.*;

import java.util.ArrayList;
import java.util.List;

public class UsageQueryBuilder {


    private final List<QueryComponent> queryComponents;

    public UsageQueryBuilder() {
        this.queryComponents = new ArrayList<>();
    }

    public UsageQueryBuilder withInstanceid(final String instanceid) {
        if (instanceid == null || instanceid.isEmpty() || instanceid.isBlank()) {
            return this;
        }

        this.queryComponents.add(new InstanceidQueryComponent(instanceid));
        return this;
    }

    public UsageQueryBuilder withOwner(final String owner) {
        if (owner == null || owner.isEmpty() || owner.isBlank()) {
            return this;
        }
        this.queryComponents.add(new OwnerQueryComponent(owner));
        return this;
    }

    public UsageQueryBuilder withProject(final String project) {
        if (project == null || project.isEmpty() || project.isBlank()) {
            return this;
        }

        this.queryComponents.add(new ProjectQueryComponent(project));
        return this;
    }

    public UsageQueryBuilder withPhase(final String phase) {
        if (phase == null || phase.isEmpty() || phase.isBlank()) {
            return this;
        }

        this.queryComponents.add(new PhaseQueryComponent(phase));
        return this;
    }

    public Query build() {
        return new Query(queryComponents);
    }

}
