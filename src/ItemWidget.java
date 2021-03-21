import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays the item as a widget.
 */
public class ItemWidget extends JPanel
                implements ActionListener
{
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- CONSTANTS
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private final int    COMPONENT_PADDING     = 20; // Keep these up here for to change things easier later.
    private final int    TEXT_SIZE             = 20;
    private final double CORRECTION_FACTOR     = 0.8;
    private final String ITEM_ROOT_DIRECTORY   = "itempictures";
    private final int    MAX_WIDGET_WIDTH      = 200;
    private final int    MAX_WIDGET_HEIGHT     = 70;
    private final int    LINKED_PAGE_WIDTH     = 150, LINKED_PAGE_HEIGHT = 50;
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- Object Variables
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private String   itemName;
    private String[] itemTags;
    private Catalog  parentCatalog;
    private Item     item;
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- Swing Objects
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private JLabel itemNameLabel;


    /**
     * Get the string representation of the object.
     * @return the string representation.
     */
    public String toString(){
        return itemName;
    }

    /**
     * Get the item associated with the Item Widget.
     * @return the item object.
     */
    public Item   getItem(){
        return this.item;
    }
    //https://stackoverflow.com/questions/19194699/i-want-to-decrease-the-font-size-if-text-doesnt-fit-in-jlabel

    /**
     * Scales the text label.
     * @param label The text label.
     * @param text The string representation of the text.
     * @param labelWidth An int representing the width of the label.
     */
    private void setScaledText(JLabel label, String text, int labelWidth){
        Font labelFont = (Font)label.getClientProperty("originalfont"); // Get the original font.
        if(labelFont == null){ // If the font exists
            labelFont = label.getFont(); // Get the font.
            label.putClientProperty("originalfont", labelFont);
        }
        int labelTextWidth = label.getFontMetrics(label.getFont()).stringWidth(label.getText()); // Get the string width.
        if (labelTextWidth > labelWidth){ // If the text is too long.
            double sizeRatio   = (double)labelWidth / (double)labelTextWidth; // Scale it down.
            int newFontSize    = (int)Math.floor(labelFont.getSize() * sizeRatio); // Get the new font size with the ratio.
            label.setFont(new Font(labelFont.getName(), labelFont.getStyle(), newFontSize)); // Reset the font.
        }else{
            label.setFont(labelFont); // Set the font as the previous one since the text can fit.
        }
        label.setText(text); // Set the text.
    }

    /**
     * Sets up the widget's information panel.
     * @param textColor Color object representing the color of the text.
     * @param linkedCatalogButton The button that will link the item widget to the item page.
     */

    private void handleWidgetItems(Color textColor, CatalogButton linkedCatalogButton){
        // set the foreground with a brighter correction factor.
        double correctedRed   = (255 - textColor.getRed())   * CORRECTION_FACTOR + textColor.getRed();
        double correctedBlue  = (255 - textColor.getGreen()) * CORRECTION_FACTOR + textColor.getGreen();
        double correctedGreen = (255 - textColor.getBlue())  * CORRECTION_FACTOR + textColor.getBlue();
        Color correctedColor    = new Color((int)correctedRed, (int)correctedGreen, (int)correctedBlue);
        //

        // Create the item name label and the robux panel.
        itemNameLabel           = new JLabel();
        JPanel robuxPanel       = new JPanel();
        Dimension sizeDimension = new Dimension(MAX_WIDGET_WIDTH, MAX_WIDGET_HEIGHT);


        // Set up the item name label.
        itemNameLabel.setPreferredSize(sizeDimension);
        itemNameLabel.setMinimumSize(sizeDimension);
        itemNameLabel.setMaximumSize(sizeDimension);
        itemNameLabel.setText(item.toString());
        itemNameLabel.setVisible(true);
        itemNameLabel.setForeground(new Color(0,0,0));
        itemNameLabel.setFont(new Font("Arial", Font.BOLD, TEXT_SIZE));
        setScaledText(itemNameLabel, item.toString(), MAX_WIDGET_WIDTH); // Scale the text
        itemNameLabel.setForeground(correctedColor);
        add(itemNameLabel);
        //


        add(Box.createVerticalStrut(5));

        // Set up the robux panel.
        robuxPanel.setSize(getWidth(), getHeight()/2);
        robuxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        robuxPanel.setLayout(new BoxLayout(robuxPanel, BoxLayout.X_AXIS)); // Use x-axis box layout.
        robuxPanel.add(new Picture("sitepictures", "currency", "png", new Dimension(20, 20), new Dimension(20, 20)), BorderLayout.WEST);
        robuxPanel.setOpaque(false);
        //


        // Set up the robux amount label.
        JLabel itemRobuxAmountLabel = new JLabel();
        itemRobuxAmountLabel.setSize(getWidth(), getHeight()/3);
        itemRobuxAmountLabel.setFont(new Font("Arial", Font.BOLD, TEXT_SIZE));
        itemRobuxAmountLabel.setText(String.valueOf(item.getRobuxPrice()));
        itemRobuxAmountLabel.setForeground(textColor);
        robuxPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        robuxPanel.add(itemRobuxAmountLabel);
        //

        add(robuxPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Set up the linked button.
        linkedCatalogButton.setButtonName("VIEW");
        linkedCatalogButton.setFont(new Font("Arial", Font.BOLD, TEXT_SIZE+10));
        linkedCatalogButton.setButtonForegroundColor(textColor);
        linkedCatalogButton.setMaximumSize(new Dimension(LINKED_PAGE_WIDTH, LINKED_PAGE_HEIGHT));
        linkedCatalogButton.addActionListener(this);
        linkedCatalogButton.setContentAreaFilled(false);
        add(linkedCatalogButton);
        //

    }

    /**
     * Called once a button is clicked.
     * @param e The action event that holds the object.
     */
    public void actionPerformed(ActionEvent e) {
        CatalogButton sourceCatalogButton = (CatalogButton)e.getSource(); // Get the source.
        parentCatalog.loadPage(sourceCatalogButton.getPage()); // Now load the page using the catalog object.
    }


    /**
     * Constructor for Item Widget.
     * @param o The item.
     * @param widgetSize The size of the widget.
     * @param foregroundColor The text color.
     * @param linkedCatalogButton The button which links the item widget to the item's page.
     * @param parentCatalog The parent catalog object.
     */
    public ItemWidget(Item o, Dimension widgetSize, Color foregroundColor, CatalogButton linkedCatalogButton, Catalog parentCatalog){
        this.parentCatalog = parentCatalog;
        this.item = o;
        itemName  = o.toString();
        setVisible(true);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS)); // Use the box layout.
        this.setSize(widgetSize);
        setPreferredSize(widgetSize);
        setMinimumSize(widgetSize);
        setMaximumSize(widgetSize);
        this.setOpaque(false);

        // Create the item's picture.
        Picture itemPicture = new Picture(ITEM_ROOT_DIRECTORY, o.toString(), "png", new Dimension((int)getWidth() - COMPONENT_PADDING, (int)getWidth() - COMPONENT_PADDING),  new Dimension((int)getWidth() - COMPONENT_PADDING, (int)getWidth() - COMPONENT_PADDING));
        this.add(itemPicture);

        handleWidgetItems(foregroundColor, linkedCatalogButton);


        setVisible(true);
    }
}
