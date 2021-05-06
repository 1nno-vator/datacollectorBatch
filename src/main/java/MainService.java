import config.MyBatisConnectionFactory;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainService {

    private static final String API_URL = "http://api.vworld.kr/req/data?service=data&request=GetFeature&data=LT_L_SPRD&size=1000";
    private static final String key = "F971EF38-A449-3F8D-A6A7-E0911427C167";
    private static final String domain = "F971EF38-A449-3F8D-A6A7-E0911427C167";
    // attrFilter=emdCd:=:11110101

    public static void main(String[] args) throws Exception {
        String resource = "application.properties";
        Properties properties = new Properties();

        MainDAO dao = new MainDAO(MyBatisConnectionFactory.getSqlSessionFactory());

        List<Map<String, Object>> bjdList = dao.getBjd();

        System.out.println("RESULT - - - - ");

        try {
            Reader reader = Resources.getResourceAsReader(resource);
            properties.load(reader);

            System.out.println(properties.getProperty("key"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("resource read error");
        }

        for (Map row : bjdList) {
            System.out.println(row);
            String BJD_CD = String.valueOf(row.get("CONV_BJD_CD"));
        }
    }

}
