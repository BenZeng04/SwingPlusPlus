package spp.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * A general-purpose, enhanced JComponent that serves as a container for the GraphicalComponent.
 * Input handling and graphics can either be handled here, or by its individual components.
 *
 * @author Ben Zeng
 * @version 2
 */
public class SPComponent extends JPanel
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
     * Stores components that need to be added
     */
    private Queue<GraphicalComponent> pendingAdditions;
    /**
     * Stores components that need to be deleted
     */
    private Queue<GraphicalComponent> pendingDeletions;
    /**
     * List of all GraphicalComponents contained within this component
     */
    private ArrayList<GraphicalComponent> components;
    /**
     * Helper variable used to help suppress components from being run.
     */
    private boolean overridden;

    /**
     * Public constructor for SPComponent
     */
    public SPComponent()
    {
        setLayout(null);
        components = new ArrayList<>();
        pendingDeletions = new LinkedList<>();
        pendingAdditions = new LinkedList<>();
        activeMousePresses = new HashSet<>();
        activeKeys = new HashSet<>();
        setFocusable(true);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent event)
            {
                // Calls the overridable mousePressEvent function.
                mousePressEvent(event);
            }

            @Override
            public void mouseReleased(MouseEvent event)
            {
                // Calls the overridable mousePressEvent function.
                mouseReleaseEvent(event);
            }
        });

        addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent event)
            {
                // Calls the keyPressEvent function from here.
                keyPressEvent(event);
            }

            @Override
            public void keyReleased(KeyEvent event)
            {
                // Calls the keyPressEvent function from here.
                keyReleaseEvent(event);
            }
        });
    }

    @Override
    public final void paint(Graphics g)
    {
        clearPending();
        draw(g);
        Collections.sort(components);
        for(GraphicalComponent component: components)
            component.draw(g);
        clearPending();
        paintComponents(g);
    }

    /**
     * Used as an additional draw function, run prior to any of the components.
     * Can be used for handing background graphics separate to any of its GraphicalComponents.
     *
     * @param g the graphics instance
     */
    public void draw(Graphics g)
    {

    }

    /**
     * Run directly from an application upon the click of the mouse. Not meant to be overridden, as it contains implementation for running components.
     *
     * @param event the mouse event
     */
    protected final void mousePressEvent(MouseEvent event)
    {
        activeMousePresses.add(event.getButton());
        overridden = false;
        boolean hitboxActivated = false;

        requestFocus(); // Automatically requests focus on this component when clicked.
        clearPending();
        mousePressed(event);

        // Sorts by GraphicalComponent layer
        Collections.sort(components);
        for(int i = components.size() - 1; i >= 0; i--)
        {
            GraphicalComponent component = components.get(i);
            if(overridden)
                continue;
            if(!hitboxActivated)
            {
                GraphicalHitbox hitbox = component.getHitbox();
                if(hitbox != null && hitbox.activated(event))
                {
                    hitbox.whenClicked(event);
                    component.getActiveHitboxActivations().add(event.getButton());
                    hitboxActivated = true;
                }
            }
            component.getActiveMousePresses().add(event.getButton());
            component.mousePressed(event);
        }
        clearPending();
    }

    /**
     * Used as an additional mousePressed function, run prior to any of the components.
     * Can be used for handing input separate to any of its GraphicalComponents.
     *
     * @param event the mouse event
     */
    public void mousePressed(MouseEvent event)
    {
    }

    /**
     * Run directly from an application upon the release of the mouse. Not meant to be overridden, as it contains implementation for running components.
     * Can be used for handing input separate to any of its GraphicalComponents.
     *
     * @param event the mouse event
     */
    protected final void mouseReleaseEvent(MouseEvent event)
    {
        activeMousePresses.remove(event.getButton());

        clearPending();
        mouseReleased(event);

        Collections.sort(components);
        for(int i = components.size() - 1; i >= 0; i--)
        {
            GraphicalComponent component = components.get(i);
            if(component.getActiveHitboxActivations().remove(event.getButton()))
            {
                GraphicalHitbox hitbox = component.getHitbox();
                if(hitbox != null)
                    hitbox.whenReleased(event);
            }
            if(component.getActiveMousePresses().remove(event.getButton()))
                component.mouseReleased(event);
        }
        clearPending();
    }

    /**
     * Used as an additional mouseReleased function, run prior to any of the components. Meant to be overridden, but not necessary.
     *
     * @param event the mouse event
     */
    public void mouseReleased(MouseEvent event)
    {
    }

    /**
     * Run directly from an application upon the click of the key. Not meant to be overridden, as it contains implementation for running components.
     *
     * @param event the key event
     */
    protected final void keyPressEvent(KeyEvent event)
    {
        overridden = false;
        activeKeys.add(event.getKeyCode());

        clearPending();
        keyPressed(event);

        Collections.sort(components);
        for(int i = components.size() - 1; i >= 0; i--)
        {
            GraphicalComponent component = components.get(i);
            if(overridden)
                continue;
            component.keyPressed(event);
            component.getActiveKeys().add(event.getKeyCode());
        }
        clearPending();
    }

    /**
     * Used as an additional keyPressed function, run prior to any of the components. Meant to be overridden, but not necessary.
     *
     * @param event the key event
     */
    public void keyPressed(KeyEvent event)
    {
    }

    /**
     * Run directly from an application upon the release of the key. Not meant to be overridden, as it contains implementation for running components.
     *
     * @param event the key event
     */
    protected final void keyReleaseEvent(KeyEvent event)
    {
        activeKeys.remove(event.getKeyCode());

        clearPending();
        keyReleased(event);

        Collections.sort(components);
        for(int i = components.size() - 1; i >= 0; i--)
        {
            GraphicalComponent component = components.get(i);
            if(component.getActiveKeys().remove(event.getKeyCode()))
                component.keyReleased(event);
        }
        clearPending();
    }

    /**
     * Used as an additional keyPressed function, run prior to any of the components. Meant to be overridden, but not necessary.
     *
     * @param event the key event
     */
    public void keyReleased(KeyEvent event)
    {
    }

    /**
     * Adds a single component to the list of components.
     *
     * @param component the component to add
     * @return successful or not
     */
    public boolean addComponent(GraphicalComponent component)
    {
        if(component.getParent() != null)
            return false; // Cannot add the same component to multiple screens
        pendingAdditions.add(component);
        return true;
    }

    /**
     * Removes the given component from the list of components.
     *
     * @param component the component
     * @return successful or not
     */
    public boolean removeComponent(GraphicalComponent component)
    {
        if(components.contains(component))
        {
            pendingDeletions.add(component);
            return true;
        }
        return false;
    }

    /**
     * Removes all components that need to be deleted, and adds all components that need to be added..
     */
    private void clearPending()
    {
        while(!pendingAdditions.isEmpty())
        {
            GraphicalComponent component = pendingAdditions.poll();
            component.setParent(this);
            components.add(component);
        }
        while(!pendingDeletions.isEmpty())
        {
            GraphicalComponent component = pendingDeletions.poll();
            component.setParent(null);
            components.remove(component);
        }
    }

    /**
     * Returns the x-position of the mouse
     *
     * @return the x-position
     */
    public int getMouseX()
    {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation, this);
        return (int) mouseLocation.getX();
    }

    /**
     * Returns the y-position of the mouse
     *
     * @return the y-position
     */
    public int getMouseY()
    {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation, this);
        return (int) mouseLocation.getY();
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
     * Used previously to forcibly prevent lower-layered graphical components from receiving any key or mouse press events.
     * Deprecated to encourage usage of the new Hitbox system.
     */
    @Deprecated
    public void denyComponents()
    {
        overridden = true;
    }
}