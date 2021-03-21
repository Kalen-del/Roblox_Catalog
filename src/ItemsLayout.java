import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Displays the item widgets in a layout.
 */
public class ItemsLayout extends JPanel {
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ArrayList<ItemWidget> itemWidgetsList = new ArrayList<>();
    private ArrayList<ItemPage>   itemPages       = new ArrayList<>();
    private Catalog parentCatalog;
    private Page    parentPage;
    private Color   backgroundColor, foregroundColor;
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- METHODS
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Gets the ItemWidget object for a specific item.
     * @param item the item object.
     * @return null if no ItemWidget is found and itemWidget if an item is found.
     */
    private ItemWidget getWidgetForItem(Item item){
        for (ItemWidget itemWidget: itemWidgetsList){
            if(itemWidget.getItem() == item){
                return itemWidget;
            }
        }
        return null;
    }

    /**
     * Adds a new item to the items layout.
     * @param newItem the new item that will be added.
     */
    public void addItem(Item newItem){
        setUpItem(newItem);
    }

    /**
     * undraws the items in the layout.
     */
    private void undrawAllItems(){
        for(ItemWidget itemWidget: itemWidgetsList){
            itemWidget.setVisible(false);
            remove(itemWidget);
        }
    }

    /**
     * draws out all the items in the layout.
     */
    public void drawAllItems(){
        undrawAllItems();
        repaint(); // Repaint it.
        for(ItemWidget itemWidget: itemWidgetsList){
            add(itemWidget);
            itemWidget.setVisible(true);
        }
        repaint(); // Repaint it.
    }

    /**
     * Set the items for the layout.
     * @param items The array list holding the items.
     */
    public void setItems(ArrayList<Item> items){
        undrawAllItems();
        for(Item item: items){
            ItemWidget foundWidget = getWidgetForItem(item);
            if(foundWidget != null){
                add(foundWidget);
                foundWidget.setVisible(true);
            }
        }
    }

    /**
     * Sets up an item for the item layout.
     * @param item The item that should be set up.
     */
    private void setUpItem(Item item){
        ItemPage   itemPage                 = new ItemPage(backgroundColor, item.toString(), parentCatalog, item, parentPage, foregroundColor); // Get the new page for the item.
        CatalogButton itemPageCatalogButton = new CatalogButton(itemPage); // Get the button for the widget.
        ItemWidget itemWidget               = new ItemWidget(item, new Dimension(200, 350), foregroundColor, itemPageCatalogButton, parentCatalog); // Create the widget.
        itemWidgetsList.add(itemWidget); // Add it to the widget list.
        itemPages.add(itemPage); // Ad it to the item page.
        add(itemWidget); // Add it to the layout.
    }

    /**
     * The constructor for the item layout panel.
     * @param items The array list of items.
     * @param size The size of the layout panel.
     * @param backgroundColor the color object for the background of the panel.
     * @param foregroundColor the color object for the foreground of the panel.
     * @param parentPage the parent page which holds this
     * @param parentCatalog the catalog object.
     */
    public ItemsLayout(ArrayList<Item> items, Dimension size, Color backgroundColor, Color foregroundColor, Page parentPage, Catalog parentCatalog){
        this.parentCatalog = parentCatalog;
        this.parentPage    = parentPage;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.setMinimumSize(size); // Set the size.
        this.setMaximumSize(size);
        this.setBackground(backgroundColor);
        this.setLayout(new WrapLayout(FlowLayout.CENTER)); // Using Wrap Layout (Open Source Code, Very handy).

        for (Item item : items) { // Set up the items.
            setUpItem(item);
        }

    }
}
