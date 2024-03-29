package com.novartis.mymigraine.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfWriter;
import com.novartis.mymigraine.R;
import com.novartis.mymigraine.model.DiaryLogDetailsModel;

public class GeneratePDF
{
	
	private static String FILEDIR = Environment.getExternalStorageDirectory() + File.separator + "Pdffiles";
	private Context mcontext;
	private ProgressDialog dialog;
	private boolean isChart;
	private boolean isDiary;
	private LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>>> hasmap;
	private boolean notes;
	private String startTime;
	private int periodTime;

	public void generatePDF(final Bitmap PTPiChartBitmap, final Bitmap listofPTPiChartField,
			final Bitmap listofTEBarChart, final Bitmap listSFBarChart, final Bitmap listPLBarChart,
			final Bitmap PLevelBarChart, final Bitmap listPLevelBarChartField, Context context,
			ProgressDialog progressDialog, boolean ischart, boolean isdiary,
			LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>>> hasmap1, boolean b,
			String time, int period)
	{

		mcontext = context;
		dialog = progressDialog;
		isChart = ischart;
		isDiary = isdiary;
		hasmap = hasmap1;
		notes = b;
		startTime = time;
		periodTime = period;

		
		final File copy_File = createFolderWithFile(mcontext.getString(R.string.pdf_file_name));
		try
		{
			copy_File.createNewFile();
			/** -----New Code -------- **/
			Rectangle pageSize = new Rectangle(PageSize.A4);
			pageSize.setBorder(Rectangle.BOX);
			pageSize.setBorderWidth(3f);

			final Document document = new Document(pageSize, 20, 20, 60, 0);

			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(copy_File));

			final float[] columnDefinitionSize = { 100F };

			writer.setPageEvent(new PdfPageEvent()
			{

				@Override
				public void onStartPage(PdfWriter arg0, Document arg1)
				{

					mainHeader(document, columnDefinitionSize);

				}

				@Override
				public void onSectionEnd(PdfWriter arg0, Document arg1, float arg2)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onSection(PdfWriter arg0, Document arg1, float arg2, int arg3, Paragraph arg4)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onParagraphEnd(PdfWriter arg0, Document arg1, float arg2)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onParagraph(PdfWriter arg0, Document arg1, float arg2)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onOpenDocument(PdfWriter arg0, Document arg1)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onGenericTag(PdfWriter arg0, Document arg1, Rectangle arg2, String arg3)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onEndPage(PdfWriter arg0, Document arg1)
				{

				}

				@Override
				public void onCloseDocument(PdfWriter arg0, Document arg1)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onChapterEnd(PdfWriter arg0, Document arg1, float arg2)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onChapter(PdfWriter arg0, Document arg1, float arg2, Paragraph arg3)
				{
					// TODO Auto-generated method stub

				}
			});

			document.open();

			if (isChart)
			{

				Font font10 = FontFactory.getFont("Helvetica", 10f, Font.BOLD);
				font10.setSize(10f);
				font10.setColor(harmony.java.awt.Color.lightGray);

				/**
				 * for PI chart generation
				 */
				if (PTPiChartBitmap != null && listofPTPiChartField!=null)
				{

					// set Title of the page
					setTitle(document, "potentialTrigger");

					PdfPTable tableMonth = new PdfPTable(1);
					tableMonth.getDefaultCell().setBorder(0);
					// tableMonth.setSpacingBefore(10f);
					tableMonth.setSpacingAfter(5f);
					Phrase phCategory;
					if (periodTime == 0)
					{
						phCategory = new Phrase(Font.BOLD, "Share of trigger entered.", font10);
					}
					else
					{
						phCategory = new Phrase(Font.BOLD, "Share of trigger entered since " + startTime + ".", font10);
					}

					PdfPCell cell = new PdfPCell(phCategory);
					cell.setBorder(0);
					cell.setPaddingLeft(84f);
					tableMonth.addCell(cell);
					document.add(tableMonth);

					// Paragraph p = new Paragraph(phCategory);
					//
					// p.setSpacingBefore(10f);
					// p.setSpacingAfter(10f);
					// document.add(p);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					PTPiChartBitmap.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */, stream);
					Image png_attendant = Image.getInstance(stream.toByteArray());
					png_attendant.setAlignment(Element.ALIGN_MIDDLE);
					// Mihir 25072013
					png_attendant.scaleAbsolute(220, 220);
					/*
					 * png_attendant.scaleToFit( PTPiChartBitmap.getWidth(),
					 * PTPiChartBitmap.getHeight());
					 */
					PdfPCell imageCell = new PdfPCell(png_attendant);
					imageCell.setBorder(Rectangle.NO_BORDER);
					imageCell.setPadding(10f);
					stream.flush();
					stream.reset();
					document.add(png_attendant);

					Paragraph par = new Paragraph();
					par.setSpacingAfter(2f);
					par.setSpacingBefore(2f);
					document.add(par);

					ByteArrayOutputStream stream_for_pichart = new ByteArrayOutputStream();
					listofPTPiChartField.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */,
							stream_for_pichart);
					Image png_attendant_for_pichart = Image.getInstance(stream_for_pichart.toByteArray());
					png_attendant_for_pichart.setAlignment(Element.ALIGN_MIDDLE);
					// Mihir 25072013
					Log.e("Mihir", "listofPTPiChartField Width " + listofPTPiChartField.getWidth()
							+ " listofPTPiChartField Heaight " + listofPTPiChartField.getHeight());
					// Width 363 Heaight 418 in 480x800
					// Width 262 Heaight 418 in 540x960
					// Width 415 Heaight 550 in 720x1280
					// Width 645 Heaight 825 in 1080x1900

					/*
					 * int scaleX = listofPTPiChartField.getWidth() > 365 ? 365
					 * : listofPTPiChartField.getWidth(); int scaleY =
					 * listofPTPiChartField.getHeight() > 420 ? 420 :
					 * listofPTPiChartField.getHeight();
					 */

					int scaleX = (int) (listofPTPiChartField.getWidth() * Float.parseFloat(mcontext
							.getString(R.string.list_plevel_barchart_scale_x)));
					int scaleY = (int) (listofPTPiChartField.getHeight() * Float.parseFloat(mcontext
							.getString(R.string.list_plevel_barchart_scale_y)));

					png_attendant_for_pichart.scaleAbsolute(scaleX, scaleY);
					/*
					 * png_attendant_for_pichart.scaleToFit(
					 * listofPTPiChartField.getWidth(),
					 * listofPTPiChartField.getHeight());
					 */
					PdfPCell imageCell_for_pichart = new PdfPCell(png_attendant_for_pichart);
					imageCell_for_pichart.setBorder(Rectangle.NO_BORDER);
					imageCell_for_pichart.setPaddingTop(30f);
					imageCell_for_pichart.setPaddingLeft(10f);
					imageCell_for_pichart.setPaddingRight(10f);
					imageCell_for_pichart.setPaddingBottom(10f);
					stream_for_pichart.flush();
					stream_for_pichart.reset();
					document.add(png_attendant_for_pichart);

					document.newPage();
				}

				/**
				 * for Trigger exposure chart
				 */
				if (listofTEBarChart != null)
				{

					// set Title of the page
					setTitle(document, "triggerExposure");

					PdfPTable tableMonth = new PdfPTable(1);
					tableMonth.getDefaultCell().setBorder(0);
					Phrase phCategory = new Phrase(Font.BOLD, "Percentage of exposure to triggers 24-48", font10);
					PdfPCell cell = new PdfPCell(phCategory);
					cell.setBorder(0);
					cell.setPaddingLeft(84f);
					tableMonth.addCell(cell);
					document.add(tableMonth);

					PdfPTable tableMonth1 = new PdfPTable(1);
					tableMonth1.getDefaultCell().setBorder(0);
					Phrase phTEB;
					if (periodTime == 0)
					{
						phTEB = new Phrase(Font.BOLD, "hours prior to headache.", font10);
					}
					else
					{
						phTEB = new Phrase(Font.BOLD, "hours prior to headache since " + startTime + ".", font10);
					}
					PdfPCell cell1 = new PdfPCell(phTEB);
					cell1.setBorder(0);
					cell1.setPaddingLeft(84f);
					tableMonth1.addCell(cell1);
					document.add(tableMonth1);

					// Phrase phTE = new Phrase(Font.BOLD,
					// "Percentage of exposure to triggers 24-48",
					// font10);
					// Paragraph p = new Paragraph(phTE);
					// p.setSpacingBefore(10f);
					// document.add(p);
					//
					// Phrase phTEB = new Phrase(Font.BOLD,
					// "hours prior to headache Since last 06/07/2013",
					// font10);
					// Paragraph p1 = new Paragraph(phTEB);
					// p1.setSpacingBefore(10f);
					// p1.setSpacingAfter(20f);
					// document.add(p1);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					listofTEBarChart.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */, stream);
					Image png_attendant = Image.getInstance(stream.toByteArray());
					png_attendant.setAlignment(Element.ALIGN_MIDDLE);
					// Mihir 25072013
					Log.e("Mihir", "listofTEBarChart Width " + listofTEBarChart.getWidth()
							+ "listofTEBarChart Heaight " + listofTEBarChart.getHeight());
					//
					// Width 540 Heaight 583 in 540x960
					// Width 720 Heaight 770 in 720x1280
					// Width 1080 Heaight 1155 in 1080x1900

					/*
					 * int scaleX = listofTEBarChart.getWidth() > 540 ? 540 :
					 * listofTEBarChart.getWidth(); int scaleY =
					 * listofTEBarChart.getHeight() > 585 ? 585 :
					 * listofTEBarChart.getHeight();
					 */

					int scaleX = (int) (listofTEBarChart.getWidth() * Float.parseFloat(mcontext
							.getString(R.string.list_plevel_barchart_scale_x)));
					int scaleY = (int) (listofTEBarChart.getHeight() * Float.parseFloat(mcontext
							.getString(R.string.list_plevel_barchart_scale_y)));

					png_attendant.scaleAbsolute(scaleX, scaleY);
					/*
					 * png_attendant.scaleToFit( listofTEBarChart.getWidth(),
					 * listofTEBarChart.getHeight());
					 */
					PdfPCell imageCell = new PdfPCell(png_attendant);
					imageCell.setBorder(Rectangle.NO_BORDER);
					imageCell.setPaddingTop(30f);
					imageCell.setPaddingLeft(10f);
					imageCell.setPaddingRight(10f);
					imageCell.setPaddingBottom(10f);
					stream.flush();
					stream.reset();
					document.add(png_attendant);

					document.newPage();
				}

				/**
				 * for Symptoms Frequency chart
				 */
				if (listSFBarChart != null)
				{

					// set Title of the page
					setTitle(document, "symptomsFrequency");

					PdfPTable tableMonth = new PdfPTable(1);
					tableMonth.getDefaultCell().setBorder(0);
					Phrase phCategory = new Phrase(Font.BOLD, "Percentage of times you experienced a specific", font10);
					PdfPCell cell = new PdfPCell(phCategory);
					cell.setBorder(0);
					cell.setPaddingLeft(84f);
					tableMonth.addCell(cell);
					document.add(tableMonth);

					PdfPTable tableMonth1 = new PdfPTable(1);
					tableMonth1.getDefaultCell().setBorder(0);
					Phrase phTEB;
					if (periodTime == 0)
					{
						phTEB = new Phrase(Font.BOLD, "headache symptoms.", font10);
					}
					else
					{
						phTEB = new Phrase(Font.BOLD, "headache symptoms since " + startTime + ".", font10);
					}
					PdfPCell cell1 = new PdfPCell(phTEB);
					cell1.setBorder(0);
					cell1.setPaddingLeft(84f);
					tableMonth1.addCell(cell1);
					document.add(tableMonth1);

					// Phrase phSB = new Phrase(Font.BOLD,
					// "Percentage of times you experienced a specific",
					// font10);
					// Paragraph p = new Paragraph(phSB);
					// p.setSpacingBefore(10f);
					// document.add(p);
					//
					// Phrase phSBB = new Phrase(Font.BOLD,
					// "headache symptoms Since last 06/07/2013",
					// font10);
					// Paragraph p1 = new Paragraph(phSBB);
					// p1.setSpacingBefore(10f);
					// p1.setSpacingAfter(20f);
					// document.add(p1);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					listSFBarChart.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */, stream);
					Image png_attendant = Image.getInstance(stream.toByteArray());
					png_attendant.setAlignment(Element.ALIGN_MIDDLE);

					// Mihir 25072013
					Log.e("Mihir", "listSFBarChart Width " + listSFBarChart.getWidth() + "listSFBarChart Heaight "
							+ listSFBarChart.getHeight());
					// Width 540 Heaight 530 in 540x960
					// Width 720 Heaight 700 in 720x1280
					// Width 1080 Heaight 1050 in 1080x1900
					/*
					 * int scaleX = listSFBarChart.getWidth() > 540 ? 540 :
					 * listSFBarChart.getWidth(); int scaleY =
					 * listSFBarChart.getHeight() > 530 ? 530 :
					 * listSFBarChart.getHeight();
					 */

					int scaleX = (int) (listSFBarChart.getWidth() * Float.parseFloat(mcontext
							.getString(R.string.list_plevel_barchart_scale_x)));
					int scaleY = (int) (listSFBarChart.getHeight() * Float.parseFloat(mcontext
							.getString(R.string.list_plevel_barchart_scale_y)));

					png_attendant.scaleAbsolute(scaleX, scaleY);

					/*
					 * png_attendant.scaleToFit(listSFBarChart.getWidth(),
					 * listSFBarChart.getHeight());
					 */
					PdfPCell imageCell = new PdfPCell(png_attendant);
					imageCell.setBorder(Rectangle.NO_BORDER);
					imageCell.setPaddingTop(30f);
					imageCell.setPaddingLeft(10f);
					imageCell.setPaddingRight(10f);
					imageCell.setPaddingBottom(10f);
					stream.flush();
					stream.reset();
					document.add(png_attendant);

					document.newPage();
				}

				/**
				 * for Pain Location chart
				 */
				if (listPLBarChart != null)
				{

					// set Title of the page
					setTitle(document, "painLocation");

					PdfPTable tableMonth = new PdfPTable(1);
					tableMonth.getDefaultCell().setBorder(0);
					Phrase phCategory = new Phrase(Font.BOLD, "Percentage of times you indicated a specific", font10);
					PdfPCell cell = new PdfPCell(phCategory);
					cell.setBorder(0);
					cell.setPaddingLeft(84f);
					tableMonth.addCell(cell);
					document.add(tableMonth);

					PdfPTable tableMonth1 = new PdfPTable(1);
					tableMonth1.getDefaultCell().setBorder(0);
					Phrase phTEB;
					if (periodTime == 0)
					{
						phTEB = new Phrase(Font.BOLD, "headache pain location.", font10);
					}
					else
					{
						phTEB = new Phrase(Font.BOLD, "headache pain location since " + startTime + ".", font10);
					}
					PdfPCell cell1 = new PdfPCell(phTEB);
					cell1.setBorder(0);
					cell1.setPaddingLeft(84f);
					tableMonth1.addCell(cell1);
					document.add(tableMonth1);

					// Phrase phPL = new Phrase(Font.BOLD,
					// "Percentage of times you indicated a specific",
					// font10);
					// Paragraph p = new Paragraph(phPL);
					// p.setSpacingBefore(10f);
					// document.add(p);
					//
					// Phrase phPLB = new Phrase(Font.BOLD,
					// "headache pain location Since last 06/07/2013",
					// font10);
					// Paragraph p1 = new Paragraph(phPLB);
					// p1.setSpacingBefore(10f);
					// p1.setSpacingAfter(20f);
					// document.add(p1);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					listPLBarChart.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */, stream);
					Image png_attendant = Image.getInstance(stream.toByteArray());
					png_attendant.setAlignment(Element.ALIGN_MIDDLE);

					// Mihir 25072013
					Log.e("Mihir", "listPLBarChart Width " + listPLBarChart.getWidth() + "listPLBarChart Heaight "
							+ listPLBarChart.getHeight());
					// Width 540 Heaight 530 in 540x960
					// Width 720 Heaight 700 in 720x1280
					// Width 1080 Heaight 1050 in 1080x1900
					/*
					 * int scaleX = listPLBarChart.getWidth() > 540 ? 540 :
					 * listPLBarChart.getWidth(); int scaleY =
					 * listPLBarChart.getHeight() > 530 ? 530 :
					 * listPLBarChart.getHeight();
					 */

					int scaleX = (int) (listPLBarChart.getWidth() * Float.parseFloat(mcontext
							.getString(R.string.list_plevel_barchart_scale_x)));
					int scaleY = (int) (listPLBarChart.getHeight() * Float.parseFloat(mcontext
							.getString(R.string.list_plevel_barchart_scale_y)));

					png_attendant.scaleAbsolute(scaleX, scaleY);

					/*
					 * png_attendant.scaleToFit(listPLBarChart.getWidth(),
					 * listPLBarChart.getHeight());
					 */
					PdfPCell imageCell = new PdfPCell(png_attendant);
					imageCell.setBorder(Rectangle.NO_BORDER);
					imageCell.setPaddingTop(30f);
					imageCell.setPaddingLeft(10f);
					imageCell.setPaddingRight(10f);
					imageCell.setPaddingBottom(10f);
					stream.flush();
					stream.reset();
					document.add(png_attendant);

					document.newPage();
				}

				/**
				 * for Pain Level chart generation
				 */
				if (PLevelBarChart != null)
				{
					if (listPLevelBarChartField != null)
					{
						// set Title of the page
						setTitle(document, "painLevel");

						PdfPTable tableMonth = new PdfPTable(1);
						tableMonth.getDefaultCell().setBorder(0);
						Phrase phCategory = new Phrase(Font.BOLD, "Percentage of times you indicated a specific",
								font10);
						PdfPCell cell = new PdfPCell(phCategory);
						cell.setBorder(0);
						cell.setPaddingLeft(84f);
						tableMonth.addCell(cell);
						document.add(tableMonth);

						PdfPTable tableMonth1 = new PdfPTable(1);
						tableMonth1.getDefaultCell().setBorder(0);
						Phrase phTEB;
						if (periodTime == 0)
						{
							phTEB = new Phrase(Font.BOLD, "headache pain intensity.", font10);
						}
						else
						{
							phTEB = new Phrase(Font.BOLD, "headache pain intensity since " + startTime + ".", font10);
						}
						PdfPCell cell1 = new PdfPCell(phTEB);
						cell1.setBorder(0);
						cell1.setPaddingLeft(84f);
						tableMonth1.addCell(cell1);
						document.add(tableMonth1);

						// Phrase phPL = new Phrase(Font.BOLD,
						// "Percentage of times you indicated a specific",
						// font10);
						// Paragraph p = new Paragraph(phPL);
						// p.setSpacingBefore(10f);
						// document.add(p);
						//
						// Phrase phPLB = new Phrase(Font.BOLD,
						// "headache pain intensity Since last 06/07/2013",
						// font10);
						// Paragraph p1 = new Paragraph(phPLB);
						// p1.setSpacingBefore(10f);
						// p1.setSpacingAfter(20f);
						// document.add(p1);
						//
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						PLevelBarChart.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */, stream);
						Image png_attendant = Image.getInstance(stream.toByteArray());
						png_attendant.setAlignment(Element.ALIGN_MIDDLE);

						// Mihir 25072013
						Log.e("Mihir", "PLevelBarChart Width " + PLevelBarChart.getWidth() + " PLevelBarChart Heaight "
								+ PLevelBarChart.getHeight());
						png_attendant.scaleAbsolute(220, 220);

						/*
						 * png_attendant.scaleToFit(PLevelBarChart.getWidth(),
						 * PLevelBarChart.getHeight());
						 */
						PdfPCell imageCell = new PdfPCell(png_attendant);
						imageCell.setBorder(Rectangle.NO_BORDER);
						imageCell.setPadding(10f);
						stream.flush();
						stream.reset();
						document.add(png_attendant);

						Paragraph par = new Paragraph();
						par.setSpacingAfter(2f);
						par.setSpacingBefore(2f);
						document.add(par);

						ByteArrayOutputStream stream_for_pichart = new ByteArrayOutputStream();
						listPLevelBarChartField.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */,
								stream_for_pichart);
						Image png_attendant_for_pichart = Image.getInstance(stream_for_pichart.toByteArray());
						png_attendant_for_pichart.setAlignment(Element.ALIGN_MIDDLE);

						// Mihir 25072013
						Log.e("Mihir", "listPLevelBarChartField Width " + listPLevelBarChartField.getWidth()
								+ " listPLevelBarChartField Heaight " + listPLevelBarChartField.getHeight());
						// Width 184 Heaight 114 in 480x800
						// Width 180 Heaight 114 in 540x960
						// Width 240 Heaight 150 in 720x1280
						// Width 360 Heaight 225 in 1080x1920
						/*
						 * int scaleX = listPLevelBarChartField.getWidth() > 185
						 * ? 185 : listPLevelBarChartField.getWidth(); int
						 * scaleY = listPLevelBarChartField.getHeight() > 115 ?
						 * 115 : listPLevelBarChartField.getHeight();
						 */
						int scaleX = (int) (listPLevelBarChartField.getWidth() * Float.parseFloat(mcontext
								.getString(R.string.list_plevel_barchart_scale_x)));
						int scaleY = (int) (listPLevelBarChartField.getHeight() * Float.parseFloat(mcontext
								.getString(R.string.list_plevel_barchart_scale_y)));
						Log.e("Mihir", "New Scale Level " + scaleX + " Y " + scaleY);

						png_attendant_for_pichart.scaleAbsolute(scaleX, scaleY);

						/*
						 * png_attendant_for_pichart.scaleToFit(
						 * listPLevelBarChartField.getWidth(),
						 * listPLevelBarChartField.getHeight());
						 */
						PdfPCell imageCell_for_pichart = new PdfPCell(png_attendant_for_pichart);
						imageCell_for_pichart.setBorder(Rectangle.NO_BORDER);
						imageCell_for_pichart.setPaddingTop(30f);
						imageCell_for_pichart.setPaddingLeft(10f);
						imageCell_for_pichart.setPaddingRight(10f);
						imageCell_for_pichart.setPaddingBottom(10f);
						stream_for_pichart.flush();
						stream_for_pichart.reset();
						document.add(png_attendant_for_pichart);

						if (isDiary)
						{
							document.newPage();
						}
					}
				}
			}

			if (isDiary)
			{

				generatePdfForDiaryLog(document, columnDefinitionSize);

			}

			document.close();
			dialog.dismiss();
		}
		catch (Exception e)
		{
			dialog.dismiss();
			e.printStackTrace();
		}
	}

	private void generatePdfForDiaryLog(Document document, float[] columnDefinitionSize)
	{

		// set MainHeader of the page
		// mainHeader(document, columnDefinitionSize);
		generateDiaryLog(document, columnDefinitionSize);
		// document.newPage();
	}

	// }).start();

	// }

	private void generateDiaryLog(Document document, float[] columnDefinitionSize)
	{

		Iterator<String> keys = hasmap.keySet().iterator();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
		// SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
		Font font12Black = FontFactory.getFont("Helvetica", 12, Font.BOLD);
		font12Black.setSize(10f);
		Font font16Black = FontFactory.getFont("Helvetica", 12, Font.BOLD);
		Font font12 = FontFactory.getFont("Helvetica", 12, Font.BOLD);
		font12.setColor(harmony.java.awt.Color.lightGray);
		font12.setSize(10f);

		Font font12Blacksimple = FontFactory.getFont(FontFactory.HELVETICA, 12);
		font12Blacksimple.setSize(10f);

		try
		{

			while (keys.hasNext())
			{
				String key = (String) keys.next();

				PdfPTable tableMonth;
				tableMonth = new PdfPTable(1);
				tableMonth.setWidths(columnDefinitionSize);
				tableMonth.setWidthPercentage(100);
				tableMonth.setSpacingBefore(20f);
				tableMonth.getDefaultCell().setBorder(0);

				Phrase phMonth = new Phrase(Font.BOLD, key, font16Black);

				PdfPCell cellMonth = new PdfPCell(phMonth);
				cellMonth.setPaddingLeft(10f);
				cellMonth.setPaddingBottom(10f);
				cellMonth.setPaddingTop(5f);
				cellMonth.setColspan(columnDefinitionSize.length);
				cellMonth.setBackgroundColor(harmony.java.awt.Color.lightGray);
				cellMonth.setBorderWidth(1f);
				cellMonth.setBorderColor(harmony.java.awt.Color.lightGray);

				tableMonth.addCell(cellMonth);
				document.add(tableMonth);

				LinkedHashMap<Integer, ArrayList<DiaryLogDetailsModel>> eventsIds = hasmap.get(key);

				Iterator<Integer> eventKeys = eventsIds.keySet().iterator();

				while (eventKeys.hasNext())
				{
					Integer string = (Integer) eventKeys.next();
					/*
					 * Data main layout
					 */
					ArrayList<DiaryLogDetailsModel> logDetailsModels = eventsIds.get(string);
					/**
					 * Diarylog Data Table Main
					 */
					float[] columnDataMainSize = { 10F, 90F };
					PdfPTable tableDataMain;
					tableDataMain = new PdfPTable(2);
					tableDataMain.setWidths(columnDataMainSize);
					tableDataMain.setWidthPercentage(100);
					tableDataMain.setSpacingBefore(5f);
					tableDataMain.setSplitRows(false);
					tableDataMain.setKeepTogether(true);
					// tableDataMain.getDefaultCell().setBorderColor((harmony.java.awt.Color.lightGray));
					tableDataMain.getDefaultCell().setBorder(0);

					calendar.setTime(s.parse(logDetailsModels.get(0).getDate_time()));
					calendar.set(Calendar.HOUR_OF_DAY, logDetailsModels.get(0).getStart_hour());

					PdfPCell pCellLeft = new PdfPCell();
					pCellLeft.setBorder(0);
					pCellLeft.setBorderWidthBottom(1f);
					pCellLeft.setBorderWidthTop(1f);
					pCellLeft.setBackgroundColor(harmony.java.awt.Color.lightGray);
					pCellLeft.setBorderWidthLeft(1f);
					pCellLeft.setBorderColor(harmony.java.awt.Color.lightGray);

					/**
					 * DiaryLog Sub Table for Date
					 */
					PdfPTable tableDataSubDate;
					tableDataSubDate = new PdfPTable(1);
					tableDataSubDate.setWidths(columnDefinitionSize);
					tableDataSubDate.setWidthPercentage(100);
					tableDataSubDate.getDefaultCell().setBorder(0);

					PdfPCell cellDate = new PdfPCell(new Phrase(new SimpleDateFormat("dd").format(calendar.getTime()),
							FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15)));
					cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
					cellDate.setVerticalAlignment(Element.ALIGN_TOP);
					cellDate.setColspan(columnDefinitionSize.length);
					// cellDate.setBackgroundColor(harmony.java.awt.Color.lightGray);
					cellDate.setPaddingTop(10f);
					cellDate.setBorder(0);
					tableDataSubDate.addCell(cellDate);
					pCellLeft.addElement(tableDataSubDate);
					// tableDataMain.addCell(tableDataSubDate);

					PdfPCell pCellRight = new PdfPCell();
					pCellRight.setBorder(0);
					pCellRight.setBorderWidthBottom(1f);
					pCellRight.setBorderWidthTop(1f);
					pCellRight.setBorderWidthLeft(2f);
					pCellRight.setBorderWidthRight(1f);
					pCellRight.setBorderColor(harmony.java.awt.Color.lightGray);
					/**
					 * Right side Main Table tableDataSubAllData
					 */
					float[] columnDataMain = { 70F, 30F };
					PdfPTable tableDataSubAllData = new PdfPTable(2);
					tableDataSubAllData.setWidths(columnDataMain);
					tableDataSubAllData.setWidthPercentage(100);
					tableDataSubAllData.getDefaultCell().setBorder(0);
					// tableDataSubAllData.getDefaultCell().setBorderColorLeft(harmony.java.awt.Color.lightGray);
					// tableDataSubAllData.getDefaultCell().setBorderWidthLeft(1f);

					/**
					 * Right side Image Table tableDataSubAlldataImage
					 */

					float[] columnDataMainForImage = { 100F };
					PdfPTable tableDataSubAlldataImage = new PdfPTable(1);
					tableDataSubAlldataImage.setWidths(columnDataMainForImage);
					tableDataSubAlldataImage.setWidthPercentage(100);
					tableDataSubAlldataImage.getDefaultCell().setBorder(0);
					tableDataSubAlldataImage.setHorizontalAlignment(Element.ALIGN_MIDDLE);

					ByteArrayOutputStream logoStream = new ByteArrayOutputStream();
					Bitmap logoBitmap = null;

					int questionId = 0;
					ArrayList<Integer> list = new ArrayList<Integer>();

					for (int i = 0; i < logDetailsModels.size(); i++)
					{

						if (logDetailsModels.get(i).getRef_question_id() == 1)
						{

							if (logDetailsModels.get(i).getRef_answer_id() == 1)
							{
								logoBitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.mild);
							}
							else if (logDetailsModels.get(i).getRef_answer_id() == 2)
							{
								logoBitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.moderate);
							}
							else if (logDetailsModels.get(i).getRef_answer_id() == 3)
							{
								logoBitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.severe);
							}

							logoBitmap.compress(Bitmap.CompressFormat.PNG /*
																		 * FileType
																		 */, 100 /* Ratio */, logoStream);
							Image logo = Image.getInstance(logoStream.toByteArray());
							logo.scalePercent(26.4f);
							logo.setAlignment(Element.ALIGN_CENTER);
							PdfPCell logoCell = new PdfPCell(logo);
							logoCell.setBorder(0);
							logoCell.setPaddingLeft(0f);
							logoCell.setPaddingRight(0f);

							tableDataSubAlldataImage.addCell(logoCell);

							Phrase phTime = null;
							Phrase phCategory = null;

							// try {
							// final String _24HourTime = new SimpleDateFormat(
							// "HH:mm").format(calendar.getTime());
							//
							// final Date _24HourDt;
							//
							// _24HourDt = _24HourSDF.parse(_24HourTime);
							//
							// phTime = new Phrase(Font.BOLD,
							// _12HourSDF.format(_24HourDt)
							// + " lasted "
							// + (logDetailsModels.get(i)
							// .getDuration() / 60)
							// + " hours", font12Black);
							//
							// } catch (ParseException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }

							SimpleDateFormat hourformat = new SimpleDateFormat("hh");
							SimpleDateFormat ampmFormat = new SimpleDateFormat("a");
							final String hourslabel;
							final String minute;
							if (logDetailsModels.get(0).getStart_minute() < 10)
							{
								minute = "0" + logDetailsModels.get(0).getStart_minute();
							}
							else
							{
								minute = String.valueOf(logDetailsModels.get(0).getStart_minute());
							}
							hourslabel = (logDetailsModels.get(i).getDuration() / 60) == 1 ? "hour" : "hours";
							phTime = new Phrase(Font.BOLD, "" + hourformat.format(calendar.getTime()) + ":" + minute
									+ " " + ampmFormat.format(calendar.getTime()) + ", lasted for "
									+ logDetailsModels.get(0).getDuration() / 60 + " " + hourslabel, font12Black);

							PdfPCell cellTime = new PdfPCell(phTime);
							cellTime.setBorder(0);
							// cellAnswer.setBorderWidthLeft(1f);
							cellTime.setBorderWidthTop(1f);
							// cellAnswer.setBorderWidthBottom(1f);
							cellTime.setBorderColor(harmony.java.awt.Color.lightGray);
							cellTime.setHorizontalAlignment(Element.ALIGN_LEFT);
							cellTime.setVerticalAlignment(Element.ALIGN_MIDDLE);
							cellTime.setPaddingLeft(5f);
							cellTime.setPaddingBottom(5f);
							cellTime.setPaddingTop(5f);
							tableDataSubAllData.addCell(cellTime);

							phCategory = new Phrase(Font.BOLD, "Headache", font12);

							PdfPCell cellCategory = new PdfPCell(phCategory);
							cellCategory.setBorder(0);
							// cellCategory.setBorderWidthRight(1f);
							cellCategory.setBorderWidthTop(1f);
							// cellCategory.setBorderWidthBottom(1f);
							cellCategory.setBorderColor(harmony.java.awt.Color.lightGray);
							cellCategory.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cellCategory.setVerticalAlignment(Element.ALIGN_MIDDLE);
							cellCategory.setPaddingLeft(40f);
							cellCategory.setPaddingBottom(5f);
							cellCategory.setPaddingRight(8f);
							cellCategory.setPaddingTop(5f);
							tableDataSubAllData.addCell(cellCategory);

							pCellRight.addElement(tableDataSubAlldataImage);
						}
						else
						{

							// for (int i = 0; i < logDetailsModels.size(); i++)
							// {
							questionId = logDetailsModels.get(i).getRef_question_id();

							if (list.contains(questionId))
							{
								continue;
							}

							Phrase phAnswer = null;
							String ans = "";
							for (int j = i; j < logDetailsModels.size(); j++)
							{

								if (questionId == Constant.QUESTION_FASTING_ID
										|| questionId == Constant.QUESTION_SKIP_MEAL_ID
										|| questionId == Constant.QUESTION_FOOD_ID)
								{

									for (int k = j; k < logDetailsModels.size(); k++)
									{
										if (logDetailsModels.get(k).getRef_question_id() >= Constant.QUESTION_SKIP_MEAL_ID
												&& logDetailsModels.get(k).getRef_question_id() <= Constant.QUESTION_FOOD_ID)
										{
											ans += logDetailsModels.get(k).getAnswer() + ", ";
											if (!list.contains(logDetailsModels.get(k).getRef_question_id()))
											{
												list.add(logDetailsModels.get(k).getRef_question_id());

											}

										}
										else
											break;
										j = k;
									}
									break;

								}
								else if (questionId == logDetailsModels.get(j).getRef_question_id())
								{
									ans += logDetailsModels.get(j).getAnswer() + ", ";
								}
							}
							list.add(questionId);

							// if (logDetailsModels.get(i).getRef_question_id()
							// == 1) {
							//
							// try {
							// final String _24HourTime = new SimpleDateFormat(
							// "HH:mm").format(calendar.getTime());
							//
							// final Date _24HourDt;
							//
							// _24HourDt = _24HourSDF.parse(_24HourTime);
							//
							// phAnswer = new Phrase(Font.BOLD,
							// _12HourSDF.format(_24HourDt)
							// + " lasted "
							// + (logDetailsModels.get(i)
							// .getDuration() / 60)
							// + " hours", font12Black);
							//
							// } catch (ParseException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }
							//
							// } else {

							ans = ans.substring(0, ans.lastIndexOf(","));
							phAnswer = new Phrase(ans.trim(), font12Blacksimple);

							// }

							PdfPCell cellAnswer = new PdfPCell(phAnswer);
							cellAnswer.setBorder(0);
							// cellAnswer.setBorderWidthLeft(1f);
							cellAnswer.setBorderWidthTop(1f);
							// cellAnswer.setBorderWidthBottom(1f);
							cellAnswer.setBorderColor(harmony.java.awt.Color.lightGray);
							cellAnswer.setHorizontalAlignment(Element.ALIGN_LEFT);
							cellAnswer.setVerticalAlignment(Element.ALIGN_MIDDLE);
							cellAnswer.setPaddingLeft(5f);
							cellAnswer.setPaddingBottom(5f);
							cellAnswer.setPaddingTop(5f);

							tableDataSubAllData.addCell(cellAnswer);

							Phrase phCategory = null;

							// if (logDetailsModels.get(i).getRef_question_id()
							// == 1) {
							//
							// phCategory = new Phrase(Font.BOLD, "Headache",
							// font12);
							//
							// } else {

							if (logDetailsModels.get(i).getRef_question_id() == 3)
								phCategory = new Phrase(Font.BOLD, "Warning signs", font12);
							else if (logDetailsModels.get(i).getRef_question_id() == 2)
								phCategory = new Phrase(Font.BOLD, "Location", font12);
							else if (logDetailsModels.get(i).getRef_question_id() == 4)
								phCategory = new Phrase(Font.BOLD, "Symptoms", font12);
							else if (logDetailsModels.get(i).getRef_question_id() == 7
									|| logDetailsModels.get(i).getRef_question_id() == 8
									|| logDetailsModels.get(i).getRef_question_id() == 9)
								phCategory = new Phrase(Font.BOLD, "Diet", font12);
							else if (logDetailsModels.get(i).getRef_question_id() == 10)
								phCategory = new Phrase(Font.BOLD, "Lifestyle", font12);
							else if (logDetailsModels.get(i).getRef_question_id() == 11)
								phCategory = new Phrase(Font.BOLD, "Environment", font12);
							else if (logDetailsModels.get(i).getRef_question_id() == 12)
								phCategory = new Phrase(Font.BOLD, "Menstruation", font12);
							else if (logDetailsModels.get(i).getRef_question_id() == 13)
								phCategory = new Phrase(Font.BOLD, "Notes", font12);
							// }

							PdfPCell cellCategory = new PdfPCell(phCategory);
							cellCategory.setBorder(0);
							// cellCategory.setBorderWidthRight(1f);
							cellCategory.setBorderWidthTop(1f);
							// cellCategory.setBorderWidthBottom(1f);
							cellCategory.setBorderColor(harmony.java.awt.Color.lightGray);
							cellCategory.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cellCategory.setVerticalAlignment(Element.ALIGN_MIDDLE);
							cellCategory.setPaddingLeft(40f);
							cellCategory.setPaddingBottom(5f);
							cellCategory.setPaddingRight(8f);
							cellCategory.setPaddingTop(5f);

							tableDataSubAllData.addCell(cellCategory);

						}

						// }
						// pCellRight.addElement(tableDataSubAllData);
					}

					if (notes && logDetailsModels.get(0).getNotes() != null
							&& !logDetailsModels.get(0).getNotes().equalsIgnoreCase(""))
					{

						PdfPCell cellAnswer = new PdfPCell(new Phrase(logDetailsModels.get(0).getNotes(),
								font12Blacksimple));
						cellAnswer.setBorder(0);
						// cellAnswer.setBorderWidthLeft(1f);
						cellAnswer.setBorderWidthTop(1f);
						// cellAnswer.setBorderWidthBottom(1f);
						cellAnswer.setBorderColor(harmony.java.awt.Color.lightGray);
						cellAnswer.setHorizontalAlignment(Element.ALIGN_LEFT);
						cellAnswer.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cellAnswer.setPaddingLeft(5f);
						cellAnswer.setPaddingBottom(5f);
						cellAnswer.setPaddingTop(5f);
						tableDataSubAllData.addCell(cellAnswer);

						PdfPCell cellCategory = new PdfPCell(new Phrase("Notes", font12));
						cellCategory.setBorder(0);
						// cellCategory.setBorderWidthRight(1f);
						cellCategory.setBorderWidthTop(1f);
						// cellCategory.setBorderWidthBottom(1f);
						cellCategory.setBorderColor(harmony.java.awt.Color.lightGray);
						cellCategory.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cellCategory.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cellCategory.setPaddingLeft(40f);
						cellCategory.setPaddingBottom(5f);
						cellCategory.setPaddingRight(10f);
						cellCategory.setPaddingTop(5f);
						tableDataSubAllData.addCell(cellCategory);
						// pCellRight.addElement(tableDataSubAllData);

					}
					pCellRight.addElement(tableDataSubAllData);
					tableDataMain.addCell(pCellLeft);
					tableDataMain.addCell(pCellRight);
					// tableDataSubAlldataImage.addCell(tableDataSubAllData);
					// tableDataMain.addCell(tableDataSubAlldataImage);
					document.add(tableDataMain);

				}
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	private File createFolderWithFile(String fileName)
	{
		if (!new File(FILEDIR).exists())
		{
			new File(FILEDIR).mkdirs();
		}

		File copy_File = new File(new File(FILEDIR) + "/" + fileName);
		return copy_File;

	}

	private void mainHeader(Document document, float[] columnDefinitionSize)
	{

		try
		{

			PdfPTable tableHeader;
			tableHeader = new PdfPTable(1);
			tableHeader.setWidths(columnDefinitionSize);
			tableHeader.setWidthPercentage(100);
			tableHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			tableHeader.setHorizontalAlignment(Element.ALIGN_MIDDLE);

			ByteArrayOutputStream logoStream = new ByteArrayOutputStream();
			Bitmap logoBitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.printed_header_old);
			logoBitmap.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */, logoStream);
			Image logo = Image.getInstance(logoStream.toByteArray());
			logo.scalePercent(28f);
			logo.setAlignment(Element.ALIGN_CENTER);
			logo.setBorder(Rectangle.NO_BORDER);
			PdfPCell logoCell = new PdfPCell(logo);
			logoCell.setBorder(Rectangle.NO_BORDER);
			logoCell.setPaddingLeft(-1f);

			tableHeader.addCell(logoCell);
			document.add(tableHeader);
		}
		catch (Exception e)
		{
		}

	}

	private void setTitle(Document document, String chartName)
	{
		try
		{
			PdfPTable tableSubHeader;

			tableSubHeader = new PdfPTable(1);
			tableSubHeader.setWidthPercentage(100);
			tableSubHeader.getDefaultCell().setVerticalAlignment(0);
			tableSubHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			ByteArrayOutputStream logoTitle = new ByteArrayOutputStream();
			Bitmap titleBitmap = null;
			if (chartName.equalsIgnoreCase("potentialTrigger"))
			{
				titleBitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.potential_triggers_new);
			}
			else if (chartName.equalsIgnoreCase("triggerExposure"))
			{

				titleBitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.trigger_exposures_new);
			}
			else if (chartName.equalsIgnoreCase("symptomsFrequency"))
			{
				titleBitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.symptom_frequency_new);
			}
			else if (chartName.equalsIgnoreCase("painLocation"))
			{
				titleBitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.pain_location_new);
			}
			else if (chartName.equalsIgnoreCase("painLevel"))
			{
				titleBitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.pain_level_new);
			}
			titleBitmap.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */, logoTitle);
			Image titleImage = Image.getInstance(logoTitle.toByteArray());
			titleImage.scalePercent(37f);
			titleImage.setAlignment(Element.ALIGN_LEFT);
			titleImage.setBorder(Rectangle.NO_BORDER);
			titleImage.setSpacingAfter(10f);
			PdfPCell titleCell = new PdfPCell(titleImage);
			titleCell.setBorder(Rectangle.NO_BORDER);
			titleCell.setPaddingTop(10f);
			titleCell.setPaddingLeft(140f);

			tableSubHeader.addCell(titleCell);
			document.add(tableSubHeader);

		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

	}

}
