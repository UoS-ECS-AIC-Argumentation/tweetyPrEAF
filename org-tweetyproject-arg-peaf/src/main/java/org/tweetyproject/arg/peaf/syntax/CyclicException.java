package org.tweetyproject.arg.peaf.syntax;

/**
 * CyclicException is thrown only if the given PEAF has cyclic in the support links
 *
 * @author Taha Dogan Gunes
 */
public class CyclicException extends RuntimeException {

    /**
     * Default constructor for the exception
     *
     * @param message error message
     */
    public CyclicException(String message) {
        super(message);
    }

}
