import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import java.util.Optional;
public class FileViewer extends Application
{
    private Optional<File> currentFile;
    private Stage primary;
    private TextArea display;
    public FileViewer()
    {
        currentFile = Optional.empty();
        display = new TextArea();
        display.setEditable(true);
        display.setFont(Font.font("courier", 12));
        display.setStyle("-fx-control-inner-background:#FFF8E7;");
    }

    @Override
    public void start(Stage primary)
    {
        this.primary = primary;
        BorderPane bp = new BorderPane();
        bp.setTop(buildMenus());
        bp.setCenter(display);
        Scene s = new Scene(bp, 700,500);
        primary.setTitle("FileViewer: Empty");
        primary.setScene(s);
        primary.show();
    }
    private void showFile()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(currentFile.get()));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ( (line = br.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            br.close();
            display.setText(sb.toString());
        }
        catch(FileNotFoundException ex)
        {
            System.err.printf("File %s not found\n", currentFile.get().getAbsolutePath());
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    private void saveFile()
    {
        String[] fileContents = display.getText().split("\n");
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(currentFile.get()));
            for(int i = 0; i < fileContents.length; i++)
            {
               bw.write(fileContents[i]);
               bw.newLine();
               bw.flush();
            }
        }
        catch(FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    private MenuBar buildMenus()
    {
        MenuBar mbar = new MenuBar();
        Menu fileMenu = new Menu("File");// somebody figure out keyboard accelearators
        mbar.getMenus().addAll(fileMenu);
        MenuItem openItem = new MenuItem("Open...");
        openItem.setOnAction( e ->
        {
            FileChooser fc = new FileChooser();
            fc.setTitle("Open a File");
            File selected = fc.showOpenDialog(primary);
            if(selected != null)
            {
                currentFile = Optional.of(selected);
                primary.setTitle("FileViewer:  " + selected.getAbsolutePath());
                showFile();
            }
        });
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction( e ->
        {
            saveFile();
        });
        MenuItem quitItem = new MenuItem("Quit");
        fileMenu.getItems().addAll(openItem, saveItem, quitItem);
        quitItem.setOnAction( e -> Platform.exit());
        return mbar;
    }
}

