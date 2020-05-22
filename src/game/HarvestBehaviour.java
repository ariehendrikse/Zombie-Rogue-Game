package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;

public class HarvestBehaviour implements Behaviour {
	
	Location toHarvest;
	/**
	 * Constructor
	 */
	public HarvestBehaviour() {
	}
	@Override
	public Action getAction(Actor actor, GameMap map) {	
		
		toHarvest=(firstHarvestable(actor,map));
		
		if (toHarvest!=null) {
			return new HumanHarvestAction(toHarvest);
		}
		
		return null;
	}
	/**
	 * Returns the first crop that is harvestable by iterating over the ground
	 * the actor is standing on then the surrounding exits.
	 * @param actor
	 * @param map
	 * @return
	 */
	protected Location firstHarvestable(Actor actor, GameMap map) {
		Location here = map.locationOf(actor);
		Ground ground =here.getGround();
		if (ground instanceof Crop) {
			if ((((Crop) ground).isRipe())) {
				return here;
			} 
		}
		for (Exit exit : here.getExits()) {
			Location destination = exit.getDestination();
			ground = destination.getGround();
			if (ground instanceof Crop){
				if ((((Crop) ground).isRipe())) {
					return destination;
				} 
				
			}
			
		}
		return null;
	}
}
