import javax.swing.*;
import java.awt.*;

/**
 * A button but for the GUI navigation.
 */
public class Navigation extends CatalogButton {
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES.
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private String navigationName;
    private Color  activeColor;
    private Color  inactiveColor;
    private boolean isActive = false;
    private JButton button;


    /**
     * Checks if the button is active or not.
     * @return a boolean: true of the button is active, false otherwise.
     */
    public boolean getActiveStatus(){
        return isActive;
    }

    /**
     * Sets the active status of the page.
     * @param newActiveStatus A boolean representing the new status.
     */
    public void setActiveStatus(boolean newActiveStatus){
        isActive = newActiveStatus;
        if(isActive){
            this.setBackground(activeColor);
        }else{
            this.setBackground(inactiveColor);
        }
    }

    /**
     * Constructor for the navigation object.
     * @param navigationName The name of the navigation object.
     * @param fontObject The font object for the navigation object.
     * @param foreground A color object representing the color of the navigation's text.
     * @param activeColor A color object representing the color of the navigation background once it is active.
     * @param inactiveColor A color object representing the color of the navigation background once it is inactive.
     * @param linkedPage A page object representing the page that is linked to this button.
     */

    public Navigation(String navigationName, Font fontObject, Color foreground, Color activeColor, Color inactiveColor, Page linkedPage){
        super(navigationName, fontObject, foreground, inactiveColor, new Dimension(500, 35), linkedPage);
        this.activeColor   = activeColor;
        this.inactiveColor = inactiveColor;
        this.setBorder(null); // Don't want the ugly borders so set it to null.
    }


}
