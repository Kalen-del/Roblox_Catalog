import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The featured page object for the catalog.
 */
public class FeaturedPage extends Page {
    // CONSTANTS
    private final int       LAYOUT_WIDTH           = 700, LAYOUT_HEIGHT      = 500;
    private final int       SCROLL_PANE_HEIGHT     = 200, SCROLL_PANE_WIDTH  = 20;
    private final int       SCROLL_DISTANCE        = 24;
    // OBJECT VALUES.
    private ArrayList<Item> displayItemList; // Store the display items here.

    /**
     * Constructor for the FeaturedPage object.
     * @param backgroundColor The background color for the page.
     * @param pageName The page name for the page.
     * @param parentCatalog The parent catalog for the page.
     * @param itemList The list of items for the page.
     */
    public FeaturedPage(Color backgroundColor, String pageName, Catalog parentCatalog, ArrayList<Item> itemList) {
        super(backgroundColor, pageName, parentCatalog);
        final Color LAYOUT_BACKGROUND_COLOR    = new Color(79, 91, 112);
        final Color LAYOUT_FOREGROUND_COLOR    = new Color(192, 197, 206);
        // Shallow copy it.
        displayItemList = new ArrayList<Item>(itemList);

        // Create a new layout.
        ItemsLayout featuredLayout = new ItemsLayout(itemList, new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT), LAYOUT_BACKGROUND_COLOR, LAYOUT_FOREGROUND_COLOR, this, parentCatalog);
        this.add(featuredLayout);
        featuredLayout.setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(
                featuredLayout,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setSize(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT);
        scrollPane.setVisible(true);
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_DISTANCE);
        scrollPane.getVerticalScrollBar().setBackground(LAYOUT_BACKGROUND_COLOR);

        // Override the scrollbar UI material.
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = LAYOUT_FOREGROUND_COLOR;
            }
        });
    }
}
