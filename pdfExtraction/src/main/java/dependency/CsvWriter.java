package dependency;

import entity.CsvEntity;
import lombok.Data;

import java.io.*;
import java.util.List;

@Data
public class CsvWriter {
    private String csvPath;
    private String csvName;
    private static String pathSeparator = "/";

    public CsvWriter(String csvPath,String csvName){
        this.csvPath = csvPath;
        this.csvName = csvName;
    }

    public void writeIntoCsv(List<CsvEntity> csvList) {
        if (csvList != null && csvList.size() > 0) {
            // 表格头
            String[] headArr = new String[]{"图片名称", "图片标题", "资料来源"};
            //CSV文件路径及名称
            String filePath = this.csvPath; //CSV文件路径
            String fileName = this.csvName + ".csv";//CSV文件名称
            File csvFile = null;
            BufferedWriter csvWriter = null;
            try {
                csvFile = new File(filePath + pathSeparator + fileName);
                csvFile.createNewFile();
                csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "utf-8"),1024);

                // 写入文件头部标题行
                csvWriter.write(String.join(",", headArr));
                csvWriter.newLine();

                // 写入文件内容
                for (CsvEntity csvEntity : csvList) {
                    csvWriter.write(csvEntity.toRow());
                    csvWriter.newLine();
                    csvWriter.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
