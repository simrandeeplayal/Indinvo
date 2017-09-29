package com.example.simrandeep.invoicemaker.invoice_layout;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 *
 * Created by Simrandeep Singh
 */

public class Debit_Note {


    String num_to_words,invoice_id,invoice_date,user_phone,user_com,user_add,user_gst,user_cp,client_com,client_add,client_state,client_zip,client_gst,total,accno,ifsc;
    ArrayList<String[]> items;
    ArrayList<String[]> GST;

    public Debit_Note(String num_to_words,String invoice_id, String invoice_date, String user_com, String user_add, String user_gst, String user_cp, String user_phone, String client_com, String client_add, String client_state, String client_zip, String client_gst, ArrayList<String[]> items, ArrayList<String[]> gsts, String total, String accno, String ifsc)
    {
        this.num_to_words=num_to_words;
        this.invoice_id=invoice_id;
        this.invoice_date=invoice_date;
        this.user_com=user_com;
        this.user_add=user_add;
        this.user_gst=user_gst;
        this.user_cp=user_cp;
        this.client_com=client_com;
        this.client_add=client_add;
        this.client_state=client_state;
        this.client_zip=client_zip;
        this.client_gst=client_gst;
        this.items=items;
        this.GST=gsts;
        this.total=total;
        this.accno=accno;
        this.ifsc=ifsc;
        this.user_phone=user_phone;
    }

    /**
     *
     * method to create Debit note invoice pdf
     *
     * @param file
     * @param path
     * @param stamp
     * @param logopath
     */


    public void pdfcreate(File file, Uri path, Uri stamp,Uri logopath) {

        com.itextpdf.text.Document doc = new com.itextpdf.text.Document(PageSize.A4, 0f, 0f, 0f, 0f);
        String outPath = file.getPath();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(outPath));
            doc.open();
            doc.setMargins(0, 0, 0, 0);           // setting margin
            PdfPTable innertable = new PdfPTable(3);  //first table
            innertable.setWidthPercentage(100);

            Image image=null,image2=null,image3=null;
            if(path!=null) {
                Bitmap bmp = BitmapFactory.decodeFile(path.toString());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 10, stream);
                image= Image.getInstance(stream.toByteArray());
                image.scaleToFit(100,100);
            }
            if(stamp!=null) {
                Bitmap bmp = BitmapFactory.decodeFile(stamp.toString());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 10, stream);
                image2= Image.getInstance(stream.toByteArray());
               image2.scaleToFit(100,100);
            }
            File logo=null;
            if(logopath!=null)
                logo=new File(logopath.toString());
            if(logopath!=null&&logo.exists())            {
                Bitmap bmp = BitmapFactory.decodeFile(logopath.toString());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 10, stream);
                image3= Image.getInstance(stream.toByteArray());
                image3.scaleToFit(50, 50);
            }
            //  innertable.setWidths(new int[]{40});

            PdfPCell cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);

            cell = new PdfPCell(new Paragraph(""+user_com, FontFactory.getFont(FontFactory.TIMES_BOLD, 17, Font.NORMAL, BaseColor.BLACK)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);
             cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);

// column 3

            if(logopath!=null&&logo.exists())
            {
                cell=new PdfPCell(image3);
            }
            else
            {
                cell=new PdfPCell(new Phrase(""));
            }
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);
            cell = new PdfPCell(new Paragraph(""+user_add, FontFactory.getFont(FontFactory.TIMES_BOLD, 17, Font.NORMAL, BaseColor.BLACK)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);
            cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);

// column 4

            cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Phone :"+user_phone, FontFactory.getFont(FontFactory.TIMES_BOLD, 17, Font.NORMAL, BaseColor.BLACK)));
            //cell.setPaddingLeft(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);
            cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);

            cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);
            cell = new PdfPCell(new Paragraph("GSTIN : "+user_gst, FontFactory.getFont(FontFactory.TIMES_BOLD, 17, Font.NORMAL, BaseColor.BLACK)));
            //cell.setPaddingLeft(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);
            cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable.addCell(cell);
// spacing
            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(6);
            cell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(6);
            cell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setFixedHeight(6);
            cell.setBorder(Rectangle.NO_BORDER);
            innertable.addCell(cell);
            doc.add(innertable);

//Second table

            PdfPTable inner = new PdfPTable(1);
            inner.setWidthPercentage(100);
            PdfPCell cel = new PdfPCell(new Phrase("DEBIT NOTE",
                    FontFactory.getFont(FontFactory.COURIER_BOLD, 25, Font.NORMAL, BaseColor.BLACK)));

            cel.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            inner.addCell(cel);
            cel = new PdfPCell();
            cel.setColspan(5);
            cel.setFixedHeight(6);
            cel.setBorder(Rectangle.NO_BORDER);
            inner.addCell(cel);

            doc.add(inner);


            PdfPTable innertable22 = new PdfPTable(2);
            innertable22.setWidthPercentage(100);
            innertable22.setWidths(new int[]{50, 50});
            PdfPCell cel22 = new PdfPCell(new Phrase("Dcoument number : "+invoice_id));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cel22.setHorizontalAlignment(Element.ALIGN_LEFT);
            innertable22.addCell(cel22);

            cel22 = new PdfPCell(new Phrase("Against invoice :"));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cel22.setHorizontalAlignment(Element.ALIGN_LEFT);
            innertable22.addCell(cel22);
            cel22 = new PdfPCell(new Phrase("Date Issue :"));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cel22.setHorizontalAlignment(Element.ALIGN_LEFT);
            innertable22.addCell(cel22);

            cel22 = new PdfPCell(new Phrase("Date of invoice : "+invoice_date));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cel22.setHorizontalAlignment(Element.ALIGN_LEFT);
            innertable22.addCell(cel22);
            cel22 = new PdfPCell(new Phrase("Address :"+user_add));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cel22.setHorizontalAlignment(Element.ALIGN_LEFT);
            innertable22.addCell(cel22);

            cel22 = new PdfPCell(new Phrase(""));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cel22.setHorizontalAlignment(Element.ALIGN_LEFT);
            innertable22.addCell(cel22);
            cel22 = new PdfPCell();
            cel22.setColspan(5);
            cel22.setFixedHeight(6);
            cel22.setBorder(Rectangle.NO_BORDER);
            innertable22.addCell(cel22);
            cel22 = new PdfPCell();
            cel22.setColspan(5);
            cel22.setFixedHeight(6);
            cel22.setBorder(Rectangle.NO_BORDER);
            innertable22.addCell(cel22);
            doc.add(innertable22);


            //  innertable.addCell(cell);
            PdfPTable innertable2 = new PdfPTable(2);
            innertable2.setWidthPercentage(100);
            innertable2.setWidths(new int[]{50, 50});
            PdfPCell cell1 = new PdfPCell(new Phrase("Bill to Party"));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable2.addCell(cell1);
            cell1 = new PdfPCell(new Phrase("Ship to Party"));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable2.addCell(cell1);
            cell1 = new PdfPCell(new Phrase("Name: "+client_com));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            innertable2.addCell(cell1);

            cell1 = new PdfPCell(new Phrase("Name : "+client_com));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell1.setPaddingLeft(20);
            innertable2.addCell(cell1);

            cell1 = new PdfPCell(new Phrase("Address : "+client_add));

            innertable2.addCell(cell1);


            cell1 = new PdfPCell(new Phrase("Address: "+client_add));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setPaddingLeft(2);

            innertable2.addCell(cell1);
            cell1 = new PdfPCell(new Phrase("GSTIN: "+client_gst));
            //cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            innertable2.addCell(cell1);

            cell1 = new PdfPCell(new Phrase("GSTIN: "+client_gst));
            innertable2.addCell(cell1);
            cell1 = new PdfPCell(new Phrase("State:  "+client_state+"\t\t\t  Code:"+client_zip));

            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            innertable2.addCell(cell1);

            cell1 = new PdfPCell(new Phrase("State:  "+client_state+"\t\t\t  Code:"+client_zip));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);

            innertable2.addCell(cell1);
            cell1 = new PdfPCell();
            cell1.setColspan(5);
            cell1.setFixedHeight(6);
            cell1.setBorder(Rectangle.NO_BORDER);
            innertable2.addCell(cell1);
            cell1 = new PdfPCell();
            cell1.setColspan(5);
            cell1.setFixedHeight(6);
            cell1.setBorder(Rectangle.NO_BORDER);
            innertable2.addCell(cell1);

            doc.add(innertable2);


            PdfPTable innertable5 = new PdfPTable(12);
            innertable5.setWidthPercentage(100);
            // innertable5.setWidths(new int[]{11,4,7,5,5,5});

            PdfPCell cell5 = new PdfPCell(new Phrase("S.no"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);

            cell5 = new PdfPCell(new Phrase("Product Description"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("HSN code"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("UOM"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("Qty"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("Rate"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("Amount"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("Discount"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("Taxable Value"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("CGST"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);

            cell5 = new PdfPCell(new Phrase("SGST"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            innertable5.addCell(cell5);

            cell5 = new PdfPCell(new Phrase("Total"));
            cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell5.setMinimumHeight(10f);
            innertable5.addCell(cell5);




         for(int i=0;i<items.size();i++) {
             String item[] = items.get(i);
             String gsco[] = GST.get(i);

             cell5 = new PdfPCell(new Phrase("" + (i + 1)));
             innertable5.addCell(cell5);
             cell5 = new PdfPCell(new Phrase("" + item[0]));
             innertable5.addCell(cell5);
             cell5 = new PdfPCell(new Phrase("" + item[1]));
             innertable5.addCell(cell5);
             cell5 = new PdfPCell(new Phrase(""));
             innertable5.addCell(cell5);
             cell5 = new PdfPCell(new Phrase("" + item[5]));
             innertable5.addCell(cell5);
             cell5 = new PdfPCell(new Phrase("" + item[4]));
             innertable5.addCell(cell5);
             cell5 = new PdfPCell(new Phrase("" + item[6]));
             innertable5.addCell(cell5);
             cell5 = new PdfPCell(new Phrase(""));
             innertable5.addCell(cell5);
             cell5 = new PdfPCell(new Phrase("     "));
             innertable5.addCell(cell5);
             PdfPTable nested4 = new PdfPTable(1);
             nested4.addCell("R: " + item[3]);
             nested4.addCell("A: " + gsco[1]);
             PdfPCell nesthousing4 = new PdfPCell(nested4);
             innertable5.addCell(nesthousing4);
             PdfPTable nested5 = new PdfPTable(1);
             nested5.addCell("R: " + item[2]);
             nested5.addCell("A: " + gsco[0]);
             PdfPCell nesthousing5 = new PdfPCell(nested5);
             innertable5.addCell(nesthousing5);
             cell5 = new PdfPCell(new Phrase("0"));
             cell5.setMinimumHeight(10f);
             innertable5.addCell(cell5);
         }
            doc.add(innertable5);

                PdfPTable t = new PdfPTable(2);
            t.setWidthPercentage(100);
            t.setWidths(new int[]{50,50});
            PdfPCell ce = new PdfPCell(new Phrase("Total"));
            t.addCell(ce);
            ce = new PdfPCell(new Phrase(""+total));
            t.addCell(ce);
          /*  ce = new PdfPCell(new Phrase(""));
            t.addCell(ce);
            ce = new PdfPCell(new Phrase("0"));
            t.addCell(ce);
            ce = new PdfPCell(new Phrase("0"));
            t.addCell(ce);
            ce = new PdfPCell(new Phrase("0"));
            t.addCell(ce);
            ce = new PdfPCell(new Phrase("0"));
            t.addCell(ce);
            ce = new PdfPCell(new Phrase("0"));
            t.addCell(ce);
            ce = new PdfPCell(new Phrase("0"));
            t.addCell(ce);
            ce = new PdfPCell(new Phrase("0"));
            t.addCell(ce);*/
            doc.add(t);


            //next step
            PdfPTable innertable6 = new PdfPTable(3);
            innertable6.setWidths(new int[]{40, 20, 20});
            innertable6.setWidthPercentage(100);
            PdfPCell cell6 = new PdfPCell(new Phrase("Total Amount Paid (In Words:):\n"+num_to_words));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            innertable6.addCell(cell6);
            cell6.setMinimumHeight(50f);
            PdfPTable nested = new PdfPTable(1);
            nested.addCell("Total Amount before tax:");
            nested.addCell("Add: CGST");
            nested.addCell("Add: SGST");
            nested.addCell("Total Tax Amount ");
            nested.addCell("Total Amount After Tax");
            PdfPCell nesthousing = new PdfPCell(nested);
            innertable6.addCell(nesthousing);
            PdfPTable nested2 = new PdfPTable(1);

            Double subtotalsgst=0.0,subtotalcgst=0.0,subtot=0.0;
            for(int i=0;i<items.size();i++)
            {
                String item[]=items.get(i);
                String gs[]=GST.get(i);
                subtot=subtot+(Double.parseDouble(item[4])*Double.parseDouble(item[5]));
                subtotalsgst=subtotalsgst+Double.parseDouble(gs[0]);
                subtotalcgst=subtotalcgst+Double.parseDouble(gs[1]);

            }


            nested2.addCell(""+subtot);
            nested2.addCell(""+subtotalcgst);
            nested2.addCell(""+subtotalsgst);
            nested2.addCell(""+subtotalcgst+subtotalsgst);
            nested2.addCell(""+total);
            PdfPCell nesthousing2 = new PdfPCell(nested2);
            innertable6.addCell(nesthousing2);

            cell6 = new PdfPCell();
            cell6.setColspan(5);
            cell6.setFixedHeight(6);
            cell6.setBorder(Rectangle.NO_BORDER);
            innertable6.addCell(cell6);
            doc.add(innertable6);


            PdfPTable innertable7 = new PdfPTable(3);
            innertable7.setWidthPercentage(100);
            //innertable6.setWidths(new int[]{20,20,20});
            PdfPTable nested3 = new PdfPTable(1);
            nested3.addCell("Bank Details");
            nested3.addCell("Bank A/C"+accno);
            nested3.addCell("Bank IFSC"+ifsc);
            nested3.addCell("Terms And Conditions");
            PdfPCell nesthousing3 = new PdfPCell(nested3);
            nesthousing3.setFixedHeight(150);
            innertable7.addCell(nesthousing3);
            PdfPTable nested5 = new PdfPTable(1);
            if(stamp!=null)
            {   PdfPCell cell55=new PdfPCell(image2);
                cell55.setHorizontalAlignment(Element.ALIGN_CENTER);
             //   cell55.setFixedHeight(150);
                nested5.addCell(cell55);

            }
            else
            {
                nested5.addCell("");
            }
            nested5.addCell("Common Seal");
            PdfPCell nesthousing5 = new PdfPCell(nested5);

            innertable7.addCell(nesthousing5);
            PdfPTable nested4 = new PdfPTable(1);
            if(path!=null)
            {    PdfPCell cell65=new PdfPCell(image);
                cell65.setHorizontalAlignment(Element.ALIGN_CENTER);
             //   cell65.setFixedHeight(150);
                nested4.addCell(cell65);}
            else
            {
                nested4.addCell("");
            }
            nested4.addCell("Authorised Signatory");
            PdfPCell nesthousing4 = new PdfPCell(nested4);

            innertable7.addCell(nesthousing4);



            doc.add(innertable7);
            doc.close();
        } catch (DocumentException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

