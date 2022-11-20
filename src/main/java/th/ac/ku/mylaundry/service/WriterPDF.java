package th.ac.ku.mylaundry.service;

import com.github.pheerathach.ThaiQRPromptPay;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import th.ac.ku.mylaundry.model.*;

public class WriterPDF {


    DecimalFormat f = new DecimalFormat("#0.00");

    public static void createINVPDF (Order order) throws IOException, DocumentException {

        String fontpath = "/System/Library/Fonts/Supplemental/Tahoma.ttf";
        BaseFont customfont = BaseFont.createFont(fontpath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font font2 = new Font(customfont,13);

        Laundry laundry = LaundryApiDataSource.getShop();
        Customer customer = CustomerApiDataSource.searchCustomer(order.getCus_phone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ArrayList<ClothList> clothLists  = OrderApiDataSource.getOrderClothList(order.getId());
        DecimalFormat f = new DecimalFormat("#0.00");
        String pdfFilename ="inv/" +order.getName()+"-inv.pdf";
        try {
            File dir = new File("inv") ;
            if(dir.mkdir()){
            }
            OutputStream file = new FileOutputStream(new File(pdfFilename));
            Document document = new Document();
            PdfWriter.getInstance(document, file);

            //Inserting Image in PDF
            Image image = Image.getInstance ("src/main/resources/th/ac/ku/mylaundry/images/main_icon_128px.png");//Header Image
            image.scaleAbsolute(100f, 100f);//image widt
            // h,height
            image.setAlignment(Element.ALIGN_CENTER);
            font2 = new Font(customfont,13,Font.BOLD);
            Paragraph paragraph = new Paragraph(laundry.getName(),font2);
            font2 = new Font(customfont,11,Font.NORMAL);
            Paragraph paragraph1 = new Paragraph("โทร : "+laundry.getPhone(),font2);
            Paragraph paragraph2 = new Paragraph("ที่อยู่ : " + laundry.getAddress(), font2);
            Paragraph paragraph3 = new Paragraph("อีเมลล์ : " + laundry.getEmail(),font2);




            PdfPTable irdTable = new PdfPTable(2);
            irdTable.addCell(getIRDCell("เลขที่"));
            irdTable.addCell(getIRDCell("วันที่ออก"));
            irdTable.addCell(getIRDCell(order.getName())); // pass invoice number
            irdTable.addCell(getIRDCell(LocalDate.now().format(formatter))); // pass invoice date

            PdfPTable irhTable = new PdfPTable(3);
            irhTable.setWidthPercentage(100);
            irhTable.addCell(getIRHCell("",PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("ใบแจ้งหนี้", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            PdfPCell invoiceTable = new PdfPCell (irdTable);
            invoiceTable.setBorder(0);
            irhTable.addCell(invoiceTable);


            FontSelector fs = new FontSelector();
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);

            font2 = new Font(customfont,16,Font.BOLD);
            fs.addFont(font2);


            Phrase bill = fs.process("บิลถึง"); // customer information




            font2 = new Font(customfont,13,Font.NORMAL);
            Paragraph name = new Paragraph(customer.getName(),font2);
            name.setIndentationLeft(20);
            Paragraph contact = new Paragraph(order.getCus_phone(),font2);
            contact.setIndentationLeft(20);
//            Paragraph address = new Paragraph("Rujipas,Mek");
            Paragraph address ;
            font2 = new Font(customfont,10,Font.NORMAL);

            if(order.getAddress().equals("null")){
                address = new Paragraph("",font2);
            }
            else{
                address = new Paragraph(order.getAddress(),font2);
            }
            address.setIndentationLeft(20);
            PdfPTable billTable = new PdfPTable(6); //one page contains 15 records
            billTable.setWidthPercentage(100);
            billTable.setWidths(new float[] { 1, 2,5,2,1,2 });
            billTable.setSpacingBefore(30.0f);
            billTable.addCell(getBillHeaderCell("ลำดับ"));
            billTable.addCell(getBillHeaderCell("บริการ"));
            billTable.addCell(getBillHeaderCell("ประเภทผ้า"));
            billTable.addCell(getBillHeaderCell("ราคา/ชิ้น"));
            billTable.addCell(getBillHeaderCell("จำนวน"));
            billTable.addCell(getBillHeaderCell("ราคา"));
            int j = 1 ;
            PreviewClothList previewClothList ;
            for (ClothList clothList : clothLists) {
                previewClothList = OrderApiDataSource.getPreviewClothList(clothList.getId());
                billTable.addCell(getBillRowCell(String.valueOf(j)));
                billTable.addCell(getBillRowCell(previewClothList.getService()));
                billTable.addCell(getBillRowCell(previewClothList.getClothType()));
                billTable.addCell(getBillRowCell(f.format(previewClothList.getPricePerU())));
                billTable.addCell(getBillRowCell(String.valueOf(previewClothList.getQuantity())));
                billTable.addCell(getBillRowCell(f.format(previewClothList.getAmount())));
                j++;
            }
            for(; j <= 15;j++){
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
            }





            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(" \n"));
            validity.addCell(getValidityCell(""));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setColspan (3);
            summaryL.setPadding (1.0f);
            billTable.addCell(summaryL);

            double tax = order.getTotal() * 0.07 ;

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("ราคารวม"));
            accounts.addCell(getAccountsCellR(f.format(order.getTotal()-tax-order.getDeliSerCharge()-order.getPickSerCharge())));
            accounts.addCell(getAccountsCell("ค่ารับส่งผ้า"));
            accounts.addCell(getAccountsCellR(f.format(order.getDeliSerCharge()+order.getPickSerCharge())));
            accounts.addCell(getAccountsCell("ภาษีมูลค่าเพิ่ม(7%)"));
            accounts.addCell(getAccountsCellR(f.format(tax)));
            accounts.addCell(getAccountsCell("ราคาสุทธิ"));
            accounts.addCell(getAccountsCellR(f.format(order.getTotal())));
            PdfPCell summaryR = new PdfPCell (accounts);
            summaryR.setColspan (3);
            billTable.addCell(summaryR);

            Image qrImg = null ;
            File file1 = new File("qr.png");

            if(order.getPayMethod().equals("พร้อมเพย์")){
                ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().dynamicQR().creditTransfer().mobileNumber(
                        EmployeeApiDataSource.getOwnerQR()).amount(new BigDecimal(order.getTotal())).build();
                qr.draw(200,200,file1);
                qrImg = Image.getInstance (file1.getPath());//Header Image
                qrImg.scaleAbsolute(100f, 100f);//image widt
                // h,height
                qrImg.setAlignment(Element.ALIGN_CENTER);
            }

            PdfPTable describer = new PdfPTable(1);
//            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
            if (qrImg != null){
//                describer.setWidthPercentage(100);
                describer.addCell(getdescCell("คิวอาร์พร้อมเพย์"));
                describer.addCell(qrImg);
            }
            else{
                describer.setWidthPercentage(100);
                describer.addCell(getdescCell(" "));
                describer.addCell(getdescCell(" "));
            }

            document.open();
            document.add(image);
            document.add(paragraph);
            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(irhTable);

            document.add(bill);
            document.add(name);
            document.add(contact);
            document.add(address);
            document.add(billTable);
            document.add(describer);

            document.close();
            file.close();

            Desktop.getDesktop().open(new File(pdfFilename));

            if(file1.exists()){
                file1.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createReceipt (Order order) throws IOException, DocumentException {

        String fontpath = "/System/Library/Fonts/Supplemental/Tahoma.ttf";
        BaseFont customfont = BaseFont.createFont(fontpath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font font2 = new Font(customfont,13);

        Laundry laundry = LaundryApiDataSource.getShop();
        Customer customer = CustomerApiDataSource.searchCustomer(order.getCus_phone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ArrayList<ClothList> clothLists  = OrderApiDataSource.getOrderClothList(order.getId());
        DecimalFormat f = new DecimalFormat("#0.00");
        String pdfFilename ="receipt/" +order.getName()+"-slip.pdf";
        if(order.getPayStatus()==0){
            return;
        }
        try {

            File dir = new File("receipt") ;
            if(dir.mkdir()){
            }
            OutputStream file = new FileOutputStream(new File(pdfFilename));
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            //Inserting Image in PDF
            Image image = Image.getInstance ("src/main/resources/th/ac/ku/mylaundry/images/main_icon_128px.png");//Header Image
            image.scaleAbsolute(100f, 100f);//image widt
            // h,height
            image.setAlignment(Element.ALIGN_CENTER);
            font2 = new Font(customfont,13,Font.BOLD);
            Paragraph paragraph = new Paragraph(laundry.getName(),font2);
            font2 = new Font(customfont,11,Font.NORMAL);
            Paragraph paragraph1 = new Paragraph("โทร : "+laundry.getPhone(),font2);
            Paragraph paragraph2 = new Paragraph("ที่อยู่ : " + laundry.getAddress(), font2);
            Paragraph paragraph3 = new Paragraph("อีเมลล์ : " + laundry.getEmail(),font2);


            PdfPTable irdTable = new PdfPTable(3);
            irdTable.addCell(getIRDCell("เลขที่"));
            irdTable.addCell(getIRDCell("วันที่ออก"));
            irdTable.addCell(getIRDCell("รูปแบบการชำระ"));
            irdTable.addCell(getIRDCell(order.getName())); // pass invoice number
            irdTable.addCell(getIRDCell(LocalDate.now().format(formatter))); // pass invoice date
            irdTable.addCell(getIRDCell(order.getPayMethod()));

            PdfPTable irhTable = new PdfPTable(3);
            irhTable.setWidthPercentage(100);
            irhTable.addCell(getIRHCell("",PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("ใบเสร็จรับเงิน", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            PdfPCell invoiceTable = new PdfPCell (irdTable);
            invoiceTable.setBorder(0);
            irhTable.addCell(invoiceTable);


            FontSelector fs = new FontSelector();
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);

            font2 = new Font(customfont,16,Font.BOLD);
            fs.addFont(font2);


            Phrase bill = fs.process("ลูกค้า"); // customer information

            font2 = new Font(customfont,13,Font.NORMAL);
            Paragraph name = new Paragraph(customer.getName(),font2);
            name.setIndentationLeft(20);
            Paragraph contact = new Paragraph(order.getCus_phone(),font2);
            contact.setIndentationLeft(20);
//            Paragraph address = new Paragraph("Rujipas,Mek");
            Paragraph address ;
            font2 = new Font(customfont,10,Font.NORMAL);

            if(order.getAddress().equals("null")){
                address = new Paragraph("",font2);
            }
            else{
                address = new Paragraph(order.getAddress(),font2);
            }
            address.setIndentationLeft(20);
            PdfPTable billTable = new PdfPTable(6); //one page contains 15 records
            billTable.setWidthPercentage(100);
            billTable.setWidths(new float[] { 1, 2,5,2,1,2 });
            billTable.setSpacingBefore(30.0f);
            billTable.addCell(getBillHeaderCell("ลำดับ"));
            billTable.addCell(getBillHeaderCell("บริการ"));
            billTable.addCell(getBillHeaderCell("ประเภทผ้า"));
            billTable.addCell(getBillHeaderCell("ราคา/ชิ้น"));
            billTable.addCell(getBillHeaderCell("จำนวน"));
            billTable.addCell(getBillHeaderCell("ราคา"));
            int j = 1 ;
            PreviewClothList previewClothList ;
            for (ClothList clothList : clothLists) {
                previewClothList = OrderApiDataSource.getPreviewClothList(clothList.getId());
                billTable.addCell(getBillRowCell(String.valueOf(j)));
                billTable.addCell(getBillRowCell(previewClothList.getService()));
                billTable.addCell(getBillRowCell(previewClothList.getClothType()));
                billTable.addCell(getBillRowCell(f.format(previewClothList.getPricePerU())));
                billTable.addCell(getBillRowCell(String.valueOf(previewClothList.getQuantity())));
                billTable.addCell(getBillRowCell(f.format(previewClothList.getAmount())));
                j++;
            }
            for(; j <= 15;j++){
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
                billTable.addCell(getBillRowCell(""));
            }





            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(" \n"));
            validity.addCell(getValidityCell(""));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setColspan (3);
            summaryL.setPadding (1.0f);
            billTable.addCell(summaryL);

            double tax = order.getTotal() * 0.07 ;

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("ราคารวม"));
            accounts.addCell(getAccountsCellR(f.format(order.getTotal()-tax-order.getDeliSerCharge()-order.getPickSerCharge())));
            accounts.addCell(getAccountsCell("ค่ารับส่งผ้า"));
            accounts.addCell(getAccountsCellR(f.format(order.getDeliSerCharge()+order.getPickSerCharge())));
            accounts.addCell(getAccountsCell("ภาษีมูลค่าเพิ่ม(7%)"));
            accounts.addCell(getAccountsCellR(f.format(tax)));
            accounts.addCell(getAccountsCell("ราคาสุทธิ"));
            accounts.addCell(getAccountsCellR(f.format(order.getTotal())));
            PdfPCell summaryR = new PdfPCell (accounts);
            summaryR.setColspan (3);
            billTable.addCell(summaryR);

            PdfPTable describer = new PdfPTable(1);
            describer.setWidthPercentage(100);
            describer.addCell(getdescCell(" "));
            describer.addCell(getdescCell(" "));

            document.open();//PDF document opened........
            document.add(image);
            document.add(paragraph);
            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(irhTable);

            document.add(bill);
            document.add(name);
            document.add(contact);
            document.add(address);
            document.add(billTable);
            document.add(describer);

            document.close();
            file.close();

            Desktop.getDesktop().open(new File(pdfFilename));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void packageReceipt (Customer customer, MemberPackage memberPackage) throws IOException, DocumentException {

        String fontpath = "/System/Library/Fonts/Supplemental/Tahoma.ttf";
        BaseFont customfont = BaseFont.createFont(fontpath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font font2 = new Font(customfont,13);

        Laundry laundry = LaundryApiDataSource.getShop();
//        Customer customer = CustomerApiDataSource.searchCustomer(order.getCus_phone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        ArrayList<ClothList> clothLists  = OrderApiDataSource.getOrderClothList(order.getId());
        DecimalFormat f = new DecimalFormat("#0.00");
        String pdfFilename ="receipt/" +customer.getName()+"-memslip.pdf";
        try {

            File dir = new File("receipt") ;
            if(dir.mkdir()){
            }
            OutputStream file = new FileOutputStream(new File(pdfFilename));
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            //Inserting Image in PDF
            Image image = Image.getInstance ("src/main/resources/th/ac/ku/mylaundry/images/main_icon_128px.png");//Header Image
            image.scaleAbsolute(100f, 100f);//image widt
            // h,height
            image.setAlignment(Element.ALIGN_CENTER);
            font2 = new Font(customfont,13,Font.BOLD);
            Paragraph paragraph = new Paragraph(laundry.getName(),font2);
            font2 = new Font(customfont,11,Font.NORMAL);
            Paragraph paragraph1 = new Paragraph("โทร : "+laundry.getPhone(),font2);
            Paragraph paragraph2 = new Paragraph("ที่อยู่ : " + laundry.getAddress(), font2);
            Paragraph paragraph3 = new Paragraph("อีเมลล์ : " + laundry.getEmail(),font2);




            PdfPTable irdTable = new PdfPTable(2);
            irdTable.addCell(getIRDCell("เลขที่"));
            irdTable.addCell(getIRDCell("วันที่ออก"));
            irdTable.addCell(getIRDCell("")); // pass invoice number
            irdTable.addCell(getIRDCell(LocalDate.now().format(formatter))); // pass invoice date


            PdfPTable irhTable = new PdfPTable(3);
            irhTable.setWidthPercentage(100);
            irhTable.addCell(getIRHCell("",PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("ใบเสร็จรับเงิน", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
            PdfPCell invoiceTable = new PdfPCell (irdTable);
            invoiceTable.setBorder(0);
            irhTable.addCell(invoiceTable);


            FontSelector fs = new FontSelector();
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);

            font2 = new Font(customfont,16,Font.BOLD);
            fs.addFont(font2);


            Phrase bill = fs.process("ลูกค้า"); // customer information




            font2 = new Font(customfont,13,Font.NORMAL);
            Paragraph name = new Paragraph(customer.getName(),font2);
            name.setIndentationLeft(20);
            Paragraph contact = new Paragraph(customer.getPhone(),font2);
            contact.setIndentationLeft(20);
//            Paragraph address = new Paragraph("Rujipas,Mek");
            Paragraph address ;
            font2 = new Font(customfont,10,Font.NORMAL);
            String addresscus = (CustomerApiDataSource.getCustomerAddress(customer.getId()).getuCode()) ;
            if(addresscus.equals("null")){
                address = new Paragraph("",font2);
            }
            else{
                address = new Paragraph(addresscus,font2);
            }
            address.setIndentationLeft(20);
            PdfPTable billTable = new PdfPTable(5); //one page contains 15 records
            billTable.setWidthPercentage(100);
            billTable.setWidths(new float[] { 1, 4,2,1,2 });
            billTable.setSpacingBefore(30.0f);
            billTable.addCell(getBillHeaderCell("ลำดับ"));
            billTable.addCell(getBillHeaderCell("บริการ"));
            billTable.addCell(getBillHeaderCell("ราคา/ชิ้น"));
            billTable.addCell(getBillHeaderCell("จำนวน"));
            billTable.addCell(getBillHeaderCell("ราคา"));
//            int j = 1 ;
//            PreviewClothList previewClothList ;
//            for (ClothList clothList : clothLists) {
//                previewClothList = OrderApiDataSource.getPreviewClothList(clothList.getId());
//                billTable.addCell(getBillRowCell(String.valueOf(j)));
//                billTable.addCell(getBillRowCell(previewClothList.getService()));
//                billTable.addCell(getBillRowCell(previewClothList.getClothType()));
//                billTable.addCell(getBillRowCell(f.format(previewClothList.getPricePerU())));
//                billTable.addCell(getBillRowCell(String.valueOf(previewClothList.getQuantity())));
//                billTable.addCell(getBillRowCell(f.format(previewClothList.getAmount())));
//                j++;
//            }
//            for(; j <= 15;j++){
//                billTable.addCell(getBillRowCell(""));
//                billTable.addCell(getBillRowCell(""));
//                billTable.addCell(getBillRowCell(""));
//                billTable.addCell(getBillRowCell(""));
//                billTable.addCell(getBillRowCell(""));
//                billTable.addCell(getBillRowCell(""));
//            }
                billTable.addCell(getBillRowCell("1"));
                billTable.addCell(getBillRowCell(memberPackage.getService()));
                billTable.addCell(getBillRowCell(f.format(memberPackage.getPrice()/memberPackage.getQuantity())));
                billTable.addCell(getBillRowCell(String.valueOf(memberPackage.getQuantity())));
                billTable.addCell(getBillRowCell(f.format(memberPackage.getPrice())));





            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(" \n"));
            validity.addCell(getValidityCell(""));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setColspan (3);
            summaryL.setPadding (1.0f);
            billTable.addCell(summaryL);

            double tax = memberPackage.getPrice() * 0.07 ;

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("ราคารวม"));
            accounts.addCell(getAccountsCellR(f.format(memberPackage.getPrice()-tax)));
            accounts.addCell(getAccountsCell("ค่ารับส่งผ้า"));
            accounts.addCell(getAccountsCellR(f.format(0)));
            accounts.addCell(getAccountsCell("ภาษีมูลค่าเพิ่ม(7%)"));
            accounts.addCell(getAccountsCellR(f.format(tax)));
            accounts.addCell(getAccountsCell("ราคาสุทธิ"));
            accounts.addCell(getAccountsCellR(f.format(memberPackage.getPrice())));
            PdfPCell summaryR = new PdfPCell (accounts);
            summaryR.setColspan (3);
            billTable.addCell(summaryR);

            PdfPTable describer = new PdfPTable(1);
            describer.setWidthPercentage(100);
            describer.addCell(getdescCell(" "));
            describer.addCell(getdescCell(" "));

            document.open();//PDF document opened........
            document.add(image);
            document.add(paragraph);
            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(irhTable);

            document.add(bill);
            document.add(name);
            document.add(contact);
            document.add(address);
            document.add(billTable);
            document.add(describer);

            document.close();
            file.close();

            Desktop.getDesktop().open(new File(pdfFilename));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeDeliveryList(ArrayList<DeliveryTime> deliveryTimes) throws DocumentException, IOException {

        File dir = new File("delivery") ;
        if(dir.mkdir()){
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fontpath = "/System/Library/Fonts/Supplemental/Tahoma.ttf";
        BaseFont customfont = BaseFont.createFont(fontpath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font font2 = new Font(customfont,15,Font.BOLD);
        String pdfFilename ="delivery/" +LocalDate.now().format(formatter)+".pdf";
        OutputStream file = new FileOutputStream(new File(pdfFilename));
        Document document = new Document();
        PdfWriter.getInstance(document, file);
        document.open();
        font2 = new Font(customfont,32,Font.BOLD);
        Paragraph paragraph1 = new Paragraph("รายการส่งวันที่ "+LocalDate.now().format(formatter),font2);
        document.add(paragraph1) ;
        ArrayList<DeliveryTime> mornDeli = new ArrayList<>();
        ArrayList<DeliveryTime> afterDeli = new ArrayList<>();
        ArrayList<DeliveryTime> evenDeli = new ArrayList<>();

        for (DeliveryTime d:deliveryTimes) {
            if(d.getTime().equals("ช่วงเช้า")){
                mornDeli.add(d);
            }
            else if(d.getTime().equals("ช่วงบ่าย")){
                afterDeli.add(d);
            }
            else if(d.getTime().equals("ช่วงเย็น")){
                evenDeli.add(d);
            }
        }
        Paragraph paragraph ;
        font2 = new Font(customfont,20,Font.BOLD);
        paragraph = new Paragraph("\nช่วงเช้า",font2);
        document.add(paragraph);
        for(int i = 0 ; i < mornDeli.size() ; i++){
            font2 = new Font(customfont,15,Font.BOLD);
            paragraph = new Paragraph("รายการ: "+mornDeli.get(i).getOrderName(),font2);
            document.add(paragraph) ;
            font2 = new Font(customfont,15,Font.NORMAL);
            paragraph = new Paragraph("งาน: "+mornDeli.get(i).getJob(),font2);
            document.add(paragraph) ;
            font2 = new Font(customfont,12,Font.NORMAL);
            paragraph = new Paragraph("ที่อยู่: "+mornDeli.get(i).getU_code(),font2);
            document.add(paragraph) ;
            font2 = new Font(customfont,12,Font.NORMAL);
            paragraph = new Paragraph("",font2);
            document.add(paragraph) ;
        }


        font2 = new Font(customfont,20,Font.BOLD);
        paragraph = new Paragraph("\nช่วงบ่าย",font2);
        document.add(paragraph);
        for(int i = 0 ; i < afterDeli.size() ; i++){
            font2 = new Font(customfont,15,Font.BOLD);
            paragraph = new Paragraph("รายการ: "+afterDeli.get(i).getOrderName(),font2);
            document.add(paragraph) ;
            font2 = new Font(customfont,15,Font.NORMAL);
            paragraph = new Paragraph("งาน: "+afterDeli.get(i).getJob(),font2);
            document.add(paragraph) ;
            font2 = new Font(customfont,12,Font.NORMAL);
            paragraph = new Paragraph("ที่อยู่: "+afterDeli.get(i).getU_code(),font2);
            document.add(paragraph) ;
            font2 = new Font(customfont,12,Font.NORMAL);
            paragraph = new Paragraph("",font2);
            document.add(paragraph) ;
        }

        font2 = new Font(customfont,20,Font.BOLD);
        paragraph = new Paragraph("\nช่วงเย็น",font2);
        document.add(paragraph);
        for(int i = 0 ; i < evenDeli.size() ; i++){
            font2 = new Font(customfont,15,Font.BOLD);
            paragraph = new Paragraph("รายการ: "+evenDeli.get(i).getOrderName(),font2);
            document.add(paragraph) ;
            font2 = new Font(customfont,15,Font.NORMAL);
            paragraph = new Paragraph("งาน: "+evenDeli.get(i).getJob(),font2);
            document.add(paragraph) ;
            font2 = new Font(customfont,12,Font.NORMAL);
            paragraph = new Paragraph("ที่อยู่: "+evenDeli.get(i).getU_code(),font2);
            document.add(paragraph) ;
            font2 = new Font(customfont,12,Font.NORMAL);
            paragraph = new Paragraph("",font2);
            document.add(paragraph) ;
        }

        document.close();
        file.close();

        Desktop.getDesktop().open(new File(pdfFilename));
    }


    public static PdfPCell getIRHCell(String text, int alignment) throws DocumentException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FontSelector fs = new FontSelector();
//        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        String fontpath = "/System/Library/Fonts/Supplemental/Tahoma.ttf";
        BaseFont customfont = BaseFont.createFont(fontpath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font font2 = new Font(customfont,16,Font.BOLD);
        fs.addFont(font2);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPCell getIRDCell(String text) throws DocumentException, IOException {
        String fontpath = "/System/Library/Fonts/Supplemental/Tahoma.ttf";
        BaseFont customfont = BaseFont.createFont(fontpath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font font2 = new Font(customfont,12,Font.NORMAL);

        PdfPCell cell = new PdfPCell (new Paragraph (text,font2));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    public static PdfPCell getBillHeaderCell(String text) throws DocumentException, IOException {
        FontSelector fs = new FontSelector();
        String fontpath = "/System/Library/Fonts/Supplemental/Tahoma.ttf";
        BaseFont customfont = BaseFont.createFont(fontpath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font font2 = new Font(customfont,11);
        font2.setColor(BaseColor.GRAY);
        fs.addFont(font2);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        return cell;
    }

    public static PdfPCell getBillRowCell(String text) throws DocumentException, IOException {
        String fontpath = "/System/Library/Fonts/Supplemental/Tahoma.ttf";
        BaseFont customfont = BaseFont.createFont(fontpath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font font2 = new Font(customfont,11);
        PdfPCell cell = new PdfPCell (new Paragraph (text,font2));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getBillFooterCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getValidityCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getAccountsCell(String text) throws DocumentException, IOException {
        String fontpath = "/System/Library/Fonts/Supplemental/Tahoma.ttf";
        BaseFont customfont = BaseFont.createFont(fontpath,BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        Font font2 = new Font(customfont,11);
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font2);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setPadding (5.0f);
        return cell;
    }
    public static PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        cell.setPadding (5.0f);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getdescCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setBorder(0);
        return cell;
    }
}
