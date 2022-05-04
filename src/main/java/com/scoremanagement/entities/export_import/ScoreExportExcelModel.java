package com.scoremanagement.entities.export_import;

import com.scoremanagement.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreExportExcelModel extends BaseExportExcelModel {
    private String courseName;
    private Double score;

    @Override
    public List<MetadataExcelModel> getListMetadata() {
        return Arrays.asList(
                new MetadataExcelModel(1, "course", String.class, "Course"),
                new MetadataExcelModel(2, "score", Double.class, "Score")
        );
    }
}
