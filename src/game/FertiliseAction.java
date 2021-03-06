package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
/**
 * Class for the action that fertilises a crop
 */ 
public class FertiliseAction extends Action {
	
	private Location target;

	/**
	 * Constructor
	 * @param target : location of crop to be fertilised
	 */
	public FertiliseAction(Location target) {
		this.target=target;
	}
	
	/**
	 * Executes the Action, which decreases the time left for a crop to ripen
	 * @return A description of the action that happened
	 */
	@Override
	public String execute(Actor actor, GameMap map) {
		try {
			if (!(target.getGround() instanceof Crop)) {
				throw new IllegalStateException("The ground at actor's location must be a crop");
			}
			((Crop) target.getGround()).fertalise();
			return actor.toString() + " fertilised crop";
		} 
		catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}	
	}

	/**
	 * Gets the menu description for this action
	 * @return the menu description
	 */
	@Override
	public String menuDescription(Actor actor) {
		return "Fertilse crop";
	}

}
