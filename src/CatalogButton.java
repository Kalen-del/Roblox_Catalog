import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A button used in the catalog to link pages.
 */
public class CatalogButton extends JButton {
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private Page linkToPage;
    private String buttonName;
    private Picture pictureObject;


    /**
     * Sets the linked page.
     * @param object The page object that will be linked to this button.
     */
    public void setPage(Page object){
        linkToPage = object;
    }

    /**
     * Gets the string representation of this object.
     * @return The string representation of this object.
     */
    public String toString(){
        return buttonName;
    }

    /**
     * Gets the page linked to this button.
     * @return The page linked to this button.
     */
    public Page getPage(){
        return linkToPage;
    }

    /**
     * Sets the button's background color.
     * @param backgroundColor the color object representing the background color.
     */
    public void setButtonBackgroundColor(Color backgroundColor){
        this.setBackground(backgroundColor);
    }

    /**
     * Sets the button's text color.
     * @param foregroundColor the color object representing the text color.
     */
    public void setButtonForegroundColor(Color foregroundColor){
        this.setForeground(foregroundColor);
    }

    /**
     * Sets the button's name.
     * @param navigationName The string object representing the objet's name.
     */
    public void setButtonName(String navigationName){
        setText(navigationName);
        buttonName = navigationName;
    }

    /**
     * Sets the button font.
     * @param fontObject the font object.
     */
    public void setButtonFont(Font fontObject){
        this.setFont(fontObject);
    }

    /**
     * The Constructor for the CatalogButton object.
     * @param navigationName The name of the object.
     * @param fontObject The font of the object.
     * @param foreground The text color of the object.
     * @param backgroundColor The background color of the object.
     * @param buttonDimension The size of the object.
     * @param linkedPage The page linked to this object
     */
    public CatalogButton(String navigationName, Font fontObject, Color foreground, Color backgroundColor, Dimension buttonDimension, Page linkedPage){
        buttonName = navigationName;
        this.setBackground(backgroundColor);
        this.setForeground(foreground);
        this.setText(navigationName);
        this.setContentAreaFilled(false); // To set the content area as non filled.
        this.setFont(fontObject);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Once a cursor goes over this make it the hand symbol.
        this.setFocusPainted(false); // Don't make it have a border when its focused.
        this.setHorizontalTextPosition(SwingConstants.CENTER); // Align it to the center.
        this.setMaximumSize(buttonDimension); // Set the size.
        this.setOpaque(true); // Make it non see-through.
        this.linkToPage = linkedPage;
    }

    /**
     * Constructor for Catalog Button.
     * @param linkedPage the linked page.
     */
    public CatalogButton(Page linkedPage){  // This constructor is used when you do not want to add in all the details yet and you just want a hallow catalog button object.
        this.setFocusPainted(false); // Disable the focusing.
        this.linkToPage = linkedPage;
    }
}
