package view.eventHandlers;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import view.interfaces.Hoverable;
import view.interfaces.Selectable;

public class HoverHandler {

    private EventHandler<MouseEvent> mouseEnteredHandler;

    private EventHandler<MouseEvent> mouseExitedHandler;

    private boolean mouseIn = false;

    public HoverHandler (final Parent root) {
        mouseEnteredHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                HoverHandler.this.doOnMouseEntered(root, mouseEvent);
                mouseEvent.consume();
            }
        };
        mouseExitedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                HoverHandler.this.doOnMouseExited(root, mouseEvent);
                mouseEvent.consume();
            }
        };
    }

    public void doOnMouseEntered(Parent root, MouseEvent event) {
        Node target = (Node) event.getTarget();
        while (!(target instanceof Hoverable) & !(target.equals(root))) {
            target = target.getParent();
        }
        ((Hoverable) target).notifyMouseEntry(true);
    }

    public void doOnMouseExited(Parent root, MouseEvent event) {
        Node target = (Node) event.getTarget();
        while (!(target instanceof Hoverable) & !(target.equals(root))) {
            target = target.getParent();
        }
        ((Hoverable) target).notifyMouseExit(true);
    }

    public EventHandler<MouseEvent> getMouseEnteredHandler() {
        return mouseEnteredHandler;
    }

    public EventHandler<MouseEvent> getMouseExitedHandler() {
        return mouseExitedHandler;
    }
}
