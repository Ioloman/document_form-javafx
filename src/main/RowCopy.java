package main;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

public class RowCopy {


    public static void copyRow(HSSFWorkbook workbook, HSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        HSSFRow newRow = worksheet.getRow(destinationRowNum);
        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            HSSFCellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            ;
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());
            newCell.setCellValue(oldCell.getStringCellValue());


            // If there are are any merged regions in the source row, copy to new row
            for (i = 0; i < worksheet.getNumMergedRegions(); i++) {
                CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
                if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                    CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                            (newRow.getRowNum() +
                                    (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                    )),
                            cellRangeAddress.getFirstColumn(),
                            cellRangeAddress.getLastColumn());
                    worksheet.addMergedRegion(newCellRangeAddress);
                }
            }
        }
    }
}