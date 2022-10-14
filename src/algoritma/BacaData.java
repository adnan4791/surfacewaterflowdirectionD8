package algoritma;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class BacaData implements Runnable {

    private File file;
    private int ncols;
    private int nrows;
    private double[][] data = null;

    // Thread
    private Thread thread;

    public BacaData(File file) {
        this.file = file;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        bacaData(this.file);
    }

    private void bacaData(File file) {
        Main.textArea01.setText("");
        Main.textArea02.setText("");
        try {
            FileInputStream fis = new FileInputStream(file);
            Scanner sc = new Scanner(fis, "UTF-8");

            if (sc.hasNextLine()) {
                String baris = sc.nextLine();
                String[] splitBaris = baris.split("\\s++");
                ncols = Integer.parseInt(splitBaris[1]);
            }

            if (sc.hasNextLine()) {
                String baris = sc.nextLine();
                String[] splitBaris = baris.split("\\s++");
                nrows = Integer.parseInt(splitBaris[1]);
            }

            // initialize array data
            data = new double[nrows][];

            sc.nextLine();
            sc.nextLine();
            sc.nextLine();
            sc.nextLine();

            StringBuffer sb = new StringBuffer();
            // MEMBACA ELEMEN DATA BARIS DEMI BARIS
            for (int i = 0; i < nrows; i++) {
                //System.out.println("TRACE Baris ke i " + i);
                String baris = sc.nextLine(); // Membaca Data
                sb.append(baris + "\n"); //append garis ke sb untuk dimasukkan ke text area

                String[] splitBaris = baris.split("\\s+");

                data[i] = new double[splitBaris.length];
                for (int j = 0; j < data[i].length; j++) {
                    String sValue = splitBaris[j].trim();
                    double dValue = Double.parseDouble(sValue);
                    data[i][j] = dValue;
                }
            }

            Main.textArea01.setText(sb.toString());

            sc.close();
            fis.close();

        } catch (IOException e) {
            System.out.println("GAGAL MEMBACA DATA");
            e.printStackTrace();
        }
    }

    public void waitData() {
        try {
            thread.join();
            Main.data = this.data;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
