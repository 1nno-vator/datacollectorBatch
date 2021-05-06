import com.fasterxml.jackson.databind.ObjectMapper;
import config.MyBatisConnectionFactory;
import org.apache.ibatis.io.Resources;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class MainService {
    private static final String DEFUALT_API_URL = "http://api.vworld.kr/req/data?service=data&request=GetFeature&data=LT_L_SPRD&size=1000";
    private static String API_URL = "";
    // attrFilter=emdCd:=:11110101

    public static void main(String[] args) throws Exception {
        String resource = "application.properties";
        Properties properties = new Properties();

        MainDAO dao = new MainDAO(MyBatisConnectionFactory.getSqlSessionFactory());

        List<Map<String, Object>> bjdList = dao.getBjd();

        try {
            Reader reader = Resources.getResourceAsReader(resource);
            properties.load(reader);

            // truncate table
            System.out.println(properties.getProperty("key"));
            String API_KEY = properties.getProperty("key");
            String DOMAIN = properties.getProperty("domain");
            API_URL = DEFUALT_API_URL
                        + Util.makeQueryString("key", API_KEY)
                        + Util.makeQueryString("domain", DOMAIN);

            dao.truncateTable("RDNM_ADRS_MS");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("property file read error");
        }

        List<Map<String, Object>> adrsList = new ArrayList<>();

        for (Map row : bjdList) {

            adrsList.clear();

            String BJD_CD = String.valueOf(row.get("conv_bjd_cd"));
            String filter = "emdCd:=:" + BJD_CD;

            String REQUEST_URL = API_URL + Util.makeQueryString("attrFilter", filter);
            JSONObject responseData = Util.callAPI(REQUEST_URL);

            JSONArray features = Util.getFeatures(responseData);

            for (int i=0; i<features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);

                String id = getId(feature);
                String type = getGeoType(feature);
                String coords = getCoordinates(feature).replace("[[[","[").replace("]]]","]");
                coords = coords.replace(","," ");
                coords = coords.replace("] [", ",");
                coords = coords.replace("[", "((").replace("]", "))");
                String coordinates = "MULTILINESTRING" + coords;
                String roadNm = getRoadName(feature);

                Map<String, Object> featureMap = new HashMap<>();
                featureMap.put("ID", id);
                featureMap.put("TYPE", type);
                featureMap.put("COORDINATES", coordinates);
                featureMap.put("ROAD_NM", roadNm);

                adrsList.add(featureMap);
            }

            int insertCount = dao.insertList(adrsList);
            System.out.println("INSERT COUNT: " + insertCount);

        }
    }

    public static String getId(JSONObject _f) {
        return (String) _f.get("id");
    }

    public static String getGeoType(JSONObject _f) {
        JSONObject GEOMETRY = (JSONObject) _f.get("geometry");
        return (String) GEOMETRY.get("type");
    }

    public static String getCoordinates(JSONObject _f) {
        JSONObject GEOMETRY = (JSONObject) _f.get("geometry");
        JSONArray coords = (JSONArray) GEOMETRY.get("coordinates");
        return coords.toString();
    }

    public static String getRoadName(JSONObject _f) {
        JSONObject PROPERTIES = (JSONObject) _f.get("properties");
        return (String) PROPERTIES.get("rn");
    }




}
