package game;

import java.util.ArrayList;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Menu;

/**
 * Class representing the Player.
 */
public class Player extends Human implements Wallet {

	private Menu mainMenu = new Menu();
	private ArrayList<Coin> wallet = new ArrayList<>();
	
	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
	}
	
	/**
	 * Gets the number of coins in the player's wallet
	 * @return the number of coins in the wallet
	 */
	@Override
	public int getWealth() {
		return wallet.size();
	}
	
	/**
	 * A method to add a coin to the wallet
	 * @param coin a coin
	 */
	@Override
	public void addCoinToWallet(Coin coin) {
		wallet.add(coin);
	}
	
	/**
	 * Method to spend the player's coins
	 * @param cost the number of coins to spend
	 */
	@Override
	public void spendCoins(int cost) {
		try {
			if (cost > getWealth()) {
				throw new Exception(this + " does not have enough coins");
			}
			for (int i = 0; i < cost; i++) {
				wallet.remove(wallet.size() - 1);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		display.println("Current player health: " + hitPoints + "/" + maxHitPoints);
		String plural = getWealth() != 1 ? "s" : "";
		display.println("Current player wealth: " + getWealth() + " coin" + plural);
		
		// Add some behaviours/actions not considered in World:
		actions.add(new HarvestBehaviour().getAction(this, map));
		actions.add(new CollectCoinsBehaviour().getAction(this, map));
		actions.add(new EndLifeAction());
		
		// Handle multi-turn Actions
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();		
		
		Action action = mainMenu.showMenu(this, actions, display);
		// While the option chosen presents a sub-menu:
		if (action instanceof MenuAction) {
			action = ((MenuAction) action).getAction(this,map, display);
		}
		
		return action;
	}
}
