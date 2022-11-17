package algoritma;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public class PannableCanvas extends Pane {
    
    private double[] xPoints = new double[3];
    private double[] yPoints = new double[3];

    DoubleProperty myScale = new SimpleDoubleProperty(1.0);

    public PannableCanvas() {
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
    }

    public void drawFlowDirection(int[][] flowDirection) {
        if (flowDirection != null) {
            double cellSize = 18; // Lebar Cell
            double arrowSize = 6; // Panjang Sisi Arrow
            double h = cellSize * flowDirection.length;
            double w = cellSize * flowDirection[0].length;
            Canvas grid = new Canvas(w, h);

            // Don't Catch mouse Events
            grid.setMouseTransparent(true);

            GraphicsContext gc = grid.getGraphicsContext2D();
            gc.clearRect(0, 0, grid.getWidth(), grid.getHeight());
            gc.setFill(Color.WHITESMOKE);
            gc.fillRect(0, 0, grid.getWidth(), grid.getHeight());

            try {
                Image image = new Image(getClass().getResourceAsStream("peta/11_1.PNG"));
                gc.drawImage(image, 0, 0, w, h);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < flowDirection.length; i++) {
                for (int j = 0; j < flowDirection[i].length; j++) {
                    int arah = flowDirection[i][j];
                    if (arah >= 0 && arah <= 8) {
                        //if (arah >= 0 && arah <= 24) {
                        Color c = Color.valueOf("#ff4757");
                        gc.setFill(c);
                        gc.setStroke(c);
                        double xo = cellSize * j;
                        double yo = cellSize * i;
                        //gc.fillRect(xo, yo, cellSize, cellSize);
                        drawArrow(gc, xo, yo, arah, cellSize, arrowSize);
                    }

                    System.out.println(i);
                }
            }
            getChildren().add(grid);
            grid.toBack();
        }
    } // END OF drawFlowDirection-----------------------------------------------

    private void drawArrow(GraphicsContext gc, double xo, double yo, int arah, double cellSize, double arrowSize) {
        double r = cellSize / 2.0;
        double d = Math.sqrt(Math.pow(r, 2) / 2.0) - 1;
        double d2 = d / 2.0; // Setengah dari d
        double xc = xo + r;
        double yc = yo + r;

        // Arrow
        double c = arrowSize;
        double a = c / 2.0;
        double b = Math.sqrt(Math.pow(c, 2) - Math.pow(a, 2));
       // double[] xPoints = new double[]{d, d - b, d - b};
       // double[] yPoints = new double[]{0, a, -a};
        xPoints[0] = d;
        xPoints[1] = d-b;
        xPoints[2] = d-b;
        yPoints[0] = 0.0;
        yPoints[1] = a;
        yPoints[2] = -a;
        int nPoints = 3;

        // Set Sudut rotasi berdasarkan arah
        double sudutRotasi = -90;
        if (arah == 1) {
            sudutRotasi = -90;
        } else if (arah == 2) {
            sudutRotasi = -45;
        } else if (arah == 3) {
            sudutRotasi = 0;
        } else if (arah == 4) {
            sudutRotasi = 45;
        } else if (arah == 5) {
            sudutRotasi = 90;
        } else if (arah == 6) {
            sudutRotasi = 135;
        } else if (arah == 7) {
            sudutRotasi = 180;
        } else if (arah == 8) { // Batas Arah D8
            sudutRotasi = -135;
        }

        // MENGGAMBAR PANAH
        gc.save();
        Transform transform = Transform.translate(xc, yc);
        transform = transform.createConcatenation(Transform.rotate(sudutRotasi, 0, 0));
        gc.setTransform(new Affine(transform));
        if (arah == 0) {
            gc.fillOval(-d2, -d2, d, d);
            gc.setFill(Color.valueOf("#2f3542")); // Warna Panah            
        } else {
            gc.setStroke(Color.BLACK); // Warna Garis
            gc.setFill(Color.BLACK); // Warna Panah
            gc.strokeLine(-d, 0, d, 0);
            gc.fillPolygon(xPoints, yPoints, nPoints);
        }
        gc.restore();
    } // END OF drawArrow-------------------------------------------------------

    public double getScale() {
        return myScale.get();
    }

    public void setScale(double scale) {
        this.myScale.set(scale);
    }

    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX() - y);
        setTranslateY(getTranslateY() - y);
    }
}
