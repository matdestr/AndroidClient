package be.kdg.teame.kandoe.core.contracts;

/**
 * This interface provides an abstraction to set the view of a presenter in the MVP pattern through a method,
 * after being injected into the view.
 * */
public interface InjectableUserActionsListener<T> {
    void setView(T view);
}
