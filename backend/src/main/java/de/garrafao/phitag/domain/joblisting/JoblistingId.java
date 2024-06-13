package de.garrafao.phitag.domain.joblisting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.garrafao.phitag.domain.project.ProjectId;
import lombok.Data;

@Data
@Embeddable
public class JoblistingId implements Serializable {
    

    @Column(name = "name")
    private String name;

    private ProjectId projectid;

    public JoblistingId() {
    }

    public JoblistingId(final String name, final ProjectId projectid) {
        this.name = name;
        this.projectid = projectid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((projectid == null) ? 0 : projectid.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JoblistingId other = (JoblistingId) obj;
        return name.equals(other.name) && projectid.equals(other.projectid);
    }

    @Override
    public String toString() {
        return String.format("JoblistingId[name='%s', projectid='%s']", name, projectid);
    }
}
