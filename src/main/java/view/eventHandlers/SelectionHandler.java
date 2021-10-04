package view.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import view.interfaces.Selectable;

/**
 * Handles mouse click events on a node
 */
public class SelectionHandler {

    private EventHandler<MouseEvent> mouseClickedHandler;

    /**
     * Creator for selection handler
     *
     * @param root base node the events to be handled will occur in
     */
    public SelectionHandler(final Parent root) {
        mouseClickedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                SelectionHandler.this.doOnMouseClicked(root, mouseEvent);
                mouseEvent.consume();
            }
        };
    }

    /**
     * Notifies target on click event
     *
     * @param root base node events occur in
     * @param event mouse event
     */
    public void doOnMouseClicked(Parent root, MouseEvent event) {
        Node target = (Node) event.getTarget();
        while (!(target instanceof Selectable) & !(target.equals(root))) {
            target = target.getParent();
        }
        if (!(target.equals(root))) {
            Selectable selectableTarget = (Selectable) target;
            if (selectableTarget.requestSelection(true)) {
                selectableTarget.notifySelection(true);
            }
        }
    }

    /**
     * Returns mouse event handler
     *
     * @return EventHandler<MouseEvent>
     */
    public EventHandler<MouseEvent> getMouseClickedHandler() {
        return mouseClickedHandler;
    }
}
