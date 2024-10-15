package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;

import java.util.Set;



public interface BlueprintFilter {


    public Blueprint filterBlueprint(Blueprint bp);


    Set<Blueprint> filterBlueprints(Set<Blueprint> blueprints);


}
