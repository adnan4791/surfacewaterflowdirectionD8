package algoritma;

import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

    // MY COMPONENT AND VARIABLES
    public static TextArea textArea01;
    public static TextArea textArea02;
    PannableCanvas canvas;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        GridPane gridPane01 = new GridPane();
        gridPane01.setHgap(8);
        gridPane01.setVgap(8);
        gridPane01.setPadding(new Insets(10));

        ColumnConstraints cConst1 = new ColumnConstraints();
        cConst1.setHgrow(Priority.NEVER);

        ColumnConstraints cConst2 = new ColumnConstraints();
        cConst2.setHgrow(Priority.ALWAYS);

        gridPane01.getColumnConstraints().addAll(cConst1, cConst2, cConst1, cConst2);

        //==========//
        Label label01 = new Label("File Data");
        TextField textField01 = new TextField();
        Button button01 = new Button("Browse");
        Button button02 = new Button("Run-D8");

        // Tambahkan komponen ke GridPane01
        gridPane01.add(label01, 0, 0);
        gridPane01.add(textField01, 1, 0, 3, 1);
        gridPane01.add(button01, 4, 0);
        gridPane01.add(button02, 5, 0);

        // Tambahkan  grid pane ke root
        root.setTop(gridPane01);

        // AKSI BUTTON
        button01.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // Aksi tombol
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("E:\\07. My Data\\datauji\\Uji\\dataascii"));
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                        new FileChooser.ExtensionFilter("CSV Files", "*.csv")
                );
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile != null) {
                    textField01.setText(selectedFile.getAbsolutePath());
                    bacaData(selectedFile);
                    System.out.println("File Selected : " + selectedFile.toString());
                } else {
                    System.out.println("Open Command Canceled");
                }
            }
        }
        );

        button02.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Run-D8");
                pencarianFlowDirection();
            }
        });

        // MEMBUAT TAB PANE
        TabPane tabPane01 = new TabPane();
        tabPane01.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        tabPane01.setSide(Side.BOTTOM);

        // Tab 1 : ------------------------------------------------------------1
        Tab tab01 = new Tab();
        tab01.setText("Data");
        textArea01 = new TextArea();
        tab01.setContent(textArea01);

        // Tab 2 : ------------------------------------------------------------1
        Tab tab02 = new Tab();
        tab02.setText("Flow Direction");
        textArea02 = new TextArea();
        tab02.setContent(textArea02);

        // Set element tabPane01
        tabPane01.getTabs().addAll(tab01, tab02);
        root.setCenter(tabPane01);

        // SET SCENE
        Scene scene = new Scene(root, 1000, 700);

        SceneGestures sceneGestures = new SceneGestures(canvas);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        primaryStage.setTitle("Water Flow Direction");
        primaryStage.setScene(scene);
        primaryStage.show();
    } // END OF START

    // =========================================================================
    // MY METHODE AND VARIABLES
    //private int ncols;
    //private int nrows;
    public static double[][] data = null;
    private int[][] flowDirection = null;

    private void bacaData(File file) {
        BacaData bfn = new BacaData(file);
        bfn.waitData();
    }

    // Hitung Waktu Komputasi =================================================>
    double start = System.currentTimeMillis();
    int l = 0;

    private void pencarianFlowDirection() {
        textArea02.setText("");
        if (data != null) {
            // PROSES PENCARIAN
            // SINGLE FLOW DIRECTION D8
            flowDirection = new int[data.length][data[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {

                    int arahR1 = 0;
                    double MIN = Double.MAX_VALUE; // Inisialisasi Titik Terendah

                    //D1
                    int I = i - 1;
                    int J = j;
                    if (I >= 0 && I < data.length && J >= 0 && J < data[i].length && data[I][J] < MIN) {
                        MIN = data[I][J];
                        arahR1 = 1;
                    }

                    //D2
                    I = i - 1;
                    J = j + 1;
                    if (I >= 0 && I < data.length && J >= 0 && J < data[i].length && data[I][J] < MIN) {
                        MIN = data[I][J];
                        arahR1 = 2;
                    }

                    //D3
                    I = i;
                    J = j + 1;
                    if (I >= 0 && I < data.length && J >= 0 && J < data[i].length && data[I][J] < MIN) {
                        MIN = data[I][J];
                        arahR1 = 3;
                    }

                    //D4
                    I = i + 1;
                    J = j + 1;
                    if (I >= 0 && I < data.length && J >= 0 && J < data[i].length && data[I][J] < MIN) {
                        MIN = data[I][J];
                        arahR1 = 4;
                    }

                    //D5
                    I = i + 1;
                    J = j;
                    if (I >= 0 && I < data.length && J >= 0 && J < data[i].length && data[I][J] < MIN) {
                        MIN = data[I][J];
                        arahR1 = 5;
                    }

                    //D6
                    I = i + 1;
                    J = j - 1;
                    if (I >= 0 && I < data.length && J >= 0 && J < data[i].length && data[I][J] < MIN) {
                        MIN = data[I][J];
                        arahR1 = 6;
                    }

                    //D7
                    I = i;
                    J = j - 1;
                    if (I >= 0 && I < data.length && J >= 0 && J < data[i].length && data[I][J] < MIN) {
                        MIN = data[I][J];
                        arahR1 = 7;
                    }

                    //D8
                    I = i - 1;
                    J = j - 1;
                    if (I >= 0 && I < data.length && J >= 0 && J < data[i].length && data[I][J] < MIN) {
                        MIN = data[I][J];
                        arahR1 = 8;
                    }

                    // TEST PROGRAM
                    // SET ARAH ALIRAN
                    int arah = 0;

                    // Set Ring 1
                    if (MIN < data[i][j]) {
                        arah = arahR1;
                    }

                    // Set FlowDirection
                    // SET ARAH ALIRAN
                    flowDirection[i][j] = 0;
                    if (MIN < data[i][j]) {
                        flowDirection[i][j] = arah;
                    }
                    System.out.println("Track Data Ke - " + i);
                }
            } // END OF FOR i

            // COBA CETAK FLOW DIRECTION            
            StringBuffer sb = new StringBuffer();
            if (flowDirection != null) {
                // COBA CETAK ISI DATA
                for (int i = 0; i < flowDirection.length; i++) {
                    for (int j = 0; j < flowDirection[i].length; j++) {
                        sb.append(flowDirection[i][j] + "\t");
                    }
                    sb.append("\n");
                }
                textArea02.setText(sb.toString());
            }
        }

        double finish = System.currentTimeMillis();
        System.out.println("waktu eksekusi program: " + (finish - start) / 1000 + " seconds");
        // END Hitung Waktu Komputasi =========================================>
    }

    // =========================================================================
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
