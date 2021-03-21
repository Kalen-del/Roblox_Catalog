import javax.swing.*;
import java.awt.*;

/**
 * The page object.
 */
public class Page extends JPanel {
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- CONSTANTS
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private final int TITLE_TEXT_SIZE    = 38; // Keep these up here so it is easier to change.
    private final Color FOREGROUND_COLOR = new Color(192, 197, 206);
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private Catalog parentCatalog;
    private JLabel  pageTitle;
    private boolean isDrawn = false;
    private String  pageName;
    private GroupLayout pageLayout;


    /**
     * Checks if the page is drawn or not.
     * @return // Returns true if its drawn, false otherwise.
     */
    public boolean isDrawn(){
        return isDrawn;
    }

    /**
     * Get the string representation of the page.
     * @return the string name of the page.
     */
    public String toString(){
        return pageName;
    }

    /**
     * Sets the new page title.
     * @param newTitle the new title of the page.
     */
    public void setPageTitle(String newTitle){
        pageTitle.setText(newTitle);
    }

    /**
     * Sets the page title's color.
     * @param color the color object.
     */
    public void setPageTitleColor(Color color){
        pageTitle.setForeground(color);
    }

    /**
     * Loads the page
     * @param o The page that should be loaded.
     */
    public void loadPage(Page o){
        parentCatalog.loadPage(o);
    }

    /**
     * Draws out the page.
     */
    public void draw(){
        this.setVisible(true);
        this.setEnabled(true);
        isDrawn = true;
    }

    /**
     * Make the page invisible.
     */
    public void undraw(){
        this.setVisible(false);
        this.setEnabled(false);
        isDrawn = false;
    }

    /**
     * Constructor for the page class.
     * @param backgroundColor a color object which sets the background color of the page.
     * @param pageName a string which sets the title of the page.
     * @param parentCatalog the parent catalog object.
     */
    public Page(Color backgroundColor, String pageName, Catalog parentCatalog){
        this.parentCatalog = parentCatalog;
        this.pageName = pageName;
        this.setBackground(backgroundColor);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS)); // Use the box layout, simple and good.

        pageTitle = new JLabel();
        pageTitle.setFont(new Font("Arial", Font.PLAIN, TITLE_TEXT_SIZE));
        pageTitle.setAlignmentX(CENTER_ALIGNMENT); // Align it to the center.
        pageTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pageTitle.setForeground(FOREGROUND_COLOR);
        pageTitle.setSize(this.getWidth(), this.getHeight()/3);
        pageTitle.setText(pageName);

        this.add(pageTitle);
    }
}
