import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.http.WebSocket;
import java.util.Objects;

/*
  links used:
  https://stackoverflow.com/a/3607942
  https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java

 */

/**
 * The page that displays an item.
 */
public class ItemPage extends Page
                implements ActionListener

{
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- CONSTANTS
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private final double CORRECTION_FACTOR   = 0.9; // Keep these up here for easy customization later.
    private final int    ROW_DISTANCE        = 50, INPUT_COLUMNS = 10, TEXT_SIZE = 20;
    private final int    DETAILS_PANE_WIDTH  = 300, DETAILS_PANE_HEIGHT = 500;
    private final int    CONTENT_PANE_WIDTH  = 600, CONTENT_PANE_HEIGHT = 600;
    private final int    IMAGE_LENGTH        = 250;
    private final Color  PURCHASE_BUTTON_BACKGROUND_COLOR = new Color(150, 206, 180);
    private final Color  PURCHASE_BUTTON_FOREGROUND_COLOR = new Color(255, 255, 255);

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private JPanel contentPane, detailsPane;
    private Item   item;
    private JLabel itemDescriptionLabel, itemAuthorLabel, itemPriceLabel;
    private CatalogButton purchaseButton;
    private Color backgroundColor, foregroundColor, correctedColor;


    /**
     * Refreshes the item's price.
     */
    private void refreshItemPrice(){
        itemPriceLabel.setText(String.format("%s Robux", String.valueOf(item.getRobuxPrice())));
    }

    /**
     * Adds text to the display panel.
     * @param title The title of the item.
     * @param text The JLabel for the panel.
     * @param titleColor The header's color.
     * @param textColor The text's color.
     * @param font The font of the JLabel.
     */
    private void addTextDisplayRow(String title, JLabel text, Color titleColor, Color textColor, Font font){
        JPanel rowPanel    = new JPanel(); // Create a new row JPanel.

        text.setFont(font);
        text.setForeground(textColor);

        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.Y_AXIS)); // Use a vertical box layout.
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Allign everything to the left.
        rowPanel.setOpaque(false); // Make it invisible.

        // Set up the header label.
        JLabel headerLabel = new JLabel();
        headerLabel.setForeground(titleColor);
        headerLabel.setFont(font);
        headerLabel.setOpaque(false);
        headerLabel.setText(title);

        // Add it to the root panel.
        rowPanel.add(headerLabel);
        // Add a gap.
        rowPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Add the text
        rowPanel.add(text);

        // Add the root panel to the details panel.
        detailsPane.add(rowPanel);
        // Put some spacing in between each root panel.
        detailsPane.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    /**
     * Changes up the item after a purchase.
     * @param amount The amount purchased
     */
    private void handlePurchase(int amount){
        item.setStock(item.getStock() - amount); // Update the stock
        if(item.isShouldWarnLowStock() && item.getStock() <= item.getLowStockThreshold() && item.getStock() > 0){ // If we should warn of low stock.
            this.setPageTitle(String.format("<html><font color=orange>LOW STOCK</font> %s</html>", item.toString())); // Set it as low stock.
        }else if(item.getStock() <= 0){ // If there is no more stock.
            this.setPageTitle(String.format("<html><font color=red>SOLD OUT</font> %s</html>", item.toString())); // Tell the user it is sold out.
            this.remove(purchaseButton); // Remove purchasing capabilities.
        }
    }

    /**
     * Sets up a prompt label.
     * @param text The text of the prompt label.
     * @param font The font of the prompt label.
     * @param background The background color of the prompt label.
     * @param foreground The foreground color of the prompt label.
     * @param label The JLabel objet for the prompt.
     * @param parentSize The size of the parent.
     */
    private void setUpPromptFormLabel(String text, Font font, Color background, Color foreground, JLabel label, Dimension parentSize){
        label.setText(text);
        label.setFont(font);
        label.setPreferredSize(new Dimension((int)parentSize.getWidth(), (int)parentSize.getHeight()/5)); // Set the size.
        label.setBackground(background);
        label.setForeground(foreground);
        label.setAlignmentX(CENTER_ALIGNMENT); // Align to the center.
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

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
     * Creates a popup for the purchase result.
     * @param inputRobux The amount of robux inputted by the user.
     * @param requiredRobux the required robux to buy this product.
     * @param amountPurchased The quantity of the item.
     * @param popUpSize The size of the popup.
     */
    private void displayPopUpPurchaseResult(int inputRobux, int requiredRobux, int amountPurchased, Dimension popUpSize){
        String excessRobux = String.valueOf((Math.abs(inputRobux - requiredRobux))); // Get the robux required / needed.
        String popUpTitle;
        JPanel backPanel   = new JPanel(); // The root panel.
        JLabel resultLabel = new JLabel(); // Create the result panel.
        Picture resultPicture;
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS)); // Use box layout and make it top to bottom.
        backPanel.setAlignmentX(CENTER_ALIGNMENT); // Align everything to the center.
        backPanel.setBackground(foregroundColor); // Set the background color with foreground to create contrast.
        backPanel.setSize(popUpSize); // Set the size of the popup.



        if (inputRobux >= requiredRobux){ // If the user has enough robux.
            popUpTitle = "PURCHASE SUCCESSFUL :)";
            handlePurchase(amountPurchased); // handle the purchasing.
            // get the success picture.
            resultPicture = new Picture("sitepictures", "success", "png", new Dimension(50, 50), new Dimension(50, 50));
            setUpPromptFormLabel(String.format("<html><font color=green>PURCHASE SUCCESSFUL</font><br>Enjoy your new %s<br>Your change is %s <font color=green>Robux</font></html>", item.toString(), excessRobux), new Font("Arial", Font.BOLD,TEXT_SIZE), foregroundColor, backgroundColor, resultLabel, popUpSize.getSize());
            // ^^ Use html for this since creating a new JPanel will be too tedious.
        }else{
            popUpTitle = "PURCHASE FAILED :(";
            // get the failure picture.
            resultPicture = new Picture("sitepictures", "fail", "png", new Dimension(50, 50), new Dimension(50, 50));
            setUpPromptFormLabel(String.format("<html><font color=red>PURCHASE FAILED</font><br>You need %s more <font color=green>Robux</font> to buy %s.<br>Please try again.</html>", excessRobux, item.toString()), new Font("Arial", Font.BOLD,TEXT_SIZE), foregroundColor, backgroundColor, resultLabel, popUpSize.getSize());
            // ^^ Use html to tell user of a failed purchase.
        }
        resultPicture.setAlignmentX(CENTER_ALIGNMENT); // Align it to the center.
        backPanel.add(resultPicture); // Add the picture of the outcome.
        backPanel.add(Box.createRigidArea(new Dimension(0, 40))); // Add some spacing
        backPanel.add(resultLabel); // Add the result text.
        backPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Add some spacing
        // Launch the JOption Pane.
        JOptionPane.showOptionDialog(this, backPanel, popUpTitle, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    /**
     * Prompts the user the order form.
     * @param popUpSize The size of the popup form.
     */
    private void promptOrderForm(Dimension popUpSize){
        int totalSaleCost        = item.getRobuxPrice(); // Get the robux cost of the item.
        String priceString       = "Total Cost: %s Robux";
        String[] quantityChoices = new String[item.getStock()]; // Preset the array with enough space.
        // Get the order icon.
        Picture orderPicture = new Picture("sitepictures", "order", "png", new Dimension(50, 50), new Dimension(50, 50));
        orderPicture.setAlignmentX(CENTER_ALIGNMENT);
        orderPicture.setHorizontalAlignment(SwingConstants.CENTER); // Align the picture in the center.

        // Set up the back panel.
        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS)); // Use box layout.
        backPanel.setAlignmentX(CENTER_ALIGNMENT); // Align it to the center.
        backPanel.setBackground(foregroundColor);
        backPanel.setSize(popUpSize); // Set the size to the popup size since we want this to cover the entire label.

        // Get the title card JLabel.
        JLabel titleCard = new JLabel();
        // Set up the prompt label.
        setUpPromptFormLabel("ORDER FORM", new Font("Arial", Font.BOLD,TEXT_SIZE+10), foregroundColor, correctedColor, titleCard, popUpSize.getSize());


        // Create a drop down menu array with all the choices.
        for(int i = 1; i <= item.getStock(); i++){
            quantityChoices[i-1] = String.valueOf(i); // Go from 1 -> stockAmount.
        }

        // Create the quantity label.
        JLabel quantityLabel = new JLabel();
        // Set up the label.
        setUpPromptFormLabel("QUANTITY", new Font("Arial", Font.BOLD,TEXT_SIZE), foregroundColor, backgroundColor, quantityLabel, popUpSize.getSize());

        // Create the drop down menu with the choices.
        final JComboBox<String> quantityDropDown = new JComboBox<String>(quantityChoices);

        // Set up the size.
        quantityDropDown.setMaximumSize(quantityDropDown.getPreferredSize());
        quantityDropDown.setAlignmentX(CENTER_ALIGNMENT);

        // Create the robux amount label.
        JLabel robuxAmountLabel = new JLabel();
        // Set up the label.
        setUpPromptFormLabel(String.format(priceString, String.valueOf(totalSaleCost)), new Font("Arial", Font.BOLD,TEXT_SIZE), foregroundColor, backgroundColor, robuxAmountLabel, popUpSize.getSize());




        // Create the robux request label.
        JLabel requestRobuxLabel = new JLabel();
        // Set up the label.
        setUpPromptFormLabel("Please Enter Robux", new Font("Arial", Font.BOLD,TEXT_SIZE), foregroundColor, backgroundColor, requestRobuxLabel, popUpSize.getSize());

        // Set up the text box panel.
        JPanel textBoxPanel = new JPanel();
        textBoxPanel.setPreferredSize(new Dimension((int)popUpSize.getWidth()/2, 25)); // Set the size.
        textBoxPanel.setOpaque(false);

        // Create the actual  textbox object.
        JTextField robuxInputField = new JTextField(INPUT_COLUMNS); // Keep it INPUT_COLUMNS long.

        // Add the textbox object to the textbox panel.
        textBoxPanel.add(robuxInputField);
        robuxInputField.setBackground(correctedColor); // Set the color.



        // Add everything just created.
        backPanel.add(titleCard);
        backPanel.add(orderPicture);
        backPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backPanel.add(quantityLabel);
        backPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backPanel.add(quantityDropDown);
        backPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        backPanel.add(robuxAmountLabel);
        backPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backPanel.add(requestRobuxLabel);
        backPanel.add(textBoxPanel);
        backPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        // Listen to dropdown selection.
        quantityDropDown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){ // If we have something selected.
                    String newSelection = (String)quantityDropDown.getSelectedItem(); // Get the selected object
                    if(newSelection != null) { // If the selection exists.
                        robuxAmountLabel.setText(String.format(priceString, String.valueOf(item.getRobuxPrice() * Integer.parseInt(newSelection))));
                        // ^^ Calculate the price and update the robux amount label.
                    }
                }
            }
        });

        // Set up the popup and store the result.
        int result = JOptionPane.showOptionDialog(this, backPanel, "PURCHASE", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"ORDER"}, null);
        if (result != -1) { // If user did not click exit.
            int userRobuxAmount = 0;
            if (!robuxInputField.getText().equals("") && robuxInputField.getText().chars().allMatch( Character::isDigit)){ // Check if the text box is not blank and if it is a number.
                userRobuxAmount = Integer.parseInt(robuxInputField.getText()); // Get the robux entered.
            }
            int amountPurchased = Integer.parseInt((String) Objects.requireNonNull(quantityDropDown.getSelectedItem())); // Get the amount purchased from the dropdown.
            totalSaleCost       = amountPurchased * item.getRobuxPrice(); // Calculate the total cost
            displayPopUpPurchaseResult(userRobuxAmount, totalSaleCost, amountPurchased, new Dimension(500, 500)); // Set up the purchase popup.
        }
    }

    /**
     * Handles all the item's details.
     */
    private void handleItemDetails(){

        // Change the title color to the corrected color.
        this.setPageTitleColor(correctedColor);
        // Create the details pane.
        detailsPane   = new JPanel();
        detailsPane.setOpaque(false);
        detailsPane.setLayout(new BoxLayout(detailsPane, BoxLayout.Y_AXIS));
        detailsPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPane.setMinimumSize(new Dimension(DETAILS_PANE_WIDTH, DETAILS_PANE_HEIGHT));
        detailsPane.setMinimumSize(new Dimension(DETAILS_PANE_WIDTH, DETAILS_PANE_HEIGHT));
        contentPane.add(detailsPane);

        //Item Author
        itemAuthorLabel = new JLabel();
        itemAuthorLabel.setOpaque(false);
        itemAuthorLabel.setText(item.getAuthor());
        // Set up the label.
        addTextDisplayRow("By", itemAuthorLabel, correctedColor, foregroundColor, new Font("Arial", Font.PLAIN, TEXT_SIZE));


        //Item description.
        itemDescriptionLabel = new JLabel();
        itemDescriptionLabel.setOpaque(false);

        // Set up the label.
        addTextDisplayRow("Description", itemDescriptionLabel, correctedColor, foregroundColor, new Font("Arial", Font.PLAIN, TEXT_SIZE));

        //Prepare to scale the text.
        itemDescriptionLabel.setText(item.getDescription());
        itemDescriptionLabel.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        setScaledText(itemDescriptionLabel, item.getDescription(), CONTENT_PANE_WIDTH);

        // Set up the price label.
        itemPriceLabel     = new JLabel();
        itemPriceLabel.setOpaque(false);
        itemPriceLabel.setText(String.format("%s Robux", String.valueOf(item.getRobuxPrice())));
        // Set up the label.
        addTextDisplayRow("Price", itemPriceLabel, correctedColor, foregroundColor, new Font("Arial", Font.PLAIN, TEXT_SIZE));
    }
    /**
     * draws the ItemPage.
     */
    public void draw(){ // Override
        handlePurchase(0); // Before we draw we want to update the item page by throwing in a phony purchase.
        super.draw();
    }

    /**
     * Constructor for the ItemPage object.
     * @param backgroundColor the background color.
     * @param pageName the page's name.
     * @param parentCatalog the parent catalog object.
     * @param item the item object.
     * @param previousPage the previous page object.
     * @param foreground the foreground color of the object.
     */
    public ItemPage(Color backgroundColor, String pageName, Catalog parentCatalog, Item item, Page previousPage, Color foreground) {
        super(backgroundColor, pageName, parentCatalog);
        this.item = item;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foreground;
        // ENCHANCE COLOR (Pitch up the color)
        double correctedRed   = (255 - foregroundColor.getRed())   * CORRECTION_FACTOR + foregroundColor.getRed();
        double correctedBlue  = (255 - foregroundColor.getGreen()) * CORRECTION_FACTOR + foregroundColor.getGreen();
        double correctedGreen = (255 - foregroundColor.getBlue())  * CORRECTION_FACTOR + foregroundColor.getBlue();

        this.correctedColor = new Color((int)correctedRed, (int)correctedGreen, (int)correctedBlue);


        add(Box.createRigidArea(new Dimension(0, 20)));
        // Create the content pane.
        contentPane = new JPanel();
        contentPane.setMaximumSize(new Dimension(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT));
        contentPane.setMinimumSize(new Dimension(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT));
        contentPane.setLayout(new GridLayout(2,1)); // Set this content pane out as a grid layout 2 by 1.
        contentPane.add(new Picture("itempictures", item.toString(), "png", new Dimension(IMAGE_LENGTH, IMAGE_LENGTH),  new Dimension(IMAGE_LENGTH, IMAGE_LENGTH)));
        add(contentPane);
        contentPane.setOpaque(false);
        handleItemDetails(); // Set up the details panel.

        // Set up the back button.
        CatalogButton previousPageButton = new CatalogButton("BACK", new Font("Arial", Font.BOLD, TEXT_SIZE), foreground, null, new Dimension(150, 50), previousPage);
        previousPageButton.setContentAreaFilled(false);
        previousPageButton.setAlignmentX(CENTER_ALIGNMENT); // Align it in the center.
        previousPageButton.setHorizontalAlignment(SwingConstants.CENTER);

        // Set up the purchase button.
        purchaseButton    = new CatalogButton("BUY", new Font("Arial", Font.BOLD, TEXT_SIZE), PURCHASE_BUTTON_FOREGROUND_COLOR, PURCHASE_BUTTON_BACKGROUND_COLOR, new Dimension(150, 50), null);
        purchaseButton.setAlignmentX(CENTER_ALIGNMENT); // Align it to the center.
        purchaseButton.setHorizontalAlignment(SwingConstants.CENTER);

        // Set up the purchase button listener.
        purchaseButton.addActionListener(this);
        previousPageButton.addActionListener(this);

        // Add the purchase button.
        add(purchaseButton);
        add(previousPageButton);
    }


    /**
     * Listens to any actions.
     */
    public void actionPerformed(ActionEvent e) {
        Object actionObject = e.getSource(); // Get the source of the action.
        if(actionObject instanceof CatalogButton){ // Check if the object is an instance of the catalog button.
            CatalogButton catalogObject = (CatalogButton) actionObject;
            if(catalogObject.getPage() != null){ // If there is a page linked then the user wants to go back.
                this.loadPage(((CatalogButton) actionObject).getPage());
            }else{ // If there is no page linked then it should be a popup.
                promptOrderForm(new Dimension(500, getHeight()/2));
            }
        }

    }
}
