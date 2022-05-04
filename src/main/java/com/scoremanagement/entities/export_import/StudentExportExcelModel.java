package com.scoremanagement.entities.export_import;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExportExcelModel extends BaseExportExcelModel {
    private String rollNumber;
    private String fullName;
    private Date dob;
    private String gender;
    private Double score;

    @Override
    public List<MetadataExcelModel> getListMetadata() {
        return Arrays.asList(
                new MetadataExcelModel(1, "rollNumber", Long.class, "Student Roll"),
                new MetadataExcelModel(2, "fullName", String.class, "Full Name"),
                new MetadataExcelModel(3, "dob", Date.class, "D.O.B"),
                new MetadataExcelModel(4, "gender", String.class, "Gender"),
                new MetadataExcelModel(5, "score", Boolean.class, "Score")
        );
    }

}
