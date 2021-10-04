package view.interfaces;

/**
 * Interface to change nodes on mouse hover
 */
public interface Hoverable {

    public void notifyMouseEntry(boolean select);

    public void notifyMouseExit(boolean select);
}
