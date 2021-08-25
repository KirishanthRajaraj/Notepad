/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad3;

import static com.sun.corba.se.impl.util.Utility.printStackTrace;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.swing.JFileChooser;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 *
 * @author rajar
 */
public class FXMLNotepadController implements Initializable {

    @FXML
    private Button createButton;
    @FXML
    private TextArea ta;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private ImageView SidebarNav;

    //Stage stage = (Stage) AnchorPane.getScene();
    @FXML
    private AnchorPane Sidebar;
    @FXML
    private ImageView SidebarNavClose;

    undoableEditHappened undo = new undoableEditHappened();

    String Filename;
    String Filepath;
    FileWriter writer;
    File file = new File("blank.txt");
    String path;
    BufferedReader reader;
    Button existingFileButton;

    undoableEditHappened ueh = new undoableEditHappened();
    @FXML
    private VBox vbox;
    @FXML
    private Label notebooklbl;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        /*vbox = new SideBarView(borderPane);

        ScrollPane sideBarScroller = new ScrollPane(sideBar);
        sideBarScroller.setFitToWidth(true);

        borderPane.setRight(sideBarScroller);*/
        /*ta.textProperty().getDocument().new UndoableEditListener(){
        public void undoableEditHappened(UndoableEditEvent e){
            um.addEdit(e.getEdit());
        }
    });*/
        //ta.addUndoableEditListener(ueh);
    }

    @FXML
    private void NewAction(ActionEvent event) {
        New();
        createNewNote();
    }

    public void New() {
        ta.setText("");
        //stage.setTitle("new");
        Filename = null;
        Filepath = null;
    }

    @FXML
    private void OpenAction(ActionEvent event) {
        Open();
        createNewNote();
    }

    public void Open() {
        /*
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\"));
        File file = fc.showOpenDialog(null);
        Filename = fc.getInitialFileName();
        if (file != null) {*/
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("C:\\"));
        fileChooser.setDialogTitle("Open");
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            Filename = file.getName();
            Filepath = file.getAbsolutePath();
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                ta.clear();
                while (line != null) {
                    ta.appendText(line + "\n");
                    System.out.println(line);
                    // read next line
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //existingFileButton.setText(Filename);
    }

    @FXML
    private void SaveAction(ActionEvent event) {
        Save();
    }

    public void Save() {
        if (Filename == null) {
            SaveAs();
        } else {
            try {
                FileWriter writer = new FileWriter(path, true);
                writer.write(ta.getText());
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(FXMLNotepadController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void SaveAsAction(ActionEvent event) {
        /*FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TEXT FILES", "txt", "text");
        fc.setSelectedExtensionFilter(filter);

        file = fc.showSaveDialog(null);
        if (file != null) {
            Filename = file.getName();
            Filepath = file.getAbsolutePath();
            //stage.setTitle(Filename);*/

        SaveAs();
    }

    public void SaveAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("C:\\"));
        fileChooser.setDialogTitle("Save");
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            Filename = file.getName();
            Filepath = file.getAbsolutePath();
            try {
                path = file.getPath() + ".txt";
                FileWriter writer = new FileWriter(path, true);
                writer.write(ta.getText());
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(FXMLNotepadController.class.getName()).log(Level.SEVERE, null, ex);
            }
            createNewNote();
            existingFileButton.setText(Filename);
        }
    }

    @FXML
    private void CloseAction(ActionEvent event) {
        Close();
    }

    public void Close() {
        System.exit(1);
    }

    @FXML
    private void taOnKeyPressed(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.S) {
            if (Filename == null) {
                SaveAs();
            } else {
                Save();
            }
        } else if (event.isControlDown() && event.getCode() == KeyCode.N) {
            New();
            createNewNote();
        } else if (event.isControlDown() && event.getCode() == KeyCode.E) {
            Close();
        } else if (event.isControlDown() && event.getCode() == KeyCode.O) {
            Open();
        }
    }

    @FXML
    private void CreateAction(ActionEvent event) {
        createNewNote();
    }

    public void createNewNote() {
        existingFileButton = new Button();
        existingFileButton.setText("new note");
        existingFileButton.setOnAction((ActionEvent a) -> openExisting());
        existingFileButton.setOnMouseClicked((MouseEvent m) -> System.out.println("checked"));
        /*EventHandler<ActionEvent> onAction = existingFile.getOnAction();
        existingFile.setOnAction(e ->{
            onAction.handle(e);
            OpenExistingAction(e);
        });*/
        existingFileButton.getStyleClass().add("buttonExistingFile");
        /*vbox.setPadding(new Insets(10, 50, 50, 50));*/
        vbox.getChildren().add(existingFileButton);
        existingFileButton.setTranslateX(notebooklbl.getTranslateX());
        existingFileButton.setTranslateY(notebooklbl.getTranslateY());
    }

    public void openExisting() {
        if (Filename != null) {
            if (file != null) {
                try {
                    reader = new BufferedReader(new FileReader(path));
                    String line = reader.readLine();
                    ta.clear();
                    while (line != null) {
                        ta.appendText(line + "\n");
                        System.out.println(line);
                        // read next line
                        line = reader.readLine();
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void UndoAction(ActionEvent event) {
        undo.um.undo();
    }

    @FXML
    private void RedoAction(ActionEvent event) {
    }

    @FXML
    private void MenuNavReleased(MouseEvent event) {
        //SidebarNavClose.setImage(new Image("C:\\Pers\\Projekte\\Notepad3\\src\\notepad3\\threelines.png"));
        TranslateTransition slide = new TranslateTransition();
        //TranslateTransition taSlide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(Sidebar);
        //taSlide.setNode(ta);

        //taSlide.setToX(0);
        slide.setToX(0);
        //taSlide.play();
        slide.play();

        Sidebar.setTranslateX(-176);
        //ta.setTranslateX(-176);
        slide.setOnFinished((ActionEvent e) -> {
            SidebarNav.setVisible(false);
            SidebarNavClose.setVisible(true);
        });
        System.out.println(ta.getTranslateX());
        ta.setScaleX(1);
    }

    @FXML
    private void MenuNavPressed(MouseEvent event) {
        //SidebarNavClose.setImage(new Image("C:\\Pers\\Projekte\\Notepad3\\src\\notepad3\\threelinesPressed.png"));
    }

    @FXML
    private void MenuNavCloseReleased(MouseEvent event) {
        //SidebarNav.setImage(new Image("C:\\Pers\\Projekte\\Notepad3\\src\\notepad3\\threelines.png"));
        TranslateTransition slide = new TranslateTransition();
        //TranslateTransition taSlide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        //taSlide.setDuration(Duration.seconds(0.4));
        slide.setNode(Sidebar);
        //taSlide.setNode(ta);

        slide.setToX(-176);
        //taSlide.setToX(-176);
        slide.play();
        //taSlide.play();

        Sidebar.setTranslateX(0);
        //ta.setTranslateX(0);
        slide.setOnFinished((ActionEvent e) -> {
            SidebarNav.setVisible(true);
            SidebarNavClose.setVisible(false);
        });
        System.out.println(ta.getTranslateX());
        //ta.setScaleX(1.9);
    }

    @FXML
    private void MenuNavClosePressed(MouseEvent event) {
        //SidebarNav.setImage(new Image("C:\\Pers\\Projekte\\Notepad3\\src\\notepad3\\threelinesPressed.png"));
    }

    @FXML
    private void beispiel(MouseEvent event) {
        
    }

}
