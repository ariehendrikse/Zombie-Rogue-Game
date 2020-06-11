package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

public class EndLifeAction extends Action {


	@Override
	public String execute(Actor actor, GameMap map) {
		map.removeActor(actor);
		return actor+ " took the easy way out ";
	}

	@Override
	public String menuDescription(Actor actor) {
		// TODO Auto-generated method stub
		return "Quit game";
	}
	@Override
	public String hotkey() {
		return "+";
	}

}
