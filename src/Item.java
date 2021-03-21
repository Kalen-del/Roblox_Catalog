import javax.swing.*;
import javax.swing.text.html.HTML;
import java.awt.*;

/**
 * Represents an item.
 */
public class Item {
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES.
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private String itemName, description, author;
    private String[] tags;
    private String[] sizes;
    private int lowStockThreshold;
    private int robuxPrice;
    private int stock;
    private boolean shouldWarnLowStock = false;
    private boolean purchasable      = false;


    /**
     * If the item should warn if there is low stock.
     * @return true of it should, false otherwise.
     */
    public boolean isShouldWarnLowStock(){
        return shouldWarnLowStock;
    }

    /**
     * Sets the stock of an item
     * @param stock the value that the stock will be set at.
     */
    public void setStock(int stock){
        this.stock       = stock;
        if(this.stock <= 0){ // If the stock is gone.
            purchasable = false; // You can't purchase it anymore.
        }
    }

    /**
     * If an item is purchasable or not.
     * @return true if it is, false otherwise.
     */
    public boolean isPurchasable(){
        return purchasable;
    }

    /**
     * Gets the low stock threshold.
     * @return integer representing the low stock threshold.
     */
    public int getLowStockThreshold(){
        return lowStockThreshold;
    }

    /**
     * Gets the price of an item in Robux.
     * @return An integer representing the price of the item in robux.
     */
    public int getRobuxPrice(){
        return robuxPrice;
    }

    /**
     * Gets the item description of the item.
     * @return A string representing the item description.
     */
    public String getDescription(){
        return description;
    }

    /**
     * Gets the stock of an item.
     * @return An integer representing the stock.
     */

    public int getStock(){
        return stock;
    }

    /**
     * Gets the author of the item.
     * @return A string representing the author of the item.
     */
    public String getAuthor(){
        return author;
    }

    /**
     * Gets the string representation of the object.
     * @return A string representation of the object.
     */
    public String toString(){
        return itemName;
    }

    /**
     * Gets the tags associated with the item.
     * @return A String table containing the tags.
     */
    public String[] getItemTags(){
        return tags.clone();
    }


    /**
     * Constructor for the item object.
     * @param itemName The item's name.
     * @param description The item's description.
     * @param author The item's author.
     * @param robuxPrice The price of the item in robux.
     * @param stock The stock of the item.
     * @param tags The tags associated with the item.
     * @param shouldWarnLowStock If the item should warn once at low stock.
     * @param lowStockThreshold The low stock threshold.
     */
    public Item(String itemName, String description, String author, int robuxPrice, int stock, String[] tags, boolean shouldWarnLowStock, int lowStockThreshold){
        this.itemName            = itemName;
        this.description         = description;
        this.author              = author;
        this.robuxPrice          = robuxPrice;
        this.stock               = stock;
        this.tags                = tags.clone(); // Clone the tags.
        this.shouldWarnLowStock  = shouldWarnLowStock;
        this.purchasable         = stock > 0; // Sets the stock.
        this.lowStockThreshold   = lowStockThreshold;
    }
}
