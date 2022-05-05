package com.scoremanagement.services.impl;

import com.scoremanagement.entities.export_import.BaseExportExcelModel;
import com.scoremanagement.entities.export_import.MetadataExcelModel;
import com.scoremanagement.services.ExportExcelFileService;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;

import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExportExcelFileServiceImpl implements ExportExcelFileService {
    @Override
    public File exportFile(String fileName, String sheetName, List<BaseExportExcelModel> dataExport, Class<? extends BaseExportExcelModel> classType) {
        File excelFile = null;
        try {
            Path pathExcelFile = Files.createTempFile(Paths.get("E:\\NTQ\\Download"), fileName, ".xlsx");
            excelFile = pathExcelFile.toFile();
            POIXMLDocument workbook = exportExcel(dataExport, classType, sheetName);

            FileOutputStream outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelFile != null && excelFile.exists()) {
                excelFile.deleteOnExit();
            }
        }
        return null;
    }

    public <T> POIXMLDocument exportExcel(List<BaseExportExcelModel> listData, Class<? extends BaseExportExcelModel> classType, String sheetName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            Constructor<? extends BaseExportExcelModel> ctor = classType.getConstructor();
            BaseExportExcelModel object = ctor.newInstance();
            List<String> headers = object.getHeaders();
            List<MetadataExcelModel> listMetadata = object.getListMetadata();
            Sheet sheet = workbook.createSheet(sheetName);
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headers.size(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers.get(col));
                cell.setCellStyle(headerStyle(sheet));
            }
            AtomicInteger rowIdx = new AtomicInteger(1);
            listData.forEach(item -> {
                int index = rowIdx.getAndIncrement();
                Row row = sheet.createRow(index);
                row.createCell(0).setCellValue(index);
                for (MetadataExcelModel info : listMetadata) {
                    try {
                        String fieldName = info.getFieldName();
                        Method methodGet = classType.getDeclaredMethod(
                                "get" + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1)
                        );
                        Object valueObject = methodGet.invoke(item);
                        String value = "";
                        if (valueObject != null) {
                            //TODO: Need modify when has other type need convert
                            switch (info.getParameterType().getName()) {
                                case "java.time.LocalDate":
                                    value = DATE_FORMATTER.format((LocalDate) valueObject);
                                    break;
                                case "java.time.LocalDateTime":
                                    value = DATE_TIME_FORMATTER.format((LocalDateTime) valueObject);
                                    break;
                                default:
                                    value = valueObject.toString();
                            }
                        }
                        row.createCell(info.getPosition()).setCellValue(value);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    private CellStyle headerStyle(Sheet sheet) {
        // Tạo định dạng: font Times New Roman, in đậm, font-size 14, chữ màu trắng
        Font font = sheet.getWorkbook().createFont();
//        font.setFontName("Times New Roman");
        font.setBold(true);

//        font.setFontHeightInPoints((short) 14); // font size
//        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Tạo cell style áp dụng font ở trên
        // Sử dụng màu nền xanh (Blue), định dạng border dưới
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);

//        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
//        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }
}
