package game;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;

/**
 * A Crop class. Extends Ground. Sowed by farmers and harvested by humans. Takes
 * 20 turns to grow, then it can be harvested to create food.	
 */
public class Crop extends Ground {
	
	private int age = 0;
	private boolean beenFertilised = false;
	private static final int FERTALISER_AMOUNT = 10;
	
	/**
	 * Constructor	
	 */
	public Crop() {
		super('=');
	}
	
	/**
	 * Method used to grow the crop every turn, and change its character
	 * based on age.
	 * 
	 * @param location of the crop on the game map
	 */
	@Override
	public void tick(Location location) {
		super.tick(location);

		age++;
		if (age == 10)
			displayChar = ':';
		if (isRipe())
			displayChar = '1';
	}
	/**
	 * Method used check if the crop is ripe (over 20 turns old)
	 * 
	 * @return boolean based on whether or not the crop is ripe
	 */
	public boolean isRipe() {
		return age>=20;
	}
	/**
	 * Fertalises the crop, raising it's age by the constant amount (10 turns). 
	 * Reduces turns needed for ripeness.
	 */
	public void fertalise() {
		age+=FERTALISER_AMOUNT;
		beenFertilised = true;
	}
	
	/**
	 * Checks if the crop has already been fertilised
	 * @return boolean based on if the crop has been fertilised already
	 */
	public boolean getFertilisedStatus() {
		return beenFertilised;
	}

}
