package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @Author Robert Jacob
 * @Version 1.0 24/03/2021
 *
 * FruitTreeAnalyser - simple JavaFX image analyser that locates and outlines fruits with rectangles and returns details about the image,
 * emphasis on union-find operations on disjoint sets to locate and perform operations on the image.
 */

public class Controller implements Initializable {


    private HashSet<Integer> fruits = new HashSet<>();
    private Image image;
    private WritableImage writableImage;
    private PixelReader pr;
    private PixelWriter pw;
    private int imageWidth, imageHeight;
    int[] disjointSetArray;



    @FXML
    public TextField maxHueTextField;
    @FXML
    public TextField minHueTextField;
    @FXML
    public TextField noiseReductionBox;
    @FXML
    public TextField fruitID;
    @FXML
    public Button colorSetButton;
    @FXML
    public TextArea fruitLogistics3;
    @FXML
    public ImageView randomColorImageView;
    @FXML
    public Button colorDisjointStrawberryButton;
    @FXML
    public TextField fruitLogistics2;
    @FXML
    public TextArea fruitLogistics;
    @FXML
    public Button quitButton;
    @FXML
    public AnchorPane secondTab;
    @FXML
    public ImageView rectangleImageView;
    @FXML
    public Button drawRectButton;
    @FXML
    public ImageView imageView2;
    @FXML
    public Button buttonStrawberry;
    @FXML
    public Button buttonOrange;
    @FXML
    public Button buttonPlum;
    @FXML
    public Button buttonSelectFile;
    @FXML
    public ImageView imageView1;


    /**
     * Method to open file dialogue to choose image for analysis.
     */
    public void chooseFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        Stage stage = new Stage();
        File file = fc.showOpenDialog(stage);

        if (file != null) {
            image = new Image(file.toURI().toString(), 216, 216, false, true);
            imageView1.setImage(image);
            pr = image.getPixelReader();
            imageWidth = (int) image.getWidth();
            imageHeight = (int) image.getHeight();
            writableImage = new WritableImage(pr, imageWidth, imageHeight);
            pw = writableImage.getPixelWriter();
        }
    }


    /**
     * Methods convert an image to black and white, locating fruits using Hue and marking fruits as white pixels.
     *
     */
    public void strawberryToBlackWhiteConversion(ActionEvent actionEvent) {
        disjointSetArray = new int[imageWidth * imageHeight];
        int i = 0;
        for (int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {
                Color color = pr.getColor(c, r);

                int min = Integer.parseInt(minHueTextField.getText());
                int max = Integer.parseInt(maxHueTextField.getText());

                if (color.getHue() >= max && color.getHue() < 360 || color.getHue() > 0 && color.getHue() < min) {
                    pw.setColor(c, r, Color.WHITE);
                    disjointSetArray[i] = i;
                } else {
                    pw.setColor(c, r, Color.BLACK);
                    imageView2.setImage(writableImage);
                    disjointSetArray[i] = -1;
                }
                i++;
            }
        }
    }


    public void orangeToBlackWhiteConversion(ActionEvent actionEvent) {
        int i = 0;
        disjointSetArray = new int[imageWidth * imageHeight];
        for (int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {

                Color color = pr.getColor(c, r);
                int min = Integer.parseInt(minHueTextField.getText());
                int max = Integer.parseInt(maxHueTextField.getText());

                if (color.getHue() > min && color.getHue() < max) {
                    pw.setColor(c, r, Color.WHITE);
                    disjointSetArray[i] = i;
                } else {
                    pw.setColor(c, r, Color.BLACK);
                    imageView2.setImage(writableImage);
                    disjointSetArray[i] = -1;
                }
                i++;
            }
        }

    }


    public void plumToBlackWhiteConversion(ActionEvent actionEvent) {

        int i = 0;
        disjointSetArray = new int[imageWidth * imageHeight];

        for (int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {

                Color color = pr.getColor(c, r);
                int min = Integer.parseInt(minHueTextField.getText());
                int max = Integer.parseInt(maxHueTextField.getText());

                if (color.getHue() > min && color.getHue() < max) {
                    pw.setColor(c, r, Color.WHITE);
                    disjointSetArray[i] = i;
                } else {
                    pw.setColor(c, r, Color.BLACK);
                    imageView2.setImage(writableImage);
                    disjointSetArray[i] = -1;
                }
                i++;
            }
        }
    }

    /**
     * Method to randomly color each fruit cluster in a black and white image to display each disjoint set separately.
     */
    public void strawberryRandomColor(ActionEvent actionEvent) {

        Random random = new Random();

        for (int id : fruits) {
            int r = random.nextInt(255);
            int g = random.nextInt(255);
            int b = random.nextInt(255);

            Color color = Color.rgb(r, g, b);
            for (int i = 0; i < disjointSetArray.length; i++) {
                if (disjointSetArray[i] != -1 && find(disjointSetArray, i) == id) {
                    writableImage.getPixelWriter().setColor(i % imageWidth, i / imageWidth, color);
                }
            }
        }
        randomColorImageView.setImage(writableImage);
    }

    /**  NOT FUNCTIONING CORRECTLY
     * Colors single disjoint set on black and white converted image.
     */
    public void colorSingleSet(ActionEvent actionEvent) {
        int i = Integer.parseInt(fruitID.getText());
            for (int j = 0; j < disjointSetArray.length; j++) {
                if (find(disjointSetArray, j) == disjointSetArray[i - 1]) {
                    writableImage.getPixelWriter().setColor(j % imageWidth, j / imageWidth, Color.BLUE);
                }
            }

        randomColorImageView.setImage(writableImage);
    }

    /**
     * Method unions white pixels that belong to a fruit cluster.
     */
    public void clusterGroup() {

        for (int i = 0; i < disjointSetArray.length - 1; i++) {
            if (i < disjointSetArray.length - imageWidth && disjointSetArray[i] != -1 && disjointSetArray[i + imageWidth] != -1) {
                union(disjointSetArray, i, i + imageWidth);
            }
            if (i < disjointSetArray.length && disjointSetArray[i] != -1 && disjointSetArray[i + 1] != -1) {
                union(disjointSetArray, i, i + 1);
            }
        }
        addRootsToWhitePixelSet();
    }

    /**
     * Method takes user input in order to reduce noise in the image.
     */
    public void noiseReduction() {
        double reduction = Integer.parseInt(noiseReductionBox.getText());
        double reductionPercent = reduction / 100;
        fruits.removeIf(t -> ((double) whitePixelsPerFruit(t) / disjointSetArray.length) * 100.0 < reductionPercent);
    }


    private void addRootsToWhitePixelSet() {//adds all roots to the Set of white pxl
        for (int id = 0; id < disjointSetArray.length; id++) {
            int parent = find(disjointSetArray, id);
            if (parent != -1)
                fruits.add(parent);
        }
    }

    /**
     * Method draws blue rectangles around fruit clusters with a sequential label with the fruits number, reports amount of pixels
     * per fruit and the id given to each fruit.
     */
    public void drawRectangle() {
        noiseReduction();
        int sequentialFruits = 0;

        for (int id : fruits) {

            sequentialFruits++;
            double maxHeight = -1, minHeight = -1, left = -1, right = -1;

            for (int i = 0; i < disjointSetArray.length; i++) {
                int x = i % imageWidth;
                int y = i / imageWidth;

                if (disjointSetArray[i] != -1 && find(disjointSetArray, i) == id) {
                    if (maxHeight == -1) {
                        maxHeight = minHeight = y;
                        left = right = x;
                    } else {
                        if (x < left)
                            left = x;
                        if (x > right)
                            right = x;
                        if (y > minHeight)
                            minHeight = y;
                    }
                }
            }
            whitePixelsPerFruit(id);
            fruitLogistics.appendText("Fruit ID:  " + id + "  Pixel Count:  " + whitePixelsPerFruit(id) + "\n");
            fruitLogistics3.appendText("Fruit ID:  " + id + "  Pixel Count:  " + whitePixelsPerFruit(id) + "\n");
            System.out.println(whitePixelsPerFruit(id));
            rectangleImageView.setImage(image);
            Rectangle fruitBox = new Rectangle(left, maxHeight, right - left, minHeight - maxHeight);
            fruitBox.setTranslateX(rectangleImageView.getLayoutX());
            fruitBox.setTranslateY(rectangleImageView.getLayoutY());
            ((AnchorPane) rectangleImageView.getParent()).getChildren().add(fruitBox);
            fruitBox.setStroke(Color.BLUE);
            fruitBox.setFill(Color.TRANSPARENT);

            Label fruitLabel = new Label();
            fruitLabel.setTranslateX(rectangleImageView.getLayoutX());
            fruitLabel.setTranslateY(rectangleImageView.getLayoutY());
            ((AnchorPane) rectangleImageView.getParent()).getChildren().addAll(fruitLabel);
            fruitLabel.setLayoutX(right + 1);
            fruitLabel.setLayoutY(maxHeight);
            fruitLabel.setTextFill(Color.WHITE);
            fruitLabel.setText(String.valueOf(sequentialFruits));
            //fruitLabel.setText("Fruit ID:  " + id);
        }
    }

    /**
     * Method removes all rectangles and labels on the anchor pane.
     */
    public void clearClusters(ActionEvent actionEvent) {
        List<Rectangle> rectList = new ArrayList<>();
        for (Node x : ((AnchorPane) rectangleImageView.getParent()).getChildren()) {
            if (x instanceof Rectangle)
                rectList.add((Rectangle) x);
        }
        ((Pane) rectangleImageView.getParent()).getChildren().removeAll(rectList);

        List<Label> labelList = new ArrayList<>();
        for (Node y : ((AnchorPane) rectangleImageView.getParent()).getChildren()) {
            if (y instanceof Label)
                labelList.add((Label) y);
        }
        ((Pane) rectangleImageView.getParent()).getChildren().removeAll(labelList);
    }

    /**
     * Quick union for disjoint sets.
     */
    public static void union(int[] a, int p, int q) {
        a[find(a, q)] = find(a, p); //The root of q is made reference p
    }

    /**
     * Iterative find method with path compression for white pixels in disjoint set.
     */
    public static int find(int[] a, int id) {
        if (a[id] == -1) {
            id = -1;
            return id;
        }
        while (a[id] != id) {
            a[id] = a[a[id]];
            id = a[id];
        }
        return id;
    }

    /**
     * Method calls methods to print rectangles and labels.
     */
    public void markCluster(ActionEvent actionEvent) {
        clusterGroup();
        drawRectangle();
        printFruitLogistics(actionEvent);
    }

//    public void testPrint(ActionEvent actionEvent) {
//        clusterGroup();
//        for (int i = 0; i < disjointSetArray.length - 1; i++) {
//            System.out.print(find(disjointSetArray, i) + ((i + 1) % imageWidth == 0 ? "\n" : " "));
//        }
//    }

    /**
     * Method counts the number of pixels per disjoint set.
     */
    public double whitePixelsPerFruit(int parent) {
        double pixels = 0;
        for (int i = 0; i < disjointSetArray.length; i++) {
            if (parent == find(disjointSetArray, i)) {
                pixels++;
            }
        }
        return pixels;
    }

    /**
     * Method prints disjoint sets statistics.
     */
    public void printFruitLogistics(ActionEvent actionEvent) {
        fruitLogistics2.setText("Fruit count:  " + fruits.size());
    }

    /**
     * Method exits the program.
     */
    public void quit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public HashSet<Integer> getFruits() {
        return fruits;
    }

    public void setFruits(HashSet<Integer> fruits) {
        this.fruits = fruits;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
