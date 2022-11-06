package th.ac.ku.mylaundry.service;


import java.awt.print.*;

public class GetPrintDialog {


    public static void printJob(String fileName){
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        paper.setImageableArea(0, 0, pageFormat.getWidth(), pageFormat.getHeight());
        pageFormat.setPaper(paper);
//        PdfDocument pdf = new PdfDocument();
//        printerJob.setPrintable(pdf, pageFormat);
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }
}
