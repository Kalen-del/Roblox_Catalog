import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;

/**
 * A page for searching objects.
 */
public class SearchablePage extends Page {
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- CONSTANTS
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private final int       LAYOUT_WIDTH = 700, LAYOUT_HEIGHT = 500;
    private final int       SCROLL_DISTANCE      = 16;
    private final int       SEARCH_INPUT_COLUMNS = 25;
    private final int       SCROLL_PANE_WIDTH    = 20, SCROLL_PANE_HEIGHT = 200;
    private final int       SEARCH_BAR_HEIGHT    = 100, SEARCH_BAR_WIDTH    = 600;
    private final double    SEARCH_ACCURACY      = 1.2; // The magic number, 1.2 search accuracy.
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ArrayList<Item> displayItemList;
    private FuzzySearch     fuzzySearch;
    private ItemsLayout     itemsLayout;

    /**
     * Constructor for searchable page.
     * @param backgroundColor the background color for the page.
     * @param pageName the page's name.
     * @param parentCatalog the parent catalog.
     * @param itemList the item list.
     */
    public SearchablePage(Color backgroundColor, String pageName, Catalog parentCatalog, ArrayList<Item> itemList) {
        super(backgroundColor, pageName, parentCatalog);
        // Constants
        final Color LAYOUT_BACKGROUND_COLOR    = new Color(79, 91, 112);
        final Color LAYOUT_FOREGROUND_COLOR    = new Color(192, 197, 206);

        fuzzySearch = new FuzzySearch(false, SEARCH_ACCURACY, itemList); // Create a new fuzzy search object.


        // Create the search bar panel.
        JPanel searchBarPanel = new JPanel();
        // Set the size
        searchBarPanel.setMaximumSize(new Dimension(SEARCH_BAR_WIDTH, SEARCH_BAR_HEIGHT));
        searchBarPanel.setMinimumSize(new Dimension(SEARCH_BAR_WIDTH, SEARCH_BAR_HEIGHT));
        searchBarPanel.setPreferredSize(new Dimension(SEARCH_BAR_WIDTH, SEARCH_BAR_HEIGHT));
        searchBarPanel.setOpaque(false);


        // Create the search bar text field.
        JTextField searchBar = new JTextField(SEARCH_INPUT_COLUMNS);
        searchBar.setFont(new Font("Arial", Font.PLAIN, 20));
        searchBar.setBackground(LAYOUT_FOREGROUND_COLOR);
        searchBar.setForeground(backgroundColor);
        searchBarPanel.add(searchBar);
        searchBar.setBorder(null);

        // Create a JLabel for the search bar.
        JLabel searchBarLabel = new JLabel();
        searchBarLabel.setAlignmentX(CENTER_ALIGNMENT);
        searchBarLabel.setText("SEARCH");
        searchBarLabel.setFont(new Font("Arial", Font.BOLD, 20));
        searchBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        searchBarLabel.setForeground(LAYOUT_FOREGROUND_COLOR);


        // Add it in.
        add(Box.createVerticalStrut(30));
        add(searchBarLabel);


        add(Box.createVerticalStrut(30));
        add(searchBarPanel);

        // Shallow copy the itemlist.
        displayItemList = new ArrayList<Item>(itemList);
        // Create the layout
        itemsLayout = new ItemsLayout(displayItemList, new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT), LAYOUT_BACKGROUND_COLOR, LAYOUT_FOREGROUND_COLOR, this, parentCatalog);
        this.add(itemsLayout);
        itemsLayout.setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(
                itemsLayout,
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

        // Listen to textbox changes.
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedEvent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changedEvent();
            }

            public void changedEvent(){ // Detected change.
                String newText = searchBar.getText(); // Get the text.
                if(newText.equals("")){ //If it is empty the user wants to see all the items.
                    itemsLayout.drawAllItems(); // Draw all the items.
                }else{
                    displayItemList = fuzzySearch.getSearchResults(newText); // Fuzzy search.
                    itemsLayout.setItems(displayItemList); // Set out new items.
                }
            }
        });

    }
}
