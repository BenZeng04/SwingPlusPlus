package spp.core;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * An extension of the JPanel which utilizes the CardLayout layout to easily switch between views.
 * This is useful for creating any type of JPanel that requires the switching of multiple other components, such as
 * Screen Switching within a program.
 *
 * @author Ben Zeng
 * @version 2
 */
public class MultiPanel extends JPanel
{
    /**
     * The layout of the given panel
     */
    private CardLayout layout;
    /**
     * Maps String IDs to screens
     */
    private HashMap<String, JComponent> map;

    /**
     * Default constructor for MultiPanel.
     */
    public MultiPanel()
    {
        layout = new CardLayout();
        map = new HashMap<>();
        setLayout(layout);
    }

    /**
     * Adds a panel to the layout. This panel can now be "switched" to using its ID. Takes into account whether or not the panel is a MultiPanel.
     *
     * @param panel   The panel
     * @param panelID the ID of the panel
     */
    public final void add(JComponent panel, String panelID)
    {
        map.put(panelID, panel);
        super.add(panel, panelID);
    }

    /**
     * Sets a panel to be displayed via its ID.
     *
     * @param panelID the ID
     */
    public final void displayPanel(String panelID)
    {
        JComponent current = map.get(panelID);
        current.requestFocus();
        layout.show(this, panelID);
    }

    /**
     * Getter for a panel via its ID.
     *
     * @param panelID the ID
     * @return the panel
     */
    public final JComponent getPanel(String panelID)
    {
        return map.get(panelID);
    }
}