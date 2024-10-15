/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author hcadavid
 */

@Component
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final ConcurrentHashMap<Tuple<String,String>,Blueprint> blueprints=new ConcurrentHashMap<>();

    public InMemoryBlueprintPersistence() {

        Point[] pts1 = new Point[]{new Point(140, 140), new Point(115, 115)};
        Blueprint bp1 = new Blueprint("nico", "house_design", pts1);
        blueprints.put(new Tuple<>(bp1.getAuthor(), bp1.getName()), bp1);

        Point[] pts2 = new Point[]{new Point(150, 150), new Point(120, 120)};
        Blueprint bp2 = new Blueprint("richi", "office_layout", pts2);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);

        Point[] pts3 = new Point[]{new Point(160, 160), new Point(125, 125)};
        Blueprint bp3 = new Blueprint("tommy", "garden_plan", pts3);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);

        Point[] pts4 = new Point[]{new Point(170, 170), new Point(130, 130)};
        Blueprint bp4 = new Blueprint("richi", "kitchen_remodel", pts4);
        blueprints.put(new Tuple<>(bp4.getAuthor(), bp4.getName()), bp4);

    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(),bp.getName()))){
            throw new BlueprintPersistenceException("The given blueprint already exists: "+bp);
        }
        else{
            blueprints.putIfAbsent(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        }
    }


    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        return blueprints.get(new Tuple<>(author, bprintname));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
        Set<Blueprint> authorBlueprints = new HashSet<>();
        for (Blueprint blueprint : blueprints.values()) {
            if (blueprint.getAuthor().equals(author)){
                authorBlueprints.add(blueprint);
            }
        }
        return authorBlueprints;
    }

    /**
     * @return The blueprints
     */
    @Override
    public Set<Blueprint> getAllBlueprints() {
        Set<Blueprint> blueprintsCall = new HashSet<>();
        blueprintsCall.addAll(blueprints.values());
        return blueprintsCall;
    }



}
