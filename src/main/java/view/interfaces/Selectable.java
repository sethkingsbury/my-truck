package view.interfaces;

/**
 * Interface to make nodes clickable
 */
public interface Selectable {
    /**
     * Checks if node is currently selectable
     *
     * @param select
     * @return
     */
    public boolean requestSelection(boolean select);

    /**
     * Notifies node that it has been selected
     *
     * @param select
     */
    public void notifySelection(boolean select);
}
