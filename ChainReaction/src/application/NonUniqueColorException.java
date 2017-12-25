package application;

/**
 * <h1>NonUniqueColorException.java</h1>
 * Raises Exception when  color is not unique
 * @author Dhruv Bhagat 2016146
 * @author Sushant Kumar Singh 2016103
 *
 */

public class NonUniqueColorException extends Exception{

	private static final String message = "NonUniqueColorException: Color should be unique for each player. ";
/**
 * Generates Exception message
 * @param msg String that denotes message
 */
	public NonUniqueColorException(String msg){

		super(message+msg);

	}

}