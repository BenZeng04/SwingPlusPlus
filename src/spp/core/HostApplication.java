package spp.core;

import javax.swing.*;
import java.awt.*;

/**
 * An extension of the custom MultiPanel which constantly repaints itself at a certain framerate to allow for animations.
 *
 * @author Ben Zeng
 * @version 2
 */
public class HostApplication extends MultiPanel
{

    /**
     * Returns the host application of a specific panel, if there is one.
     *
     * @param panel the panel
     * @return the root
     * @throws IllegalArgumentException if the root of this panel is not a host application.
     */
    public static HostApplication getHost(Container panel) throws IllegalArgumentException
    {
        while(!(panel instanceof HostApplication))
            panel = panel.getParent();
        return (HostApplication) panel;
    }

    /**
     * Creates a base / default starter project using a custom title, width, and height of the frame.
     *
     * @param title the title of the JFrame
     * @param width the width of the JFrame
     * @param height the height of the JFrame
     * @return the HostApplication used for the project
     */
    public static HostApplication createBaseProject(String title, int width, int height)
    {
        JFrame frame = new JFrame();
        HostApplication hostApplication = new HostApplication();
        frame.setContentPane(hostApplication);
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        return hostApplication;
    }

    /**
     * Whether or not the animation timer is currently active
     */
    private boolean active;
    /**
     * The start time (milliseconds) of when the timer had started.
     */
    private long start;
    /**
     * The framerate of the timer.
     */
    private int FPS;
    /**
     * The number of frames that have elapsed since the timer started. Used to accurately simulate framerate.
     */
    private int framesElapsed;

    /**
     * Default constructor.
     */
    public HostApplication()
    {
        super();
        Thread timer = new Thread(() -> {

            while(true)
            {
                if(active)
                {
                    long current = System.currentTimeMillis();
                    while(framesElapsed < (current - start) * FPS / 1000)
                    {
                        repaint();
                        framesElapsed++;
                    }
                }
            }
        });
        timer.start();
        setFPS(60);
        setActive(true);
        setFocusable(true);
    }

    /**
     * Sets the timer to either be active or inactive.
     *
     * @param active whether or not the timer should be active
     */
    public void setActive(boolean active)
    {
        if(active)
        {
            start = System.currentTimeMillis();
            framesElapsed = 0;
        }
        this.active = active;
    }

    /**
     * Sets the framerate of this timer.
     *
     * @param FPS the framerate
     */
    public void setFPS(int FPS)
    {
        this.FPS = FPS;
    }
}
