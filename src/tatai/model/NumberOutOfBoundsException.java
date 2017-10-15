package tatai.model;

@SuppressWarnings("serial")

/**
 * Exceptionn thrown when user provides invalid range of numbers 
 * when instantiating the Number class.
 * @author lucy
 *
 */
public class NumberOutOfBoundsException extends Exception {
	public NumberOutOfBoundsException(String message){
		super(message);
	}
}
