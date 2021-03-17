package main;

import javafx.collections.ObservableList;
import jxl.read.biff.BiffException;
import jxl.write.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class FileManager {

    public static void saveExcel(String origin, File destination, int number, String date,
                                 String organization, String subdivision,
                                 String periodFrom, String periodUntil,
                                 ObservableList<TableRow> table, String[] total,
                                 String matName, String buhName) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(origin)));
        HSSFSheet sheet = workbook.getSheetAt(0);
        CellReference cr;
        Row row;
        Cell cell;
        CellStyle style;
        DataFormat format = workbook.createDataFormat();

        cr = new CellReference("A6");
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(organization);

        cr = new CellReference("A8");
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(subdivision);

        cr = new CellReference("AP14");
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(number);

        cr = new CellReference("AX14");
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(date);

        cr = new CellReference("BF14");
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(periodFrom);

        cr = new CellReference("BK14");
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(periodUntil);

        cr = new CellReference("V" + 29);
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(buhName);

        cr = new CellReference("AN" + 26);
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(matName);

        cr = new CellReference("Q" + 26);
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(getReponsibleNamesJob(matName));

        int currentRow = 22;
        String[] cols = {"A", "E", "M", "P", "U", "Y", "AD", "AK", "AQ", "AU", "AZ", "BD", "BI", "BM", "BS"};
        for (TableRow tableRow : table){
            // insert new row
            RowCopy.copyRow(workbook, sheet, currentRow, currentRow + 1);
            for (int i = 0; i <= 75; i++){
                row = sheet.getRow(currentRow);
                cell = row.getCell(i);
                style = cell.getCellStyle();

                row = sheet.getRow(currentRow + 1);
                row.createCell(i);
                cell = row.getCell(i);
                cell.setCellStyle(style);
            }
            // insert data
            cr = new CellReference(cols[0] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getNumber());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[1] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getTitle());

            cr = new CellReference(cols[2] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getCode());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[3] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getPrice());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0.00"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[4] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getAmount());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[5] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getPrice());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0.00"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[6] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getIncome());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[7] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getSentToStorageOrCompensatedByRecipient());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[8] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getBrokenAmount());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[9] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getBrokenTotalPrice());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0.00"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[10] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getLostAmount());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[11] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getLostTotalPrice());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0.00"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[12] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getLeftInTheEndAmount());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[13] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getLeftInTheEndTotalPrice());
            style = cell.getCellStyle();
            style.setDataFormat(format.getFormat("0.00"));
            cell.setCellStyle(style);

            cr = new CellReference(cols[14] + (currentRow + 1));
            row = sheet.getRow(cr.getRow());
            cell = row.getCell(cr.getCol());
            cell.setCellValue(tableRow.getComment());

            currentRow++;
        }

        row = sheet.getRow(currentRow);
        sheet.removeRow(row);

        currentRow++;

        int iTotal = 0;

        cr = new CellReference(cols[4] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0"));
        cell.setCellStyle(style);

        cr = new CellReference(cols[5] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0.00"));
        cell.setCellStyle(style);

        cr = new CellReference(cols[6] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0"));
        cell.setCellStyle(style);

        cr = new CellReference(cols[7] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0"));
        cell.setCellStyle(style);

        cr = new CellReference(cols[8] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0"));
        cell.setCellStyle(style);

        cr = new CellReference(cols[9] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0.00"));
        cell.setCellStyle(style);

        cr = new CellReference(cols[10] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0"));
        cell.setCellStyle(style);

        cr = new CellReference(cols[11] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0.00"));
        cell.setCellStyle(style);

        cr = new CellReference(cols[12] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0"));
        cell.setCellStyle(style);

        cr = new CellReference(cols[13] + (currentRow + 1));
        row = sheet.getRow(cr.getRow());
        cell = row.getCell(cr.getCol());
        cell.setCellValue(total[iTotal++]);
        style = cell.getCellStyle();
        style.setDataFormat(format.getFormat("0.00"));
        cell.setCellStyle(style);

        FileOutputStream outputStream = new FileOutputStream(destination);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


    public static int getNumber() throws IOException {
        String text = getStringForJson("src\\main\\document.json");
        JSONObject document = new JSONObject(text);
        return document.getInt("number");
    }

    public static void setNumber(int number) throws IOException {
        File file = new File("src\\main\\document.json");
        FileWriter writer = new FileWriter(file);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("number", number);
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
    }

    public static ArrayList<String> getOrganizations() throws IOException {
        String text = getStringForJson("src\\main\\organizations.json");
        JSONArray jsonArray = new JSONArray(text);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            result.add(object.getString("title"));
        }
        return result;
    }

    public static ArrayList<String> getSubdivisions(String organization) throws IOException {
        String text = getStringForJson("src\\main\\organizations.json");
        JSONArray jsonArray = new JSONArray(text);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            if (object.getString("title").equals(organization)){
                JSONArray subdivisions = object.getJSONArray("subdivisions");
                for (int j = 0; j < subdivisions.length(); j++) {
                    result.add(subdivisions.getString(j));
                }
                break;
            }
        }
        return result;
    }

    public static ArrayList<String> getDishNames() throws IOException {
        String text = getStringForJson("src\\main\\items.json");
        JSONArray jsonArray = new JSONArray(text);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(jsonArray.getJSONObject(i).getString("title"));
        }
        return result;
    }

    public static ArrayList<String> getBuhNames() throws IOException {
        String text = getStringForJson("src\\main\\people.json");
        JSONArray jsonArray = new JSONArray(text);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("title").equals("Бухгалтер"))
                result.add(jsonArray.getJSONObject(i).getString("name"));
        }
        return result;
    }

    public static ArrayList<String> getReponsibleNames() throws IOException {
        String text = getStringForJson("src\\main\\people.json");
        JSONArray jsonArray = new JSONArray(text);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("title").equals("Ответственный"))
                result.add(jsonArray.getJSONObject(i).getString("name"));
        }
        return result;
    }

    public static String getReponsibleNamesJob(String name) throws IOException {
        String text = getStringForJson("src\\main\\people.json");
        JSONArray jsonArray = new JSONArray(text);
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("name").equals(name))
                return jsonArray.getJSONObject(i).getString("job");
        }
        return "";
    }

    public static String getDishNameByCode(int code) throws IOException {
        String text = getStringForJson("src\\main\\items.json");
        JSONArray jsonArray = new JSONArray(text);
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getInt("code") == code)
                return jsonArray.getJSONObject(i).getString("title");
        }
        return "";
    }

    public static String getDishPriceByCode(int code) throws IOException {
        String text = getStringForJson("src\\main\\items.json");
        JSONArray jsonArray = new JSONArray(text);
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getInt("code") == code)
                return jsonArray.getJSONObject(i).getBigDecimal("price").toString();
        }
        return "";
    }

    public static ArrayList<String> getDishCodes() throws IOException {
        String text = getStringForJson("src\\main\\items.json");
        JSONArray jsonArray = new JSONArray(text);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(Integer.toString(jsonArray.getJSONObject(i).getInt("code")));
        }
        return result;
    }

    public static String getDishCodeByName(String name) throws IOException {
        String text = getStringForJson("src\\main\\items.json");
        JSONArray jsonArray = new JSONArray(text);
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("title").equals(name))
                return Integer.toString(jsonArray.getJSONObject(i).getInt("code"));
        }
        return "";
    }

    private static String getStringForJson(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        char[] a = new char[(int) file.length()];
        fileReader.read(a);
        fileReader.close();
        return new String(a);
    }
}
