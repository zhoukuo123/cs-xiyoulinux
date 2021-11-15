package search;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.activity.entity.CsUserActivity;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qkm
 */
@SpringBootTest
public class DbToEs {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void dbToEs() throws IOException {
        RowMapper<CsUserActivity> mapper = new BeanPropertyRowMapper<>(CsUserActivity.class);
        List<CsUserActivity> csUserActivities = jdbcTemplate.query("select * from cs_user_activity", mapper);


        //整合所有的数据
        BulkRequest bulkRequest = new BulkRequest();

        for (CsUserActivity csUserActivity : csUserActivities) {
            System.out.println(JSON.toJSONString(csUserActivity));
            IndexRequest indexRequest = new IndexRequest("activity").id(csUserActivity.getId())
                    .source(JSON.toJSONString(csUserActivity), XContentType.JSON);

            bulkRequest.add(indexRequest);
        }
        //运行所有操作，没有事务性
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.status());
    }

    @Test
    public void addIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("activity");
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    @Test
    public void matchAll() throws IOException {
        SearchRequest request = new SearchRequest("activity");
        request.source().query(QueryBuilders.matchAllQuery());
        System.out.println(extracted(request));
    }

    private List<CsUserActivity> extracted(SearchRequest searchRequest) throws IOException {
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);

        //将从es中查询的结果装入list
        ArrayList<CsUserActivity> activityList = new ArrayList<>();

        for (SearchHit documentFields : search.getHits().getHits()) {
            //将结果转成jsonString
            String sourceAsString = documentFields.getSourceAsString();
            activityList.add(JSON.parseObject(sourceAsString, CsUserActivity.class));
        }
        return activityList;
    }
}
