package be.kdg.teame.kandoe.core.contracts;

/**
 * This interface provides methods that has to be implemented when receiving (or pushing) data from (or to) the web.
 */
public interface WebDataView {
    void showErrorConnectionFailure(String errorMessage);
}
