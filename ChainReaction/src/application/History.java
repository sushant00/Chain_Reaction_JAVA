package application;

import java.io.Serializable;
/**
 * Helper Data type,for storing current moves has row and column fields
 * @author Dhruv Bhagat 2016146
 * @author Sushant Kumar Singh 2016103
 *
 */
public class History implements Serializable {
	int row;
	int col;
	/**
	 * Initializes history objects with row and column information
	 * @param r denotes the row number
	 * @param c denotes the column number
	 */
	History(int r,int c){
		this.row=r;
		this.col=c;
	}
}
