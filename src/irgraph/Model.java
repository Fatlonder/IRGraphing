/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irgraph;

/**
 *
 * @author FA
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Model {

    private XYSeries series;
    private XYSeriesCollection dataset = new XYSeriesCollection();
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private static int index = 0;

    public Model() {
        series = new XYSeries("IR Data High");
        dataset.addSeries(series);
        chart = ChartFactory.createXYStepChart("IR Data", "Time (ms)", "Bit", dataset);
        chartPanel = new ChartPanel(chart);
    }

    public void parseDiscrete(String inline) {
        int[] data;
        System.out.println(" Start of method ");
        //int size = 0;

        /* it will increase my complexity to n^2
         for (int i = 0; i < inline.length(); i++) {
         if (Character.isDigit(inline.charAt(i))) {
         size++;
         }
         }*/
        // it will have unused data, which will not slow my process.
        String[] parse = inline.split("\t");
        data = new int[parse.length];
        for (int i = 0; i < parse.length; i++) {
            try {
                System.out.println("Outside copy loop ");

                data[i] = Integer.parseInt(parse[i]);

                /*if(Character.isDigit(parse[i].trim().charAt(i)))
                 {
                 data[i] = Character.getNumericValue(inline.charAt(i));
                 System.out.println(data[i]+ " Added on array ");
                 }*/
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

        }
        for (int i = 0; i < data.length - 1; i++) {

            if (data[i] > 4200 && data[i + 1] < 4600) {
                // Indicates the start of data transfer
                System.out.println("Start Bits " + data[i] + " i+1 " + data[i + 1]);
                index++;
                i++;
            } else if ((data[i] < 700 && data[i] > 500) && (data[i + 1] > 500 && data[i + 1] < 700)) {
                series.add(index++, 0);
                i++;
                System.out.println("Logical Low 0 " + data[i] + " i+1 " + data[i + 1]);
            } else if ((data[i] < 700 && data[i] > 500) && (data[i + 1] > 1400 && data[i + 1] < 1800)) {
                series.add(index++, 1);
                i++;
                System.out.println("Logical High 1 " + data[i] + " i+1 " + data[i + 1]);

            } else if (data[i] < 750 && data[i] > 400) {
                // indicates the end of data transfer 
                System.out.println("End Bits " + data[i] + " i+1 " + data[i + 1]);
                series.add(index++, 0);
                series.add(index++, 0);
                series.add(index++, 0);
                index++;
            } else {
                System.out.println("Unknown pulse length! " + data[i] + " i+1 " + data[i + 1]);
            }
        }
    }

    public ChartPanel getChart() {

        return chartPanel;
    }

    public XYSeries getSeries() {
        return series;
    }

    public XYSeriesCollection getDataset() {
        return dataset;
    }

    public static void setIndex(int x) {
        index = x;
    }
}
