import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * The picture class.
 */
public class Picture extends JLabel{
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-- OBJECT VALUES.
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private String iconName;
    private String imageIdentifier, imageName, imageFileType;
    private String parsedImageLocation;
    private ImageIcon imageIcon;
    private Dimension imageSize, labelSize;


    /**
     * parses the location.
     * @param identifier the root directory.
     * @param pictureName the picture name.
     * @param fileType the extension of the file.
     */
    private void parseLocation(String identifier, String pictureName, String fileType){
        parsedImageLocation = String.format("/resources/%s/%s.%s", identifier, pictureName, fileType);
    }

    /**
     * Gets the string representation of the picture.
     * @return string representing the picture's name
     */
    public String toString(){
        return iconName;
    }

    /**
     * Gets the size of the picture.
     * @return A dimension object holding the picture size.
     */
    public Dimension getLabelSize(){
        return new Dimension(labelSize);
    }

    /**
     * The size of the icon.
     * @return A dimension object holding the icon size.
     */
    public Dimension getImageSize() {
        return new Dimension(imageSize);
    }

    /**
     * Constructor for the Picture object.
     * @param identifier The root directory of the object.
     * @param pictureName The picture's name.
     * @param fileType The file type of the image.
     * @param imageSize The icon size.
     * @param labelSize The label size.
     */
    public Picture(String identifier, String pictureName, String fileType, Dimension imageSize, Dimension labelSize){
        iconName = pictureName;
        this.setOpaque(false);
        this.setSize(labelSize);
        // Set the dimensions.
        labelSize = new Dimension(labelSize);
        imageSize = new Dimension(imageSize);
        this.setHorizontalAlignment(JLabel.CENTER); // Align to the center.
        this.setVerticalAlignment(JLabel.CENTER);
        // Set the size
        this.setMinimumSize(labelSize);
        this.setMaximumSize(labelSize);
        parseLocation(identifier, pictureName, fileType); // Parse the location.
        imageIcon = new ImageIcon(new ImageIcon(getClass().getResource(this.parsedImageLocation)).getImage().getScaledInstance((int)imageSize.getWidth(), (int)imageSize.getHeight(), Image.SCALE_SMOOTH)); // Set the image and scale it.
        this.setIcon(imageIcon); // Set the icon to the image icon.
    }
}
