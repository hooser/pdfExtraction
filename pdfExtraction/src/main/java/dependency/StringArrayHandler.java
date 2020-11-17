package dependency;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.CsvEntity;
import entity.ReturnEntity;
import opt.GetSourceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class StringArrayHandler {

    private final String pictureOrFormStartString = "图"+"[ ]*"+"\\d+"+".*";
    private final String pictureOrFormEndString = "资料来源.*";
    private final String nameSeperator = "_";

    private  Pattern pictureOrFormStartPattern;
    private  Pattern pictureOrFormEndPattern;

    private String targetPath;
    private String pdfName;
    private PdfToPng pdfToPng;
    private IllegalCharInNameHandler illegalCharInNameHandler = new IllegalCharInNameHandler();

    private static String encoding = "UTF-8";
    private static Logger log = LoggerFactory.getLogger(StringArrayHandler.class);

    public StringArrayHandler(String targetPath, String pdfName){
        this.targetPath = targetPath;
        this.pdfName = pdfName;
        pdfToPng = new PdfToPng(targetPath,illegalCharInNameHandler.legalizeName(pdfName));
    }

    public ReturnEntity writeContentToFile(String[] stringArray, int currentPage, File pdfFile, String pdfName, String prePictureTitle) throws FileNotFoundException, UnsupportedEncodingException {

        pictureOrFormStartPattern = Pattern.compile(pictureOrFormStartString);
        pictureOrFormEndPattern = Pattern.compile(pictureOrFormEndString);

        int lineNum = 0;
        ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setWillGetSourcce(GetSourceEnum.NOGET.getCode());                                     //默认资料来源不在下个页面
        List<CsvEntity> csvList = new ArrayList();
        int pictureNums = 0;
        String pictureTitle = "";
        String pictureSource = "";
        if(!StringUtils.isEmpty(prePictureTitle)){
            Matcher pictureOrFormEndMatcher = pictureOrFormEndPattern.matcher(stringArray[lineNum]);
            while(!pictureOrFormEndMatcher.matches() && lineNum < stringArray.length){
                lineNum++;
                if(lineNum < stringArray.length){
                    pictureOrFormEndMatcher = pictureOrFormEndPattern.matcher(stringArray[lineNum]);
                }
            }
            if(lineNum == stringArray.length){
                prePictureTitle = "";
            }
            else{
                csvList.add(new CsvEntity(pdfName + nameSeperator + (currentPage-1), prePictureTitle, stringArray[lineNum]));
            }
        }
        for(;lineNum < stringArray.length;lineNum++) {
            Matcher pictureOrFormStartMatcher = pictureOrFormStartPattern.matcher(stringArray[lineNum]);
            if (pictureOrFormStartMatcher.matches()) {
                pictureTitle = stringArray[lineNum].replaceAll("[ ]+","");
                pdfToPng.writePdfToPng(pdfFile, currentPage - 1,illegalCharInNameHandler.legalizeName(pictureTitle));
                Matcher pictureOrFormEndMatcher = pictureOrFormEndPattern.matcher(stringArray[lineNum]);
                while (lineNum < stringArray.length && !pictureOrFormEndMatcher.matches()) {
                    lineNum++;
                    if(lineNum < stringArray.length){
                        pictureOrFormEndMatcher = pictureOrFormEndPattern.matcher(stringArray[lineNum]);
                    }
                }
                //“资料来源” 位于下一页面
                if(lineNum == stringArray.length){
                    returnEntity.setWillGetSourcce(GetSourceEnum.GET.getCode());
                    returnEntity.setPictureTitle(pictureTitle);
                    returnEntity.setCsvList(csvList);
                    return returnEntity;
                }
                else{
                    pictureSource = stringArray[lineNum];
                }
            }
            if (pictureTitle != "" && pictureSource != "") {
                csvList.add(new CsvEntity(pdfName + nameSeperator + currentPage + nameSeperator + pictureNums, pictureTitle, pictureSource));
                pictureNums++;
                pictureTitle = "";
                pictureSource = "";
            }
        }
        returnEntity.setWillGetSourcce(GetSourceEnum.NOGET.getCode());
        returnEntity.setPictureTitle("");
        returnEntity.setCsvList(csvList);
        return returnEntity;
    }
}
