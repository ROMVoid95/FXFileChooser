package net.raumzeitfalle.fx.filechooser;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.testfx.framework.junit5.ApplicationTest;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

abstract class FileChooserStageTestBase extends ApplicationTest {

	protected Stage primaryStage;
	
	protected abstract PathFilter getPathFilter();
	
	protected abstract Skin getSkin();
	
	protected abstract Path getStartDirectory();

	@Override 
	public void start(Stage stage) {
		primaryStage = stage;
        FileChooserModel model = FileChooserModel.startingIn(getStartDirectory(), getPathFilter());
        FXDirectoryChooser dirChooser = FXDirectoryChooser.createIn(model.currentSearchPath(), ()->stage.getOwner());
        Parent view = null;
        try {
 			view = new FileChooserView(dirChooser, ()->stage.close(), model, getSkin(), FileChooserViewOption.STAGE);
 		} catch (IOException e) {
			Label errorLabel = new Label("Could not load FileChooserView.");
			errorLabel.setTextFill(Color.WHITE);
			StackPane pane = new StackPane();
			Rectangle rect = new Rectangle();
			rect.setFill(Color.RED);
			rect.widthProperty().bind(pane.widthProperty());
			rect.heightProperty().bind(pane.heightProperty());
			pane.getChildren().add(rect);
			pane.getChildren().add(errorLabel);
			view = pane;
		}
        
        Scene scene = new Scene(view, 700, 500);
        stage.setScene(scene);
        stage.show();
        
    }
	
	protected void captureImage(Parent put, String filename) {
		BufferedImage bImage = SwingFXUtils.fromFXImage(capture(put).getImage(), null);
        try {
			ImageIO.write(bImage, "png", new File(filename));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
