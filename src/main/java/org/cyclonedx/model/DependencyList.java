package org.cyclonedx.model;

import java.util.ArrayList;
import java.util.List;

public class DependencyList extends ArrayList<Dependency> {
    public DependencyList(List<Dependency> dependencies) {
        super(dependencies);
    }
}