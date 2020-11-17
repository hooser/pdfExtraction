import dependency.IllegalCharInNameHandler;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PdfboxUtilsTest {

    @Autowired
    private IllegalCharInNameHandler illegalCharInNameHandler;

//    private final String[] illegalChars = { "\\","/","：",":","，","*","?","\"","<",">","|"};
//    private final String[] replaceChars = {"_","_","_","_","_","_","_","_","_","_","_"};
//
//    public String legalizeName(String preName){
//        return  StringUtils.replaceEach(preName,illegalChars,replaceChars);
//    }

    @Test
    public void testPageNumber(){
        String s = "algkjkd：快乐//dkl,;lds--ldsk";
        System.out.println(illegalCharInNameHandler.legalizeName(s));
    }
}
