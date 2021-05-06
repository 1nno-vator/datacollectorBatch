import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainDAO {

    private static final String NAMESPACE = "batch.mappers.mapper.";

    private SqlSessionFactory sqlSessionFactory = null;

    public MainDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<Map<String, Object>> getBjd() {
        List<Map<String, Object>> test = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // do something ...
            String queryId = "getBjd";
            test = session.selectList(NAMESPACE + queryId);
        } finally {
            // session.commit(); // insert or update ...
            session.close();
        }
        return test;
    }
}
