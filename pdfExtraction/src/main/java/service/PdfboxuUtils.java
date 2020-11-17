package service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import dependency.CsvWriter;
import dependency.StringArrayHandler;
import entity.CsvEntity;
import entity.ReturnEntity;
import opt.GetSourceEnum;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;


public class PdfboxuUtils {

    private final String pathSeperator = "/";

    /**
     * 读入pdf文件地址，输出解析后的txt文件
     * @param filePath
     * @param targetPath
     * @throws IOException
     */
    public static void extractWords(String filePath, String targetPath, String csvPath) throws IOException {
        try {
            // 开始提取页数
            int startPage = 0;
            // 结束提取页数
            int endPage = Integer.MAX_VALUE;
            String content = null;
            File file = new File(filePath);
            File[] files=file.listFiles();
            for(File tfile:files){
                String prePictureTitle = "";                        //上一页的图片title
                List<CsvEntity> resultList = new ArrayList();
                String totalName = tfile.getName();
                int dotPosition = totalName.lastIndexOf('.');
                String pdfName = totalName.substring(0,dotPosition);
                System.out.println(pdfName);
                StringArrayHandler stringArrayHandler = new StringArrayHandler(targetPath,pdfName);
                PDDocument document = PDDocument.load(tfile);
                PDFTextStripper pts = new PDFTextStripper();
                startPage = 1;
                endPage = document.getNumberOfPages();
                System.out.println("Total Page: " + endPage);
                for(int currentPage = startPage; currentPage <= endPage; currentPage++){
                    pts.setStartPage(currentPage);
                    pts.setEndPage(currentPage);
                    //content就是从pdf中解析出来的文本
                    content = pts.getText(document);
//                    System.out.println(content);
                    String[] stringArray = content.split("\r\n");
                    ReturnEntity returnEntity = stringArrayHandler.writeContentToFile(stringArray,currentPage,tfile,pdfName,prePictureTitle);
                    prePictureTitle = "";
                    //---“资料来源” 在下一页面，需要记录returnEntity的title
                    if(returnEntity.getWillGetSourcce() == GetSourceEnum.GET.getCode()){
                        prePictureTitle = returnEntity.getPictureTitle();
                    }

                    resultList.addAll(returnEntity.getCsvList());
                }
                CsvWriter csvWriter = new CsvWriter(csvPath,pdfName);
                csvWriter.writeIntoCsv(resultList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
//        String inputPath = args[0];
//        String outputPath = args[1];
//        extractWords(args[0],args[1]);
        extractWords("G:/yanbao","F:/p","F:/csvFile");
    }
}
