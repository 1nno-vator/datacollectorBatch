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
        List<Map<String, Object>> bjdList = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // do something ...
            String queryId = "getBjd";
            bjdList = session.selectList(NAMESPACE + queryId);
        } finally {
            // session.commit(); // insert or update ...
            session.close();
        }
        return bjdList;
    }

    public void truncateTable(String _tableName) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String queryId = "truncateTable";
            session.update(NAMESPACE + queryId, _tableName);
        } finally {
            session.commit();
            session.close();
        }
    }

    public int insertList(List list) {
        SqlSession session = sqlSessionFactory.openSession();
        int insertCount = 0;
        try {
            // do something ...
            String queryId = "insertList";
            insertCount = session.insert(NAMESPACE + queryId, list);
        } finally {
            session.commit(); // insert or update ...
            session.close();
        }
        return insertCount;
    }
}
