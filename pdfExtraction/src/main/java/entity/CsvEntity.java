package entity;
import lombok.Data;

@Data
public class CsvEntity {
    private String pictureName;
    private String pictureTitle;
    private String pictureSource;

    public CsvEntity(String pictureName, String pictureTitle, String pictureSource){
        this.pictureName = pictureName;
        this.pictureTitle = pictureTitle;
        this.pictureSource = pictureSource;
    }

    public String toRow(){
        return String.format("%s,%s,%s",this.pictureName,this.pictureTitle,this.pictureSource);
    }
}
