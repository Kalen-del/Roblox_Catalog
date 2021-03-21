import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * The root class for the catalog application.
 * @author Li
 * Helpful links:
 * https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
 */
public abstract class Catalog extends JFrame {
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- CONSTANTS
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private final int PAGE_STACK_THRESHOLD       = 5;
    private final int PAGE_WIDTH                 = 1024, PAGE_HEIGHT = 768;
    private final int PREF_LENGTH                = 1244;
    private final int DIVIDER_WIDTH              = 200;
    private final Color PANEL_BACKGROUND_COLOR            = new Color(79, 91, 102);
    private final Color NAVIGATION_PANEL_BACKGROUND_COLOR = new Color(50,57,67);

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES.
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ArrayList<Navigation> navigationList = new ArrayList<>();
    private ArrayList<Page>       pageList       = new ArrayList<>();
    private JPanel NavigationFrame;
    private JSplitPane SplitPane;
    private JLabel logoIcon;


    /**
     * loads a page
     * @param o the page that should be loaded.
     */
    public void loadPage(Page o){
        if (o != null) {
            for (Page page : pageList) {
                if (page.isDrawn()) {
                    page.undraw();
                }
            }
            o.draw();
            SplitPane.setRightComponent(o);
        }
    }

    /**
     * Switches The Page Of The Navigation.
     * @param o The navigation button object.
     */
    private void switchNavigationPage(Navigation o){
        if(o.getPage() == null){ // If there is no linked page for navigation.
            System.err.printf("NAVIGATION ERROR: %s LINKED TO NULL\n", o.toString()); // Error
            System.err.println(Arrays.toString(Thread.currentThread().getStackTrace()).replace( ',', '\n' ));
            System.exit(-1);
        }
        // Set all navigators to inactive color.
        for (Navigation navigator: navigationList){
            navigator.setActiveStatus(false);
        }
        // Set the current navigation button to active.
        o.setActiveStatus(true);

        // Load the object.
        loadPage(o.getPage());

    }

    /**
     * Add page object to Catalog.
     * @param o the page object.
     */
    public void addPage(Page o){
        this.pageList.add(o);
    }

    /**
     * add navigator to the catalog.
     * @param o The navigation button.
     * @param space The space in between.
     * @param isDefault If the navigation button is the default.
     */
    public void addToNavigationPanel(Navigation o, Dimension space, boolean isDefault){
        this.navigationList.add(o);
        NavigationFrame.add(o);
        NavigationFrame.add(Box.createRigidArea(space));
        NavigationFrame.repaint();
        if (isDefault){ // If its default.
            switchNavigationPage(o); // Start up the page
        }
        o.addActionListener(new ActionListener() { // Listen to object click.
            public void actionPerformed(ActionEvent e) {
                switchNavigationPage((Navigation) e.getSource());
            }
        });
    }

    /**
     * Adds an image to the navigation panel.
     * @param image The picture object.
     * @param space The space that should put.
     */
    public void addToNavigationPanel(Picture image, Dimension space) {
        NavigationFrame.add(image);
        NavigationFrame.add(Box.createRigidArea(space));
        NavigationFrame.repaint();
    }

    /**
     * Repaint the navigation panel.
     */
    public void repaintNavigationFrame(){
        NavigationFrame.repaint();
    }

    /**
     * Set the navigation color.
     * @param newColor the new color.
     */
    public void setNavigationColor(Color newColor){
        NavigationFrame.setBackground(newColor);
    }

    /**
     * Force child classes to call this.
     */
    public abstract void setUp();

    /**
     * Constructor for the catalog object.
     */
    public Catalog() {
        initComponents(); // Start up the components.
        this.setUp(); // Call setup.
    }

    /**
     * Sets up all the components.
     */
    private void initComponents() {

        // Use split pane.
        SplitPane = new JSplitPane();
        NavigationFrame = new JPanel();
        JPanel contentFrame = new JPanel();
        JPanel backgroundFrame = new JPanel();
        JPanel featuredJPanel = new JPanel();

        // Set up the close operation.
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(79, 91, 102));
        setMaximumSize(new Dimension(PAGE_WIDTH, PAGE_HEIGHT));
        setMinimumSize(new Dimension(PAGE_WIDTH, PAGE_HEIGHT));
        setPreferredSize(new Dimension(PAGE_WIDTH, PAGE_HEIGHT));

        // Set the border to nothing.
        SplitPane.setBorder(null);
        SplitPane.setDividerLocation(DIVIDER_WIDTH);
        SplitPane.setDividerSize(0); // Set the divider size to nothing.
        SplitPane.setMaximumSize(new Dimension(PAGE_WIDTH, PAGE_HEIGHT));
        SplitPane.setPreferredSize(new Dimension(PAGE_WIDTH, PAGE_HEIGHT));

        NavigationFrame.setBackground(NAVIGATION_PANEL_BACKGROUND_COLOR);
        NavigationFrame.setForeground(NAVIGATION_PANEL_BACKGROUND_COLOR);
        NavigationFrame.setFocusable(true);
        NavigationFrame.setLayout(new javax.swing.BoxLayout(NavigationFrame, javax.swing.BoxLayout.PAGE_AXIS));

        SplitPane.setLeftComponent(NavigationFrame);


        backgroundFrame.setBackground(PANEL_BACKGROUND_COLOR);




        SplitPane.setRightComponent(contentFrame); // Set the right component.

        GroupLayout layout = new GroupLayout(getContentPane()); // Set the group layout.
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup( // Set the horizontal group.
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(SplitPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, PREF_LENGTH, Short.MAX_VALUE) // Set up the pref length of the alignment.
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(SplitPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Set the vertical group.
        );

        pack();
        setLocationRelativeTo(null); // Center the application
    }

}
