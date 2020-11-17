package entity;

import lombok.Data;
import opt.GetSourceEnum;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReturnEntity {
    private int willGetSourcce;                                        //资料来源位于下一页
    private String pictureTitle;                                       //图片标题
    private List<CsvEntity> csvList;

    public ReturnEntity(){
        this.willGetSourcce = GetSourceEnum.NOGET.getCode();
        this.pictureTitle = "";
        this.csvList = new ArrayList();
    }
}
