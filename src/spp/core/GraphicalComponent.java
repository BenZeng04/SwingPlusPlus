package spp.core;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;

/**
 * A dynamic counterpart to the JComponent meant to represent general graphical elements within a container,
 * such as moving buttons, entities, cursors, and much more.
 * Allows for more freedom and simplicity in handling mouse and key inputs directed to this specific component, as well
 * as the ability to move around, change bounds / hitboxes, be added and removed, etc.
 *
 * @author Ben Zeng
 * @version 2
 */
public class GraphicalComponent implements Comparable<GraphicalComponent>
{
    /**
     * All the key codes that are currently being held
     */
    private HashSet<Integer> activeKeys;

    /**
     * All the mouse button codes that are currently being held
     */
    private HashSet<Integer> activeMousePresses;

    /**
     * All the mouse buttons that have specifically triggered / activated this components' hitbox
     */
    private HashSet<Integer> activeHitboxActivations;

    /**
     * The Hitbox for this specific component.
     */
    private GraphicalHitbox hitbox;

    /**
     * The screen which the given component is inside of.
     */
    private SPComponent parent;

    /**
     * An identifier for the layer of the component. Necessary when handling multi-layered components, with specific components on a higher level than others.
     */
    private final int layer;

    /**
     * Default Constructor for GraphicalComponent.
     */
    public GraphicalComponent()
    {
        this(0);
    }

    /**
     * Constructor for GraphicalComponent.
     *
     * @param layer the layer of the component.
     */
    public GraphicalComponent(int layer)
    {
        this.layer = layer;
        activeHitboxActivations = new HashSet<>();
        activeMousePresses = new HashSet<>();
        activeKeys = new HashSet<>();
    }

    @Override
    public final int compareTo(GraphicalComponent other)
    {
        return Integer.compare(getLayer(), other.getLayer());
    }

    /**
     * Getter for the layer parameter.
     *
     * @return the layer
     */
    public final int getLayer()
    {
        return layer;
    }

    /**
     * Effectively the draw function for an individual component.
     *
     * @param g the graphics instance
     */
    public void draw(Graphics g)
    {

    }

    /**
     * Called whenever this component's container receives a mouse press event, if not forcibly overridden by other components.
     *
     * @param event the mouse event
     */
    public void mousePressed(MouseEvent event)
    {
    }

    /**
     * Called whenever this component's container receives a key press event, if not forcibly overridden by other components.
     *
     * @param event the key event
     */
    public void keyPressed(KeyEvent event)
    {
    }

    /**
     * Called whenever this component's parent container receives a mouse release event, if the original mouse press event has not been forcibly overridden by other components.
     *
     * @param event the mouse event
     */
    public void mouseReleased(MouseEvent event)
    {

    }

    /**
     * Called whenever this component's parent container receives a key release event, if the original mouse press event has not been forcibly overridden by other components.
     *
     * @param event the key event
     */
    public void keyReleased(KeyEvent event)
    {

    }

    /**
     * Returns the parent component.
     *
     * @return The parent component
     */
    public SPComponent getParent()
    {
        return parent;
    }

    /**
     * Sets the parent component.
     *
     * @param parent the parent component
     */
    public void setParent(SPComponent parent)
    {
        this.parent = parent;
    }

    /**
     * Adds another GraphicalComponent to this component's parent.
     *
     * @param gc the graphical component
     */
    public void addComponent(GraphicalComponent gc)
    {
        getParent().addComponent(gc);
    }

    /**
     * Removes a graphical component from this component's parent.
     *
     * @param gc the graphical component
     */
    public void removeComponent(GraphicalComponent gc)
    {
        getParent().removeComponent(gc);
    }

    /**
     * Returns the host application of this component's parent container, if there is one.
     *
     * @return the root
     * @throws IllegalArgumentException if the root of this panel is not a host application.
     */
    public HostApplication getHostPanel() throws IllegalArgumentException
    {
        return HostApplication.getHost(getParent());
    }

    /**
     * Returns the hitbox of this component.
     *
     * @return the hitbox
     */
    public GraphicalHitbox getHitbox()
    {
        return hitbox;
    }

    /**
     * Sets the hitbox and hitbox listeners of this component.
     *
     * @param hitbox the hitbox
     */
    public void setHitbox(GraphicalHitbox hitbox)
    {
        this.hitbox = hitbox;
    }

    /**
     * Returns the list of active keys being held.
     *
     * @return the list of keys
     */
    public HashSet<Integer> getActiveKeys()
    {
        return activeKeys;
    }

    /**
     * Returns the list of active mouse buttons being held
     *
     * @return the list of mouse presses
     */
    public HashSet<Integer> getActiveMousePresses()
    {
        return activeMousePresses;
    }

    /**
     * Returns whether or not a specific key is held.
     *
     * @param keyCode the key
     * @return whether or not keyCode is held
     */
    public boolean isKeyHeld(int keyCode)
    {
        return activeKeys.contains(keyCode);
    }

    /**
     * Returns whether or not a specific mouse button is held.
     *
     * @param buttonCode the mouse button
     * @return whether or not buttonCode is held
     */
    public boolean isMouseButtonHeld(int buttonCode)
    {
        return activeMousePresses.contains(buttonCode);
    }

    /**
     * Returns the list of mouse buttons that have been clicked inside of this components' hitbox without being released yet.
     *
     * @return the list of mouse buttons
     */
    public HashSet<Integer> getActiveHitboxActivations()
    {
        return activeHitboxActivations;
    }

    /**
     * Returns whether or not a specific mouse button has been clicked inside of this components' hitbox without being released yet.
     *
     * @param buttonCode the mouse button
     * @return whether or not buttonCode is held
     */
    public boolean isHitboxHeld(int buttonCode)
    {
        return activeHitboxActivations.contains(buttonCode);
    }

    /**
     * Used previously to forcibly prevent lower-layered graphical components from receiving any key or mouse press events.
     * Deprecated to encourage usage of the new Hitbox system.
     */
    @Deprecated
    public void denyComponents()
    {
        getParent().denyComponents();
    }
}