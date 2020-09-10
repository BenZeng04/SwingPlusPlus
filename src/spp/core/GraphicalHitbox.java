package spp.core;

import java.awt.event.MouseEvent;

/**
 * An interface used for collision detection and mouse event handling within a GraphicalComponent.
 *
 * @author Ben Zeng
 * @version 2
 */
public interface GraphicalHitbox
{
    /**
     * A method to detect mouse events being triggered within the GraphicalComponent this hitbox is contained in.
     *
     * @param me The mouse event being fired
     * @return whether or not this hitbox has been activated.
     */
    boolean activated(MouseEvent me);

    /**
     * The event which occurs upon this hitbox being clicked on.
     *
     * @param me The mouse event being fired
     */
    void whenClicked(MouseEvent me);

    /**
     * Called upon the release of the mouse after having clicked on this hitbox.
     *
     * @param me The mouse event being fired
     */
    void whenReleased(MouseEvent me);
}
