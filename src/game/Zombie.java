package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.MoveActorAction;

/**
 * A Zombie.
 * 
 * This Zombie is pretty boring.  It needs to be made more interesting.
 * 
 * @author ram
 *
 */
public class Zombie extends ZombieActor {

	private GameMap map;
	private Random rand = new Random();
	private boolean canMoveThisTurn = false;
	private List<Limb> limbs;
	private static final int PUNCH_PROBABILITTY_CONSTANT = 25;
	private static final String[] ZOMBIE_SPEECH = new String[] {"groaaaaan", "braaaains", "buurp", "grrrr", "gargle gargle",
			"akhgrjbrdrd"};

	public Zombie(String name, GameMap gameMap) {
		super(name, 'Z', 50, ZombieCapability.UNDEAD, 50);
		
		inventory.add(new Coin());  // Each zombie has 1 coin, which is dropped when they are killed
		
		behaviours.add(new PickupWeaponBehaviour());
		behaviours.add(new AttackBehaviour(ZombieCapability.ALIVE));
		behaviours.add(new HuntBehaviour(Human.class, 11));
		behaviours.add(new WanderBehaviour());
		
		limbs = new ArrayList<>();
		// All Zombies start with 2 arms and 2 legs
		limbs.add(new Arm());
		limbs.add(new Arm());
		limbs.add(new Leg());
		limbs.add(new Leg());
		
		map = gameMap;
	}
	
	@Override
	public Weapon getWeapon() {
		for (Item item : inventory) {
			if (item.asWeapon() != null) {
				// May drop the weapon when using it (depending on arm count). If dropped, just use intrinsic weapon:
				if (getLimbCount(Arm.class) == 0 || (getLimbCount(Arm.class) == 1 && rand.nextBoolean())) {
					removeItemFromInventory(item);
					dropItem(item);
					System.out.println(this.name + " dropped the " + item.toString() + " it was holding");
					break;
				}
				return item.asWeapon();
			}
		}
		return getIntrinsicWeapon();
	}

	@Override
	public IntrinsicWeapon getIntrinsicWeapon() {
		int punchChance = PUNCH_PROBABILITTY_CONSTANT * getLimbCount(Arm.class); // 50, 25 and 0 chance of punching if there are 2, 1, and 0 arms respectively
		if (rand.nextInt(100) < punchChance) {
			return new Punch();
		}
		else {
			return new Bite(); 
		}
	}

	/**
	 * If a Zombie can attack, it will.  If not, it will chase any human within 10 spaces.  
	 * If no humans are close enough it will wander randomly.
	 * 
	 * @param actions list of possible Actions
	 * @param lastAction previous Action, if it was a multiturn action
	 * @param map the map where the current Zombie is
	 * @param display the Display where the Zombie's utterances will be displayed
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		// Zombies can speak sometimes (doesn't count as its turn)
		if (rand.nextInt(100)>90) {
			display.println(name + " says '" + ZOMBIE_SPEECH[rand.nextInt(ZOMBIE_SPEECH.length)] + "'");
		}

		for (Behaviour behaviour : behaviours) {
			Action action = behaviour.getAction(this, map);
			if (action != null) {
				if (action instanceof MoveActorAction) {
					canMoveThisTurn = false; // Update for next turn
				}
				return action;
			}
		}
		// Zombie is about to do nothing, so can move next turn:
		canMoveThisTurn = true;
		return new DoNothingAction();	
	}
	
	/**
	 * Do some damage to the current Zombie. 
	 * 
	 * Has a 25% chance of knocking off one of the Zombie's arms or legs.
	 *
	 * @param points number of hitpoints to deduct.
	 */
	@Override
	public void hurt(int points) {
		super.hurt(points);
		// 25% chance of dropping a limb:
		if (rand.nextInt(100) < 25) {
			knockOffLimb();
		}
	}
	
	/**
	 * Knocks a random limb off the zombie
	 * 
	 * @author rahnstavar
	 */
	private void knockOffLimb() {
		if (limbs.size() != 0){
			int selected = rand.nextInt(limbs.size());
			System.out.println("One of " + this.name + "'s " + limbs.get(selected).toString() + "s flung off.");
			dropItem(limbs.get(selected));
			limbs.remove(selected);
		}
	}
	
	// Custom dropItem method used instead of DropItemAction as zombies need 
	// to drop limbs and weapons in adjacent locations, not where they're standing
	private void dropItem(Item item) {
		Location dropLocation = map.locationOf(this);
		int selectedExit = rand.nextInt(dropLocation.getExits().size());
		dropLocation.getExits().get(selectedExit).getDestination().addItem(item);
	}
	
	private int getLimbCount(Class<?> limbType) {
		int tally = 0;
		for (Limb aLimb: limbs) {
			// Check if the selected limb is an instance of the parameter limbType:
			if (limbType.isInstance(aLimb)) {
				tally++;
			}
		}
		return tally;
	}
	
	/**
	 * Find out if the Zombie can move this turn
	 * @return a boolean, true if the Zombie can move, false otherwise
	 */
	public boolean canMove() {
		int legCount = getLimbCount(Leg.class);
		if (legCount == 2 || (legCount == 1 && canMoveThisTurn)) {
			return true;
		}
		return false;
	}
}
