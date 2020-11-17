package dependency;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfToPng {
    private String picturePath;
    private String pdfName;
    private static String pathSeperator = "/";

    public PdfToPng(String picturePath, String pdfName){
        this.picturePath = picturePath;
        this.pdfName = pdfName;
    }

    /**
     * @Author shijie.hu
     * @param currentPage 页码
     * @param pictureName 提取的图片名
     */
    public void writePdfToPng(File pdfFile,int currentPage,String pictureName){
        try
        {
            // 打开来源 pdf
            PDDocument pdfDocument = PDDocument.load(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);

            //存放图片的文件夹路径
            String completedPicturePath = picturePath + pathSeperator + pdfName;
            File file = new File(completedPicturePath);
            file.mkdir();

            // 以300 dpi 读取存入 BufferedImage 对象
            int dpi = 300;
            BufferedImage buffImage = pdfRenderer.renderImageWithDPI(currentPage, dpi, ImageType.RGB);
            // 将 BufferedImage 写入到 png
            ImageIOUtil.writeImage(buffImage, completedPicturePath+pathSeperator+pictureName+".png", dpi);
            // 关闭文档
            pdfDocument.close();
        }
        catch (InvalidPasswordException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
