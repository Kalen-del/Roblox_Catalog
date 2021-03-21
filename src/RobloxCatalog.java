import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Catalog Object For the Roblox Catalog.
 */
public class RobloxCatalog extends Catalog {

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private Navigation featuredNavigation, gearNavigation, winterNavigation;
    private Picture    navigationIcon;


    /**
     * Called upon start up.
     */
    public void setUp(){
        // Constants
        final Color DEFAULT_PAGE_COLOR = new Color(111, 124, 133);

        super.setTitle("Roblox Catalog");

        // Constants:
        final Color PAGE_COLOR                = new Color(192,197,206);
        final Color NAVIGATION_INACTIVE_COLOR = new Color(50,57,67);
        final Color NAVIGATION_ACTIVE_COLOR   = new Color(79,91,102);
        //
        navigationIcon = new Picture("sitepictures", "logo", "png", new Dimension(120, 120), new Dimension(200, 200));
        addToNavigationPanel(navigationIcon, new Dimension(0, 60));

        // Set up the windows icon.
        URL iconUrl = this.getClass().getResource("resources/sitepictures/icon.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(iconUrl));

        FeaturedPage homePage;
        SearchablePage winterCatalog, summerCatalog;
        // Set up the items list.
        ArrayList<Item> winterCatalogItems = new ArrayList<Item>(
                Arrays.asList(
                        new Item(
                                "Orange Winter Scarf",
                                "The bright spot in your weary winter day.",
                                "Roblox",
                                25,
                                10,
                                new String[]{"Scarf", "Winter", "Roblox", "Neck"},
                                true,
                                3
                        ),
                        new Item(
                                "Puppy Wagon",
                                "The cutest companion for your travels!",
                                "Lukacors",
                                60,
                                3,
                                new String[]{"Accessory", "Animals", "Dog"},
                                true,
                                3
                        ),
                        new Item(
                                "Orange Messy Wavy Hair",
                                "This is some sick looking hair!",
                                "Homemade_Meal",
                                80,
                                40,
                                new String[]{"Accessory", "Hair", "Head"},
                                false,
                                0
                        ),
                        new Item(
                                "Purple Winter Scarf",
                                "Brr... it's cold in here! There must be some winter in the at-mos-phere!",
                                "ROBLOX",
                                95,
                                200,
                                new String[]{"Accessory", "Scarf", "Winter"},
                                true,
                                5
                        ),
                        new Item(
                                "Wild Neon Blue Scarf",
                                "You know it's not made from real animals because you can't find that color in nature!",
                                "ROBLOX",
                                70,
                                6,
                                new String[]{"Accessory", "Neck", "Winter"},
                                true,
                                5
                        ),
                        new Item(
                                "Golden Neck Headphones",
                                "Everyone knows around your neck is the coolest place to keep your headphones",
                                "ROBLOX",
                                35,
                                78,
                                new String[]{"Accessory", "Neck", "Headphones"},
                                true,
                                20
                        )
                )
        );

        // Create the object array lists.
        ArrayList<Item> gearCatalogItems = new ArrayList<Item>(
                Arrays.asList(
                        new Item(
                                "Golden Super Fly Boombox",
                                "Nothin' but the hits. Play the hottest jams on ROBLOX all over town.",
                                "ROBLOX",
                                500,
                                20,
                                new String[]{"Gear", "Musical", "Social"},
                                true,
                                5
                        ),
                        new Item(
                                "Gravity Coil",
                                "Equipping this item cancels 75% of gravity's effect on your character.",
                                "ROBLOX",
                                250,
                                10,
                                new String[]{"Gear", "Navigation", "Gravity"},
                                true,
                                5
                        ),
                        new Item(
                                "Red Convertible",
                                "Zip around the streets of ROBLOX in your sweet red convertible.",
                                "ROBLOX",
                                10000,
                                4,
                                new String[]{"Vehicle", "Car", "Red", "Red Car"},
                                true,
                                2
                        ),
                        new Item(
                                "Robot Guard Dog",
                                "More loyal than Hachiko, this guy will go after anyone who gets too close.",
                                "ROBLOX",
                                600,
                                10,
                                new String[]{"Pet", "Dog", "Robot", "Guard"},
                                true,
                                2
                        )
                )
        );

        // Merge the two item lists together.
        ArrayList<Item> featuredCatalogItems = new ArrayList<>();
        featuredCatalogItems.addAll(winterCatalogItems);
        featuredCatalogItems.addAll(gearCatalogItems);


        // Set up the navigation buttons.
        featuredNavigation = new Navigation("Featured", new Font("Arial", Font.PLAIN, 24), PAGE_COLOR, NAVIGATION_ACTIVE_COLOR, NAVIGATION_INACTIVE_COLOR, new FeaturedPage(DEFAULT_PAGE_COLOR, "FEATURED",this, featuredCatalogItems));
        gearNavigation     = new Navigation("Gear", new Font("Arial", Font.PLAIN, 24), PAGE_COLOR, NAVIGATION_ACTIVE_COLOR, NAVIGATION_INACTIVE_COLOR, new SearchablePage(DEFAULT_PAGE_COLOR, "GEAR CATALOG",this,  gearCatalogItems));
        winterNavigation   = new Navigation("Winter",    new Font("Arial", Font.PLAIN, 24), PAGE_COLOR, NAVIGATION_ACTIVE_COLOR, NAVIGATION_INACTIVE_COLOR, new SearchablePage(DEFAULT_PAGE_COLOR, "WINTER CATALOG", this, winterCatalogItems));
        repaintNavigationFrame(); // Repaint the frame
        addToNavigationPanel(featuredNavigation, new Dimension(0, 5), true);
        addToNavigationPanel(gearNavigation, new Dimension(0, 5), false);
        addToNavigationPanel(winterNavigation,    new Dimension(0, 5), false);
        addPage(gearNavigation.getPage());
        addPage(featuredNavigation.getPage());
        addPage(winterNavigation.getPage());
        setNavigationColor(NAVIGATION_INACTIVE_COLOR); // Set the navigation color.
    }


}
