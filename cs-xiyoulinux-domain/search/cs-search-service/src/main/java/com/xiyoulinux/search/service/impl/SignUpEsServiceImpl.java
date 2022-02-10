package com.xiyoulinux.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.join.pojo.factory.InterviewStatusFactory;
import com.xiyoulinux.join.pojo.vo.InterviewStatusVO;
import com.xiyoulinux.joinadmin.pojo.JoinInfo;
import com.xiyoulinux.joinadmin.pojo.vo.IntervieweeInfoAndGradeVO;
import com.xiyoulinux.joinadmin.pojo.vo.SignUpRecordVO;
import com.xiyoulinux.pojo.PagedGridResult;
import com.xiyoulinux.search.service.SignUpEsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CoderZk
 */
@Slf4j
@DubboService
public class SignUpEsServiceImpl implements SignUpEsService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public PagedGridResult searchSignUpRecords(String keywords, String sort, Integer page, Integer pageSize) {

        SearchRequest searchRequest = new SearchRequest("cs-join-info");

        SortBuilder sortBuilder = null;

        if (sort.equals("sno")) {
            sortBuilder = new FieldSortBuilder("sno").order(SortOrder.ASC);
        } else if (sort.equals("name")) {
            sortBuilder = new FieldSortBuilder("name.keyword").order(SortOrder.ASC);
        } else if (sort.equals("className")) {
            sortBuilder = new FieldSortBuilder("className").order(SortOrder.ASC);
        } else if (sort.equals("mobile")) {
            sortBuilder = new FieldSortBuilder("mobile").order(SortOrder.ASC);
        }

        PageInfo pageInfo = new PageInfo(page, pageSize, keywords);
        SearchSourceBuilder searchSourceBuilder = getSearchSourceBuilderToSignUpRecords(pageInfo, sortBuilder);

        //增加查询条件
        searchRequest.source(searchSourceBuilder);

        return getResultFromSignUpRecords(searchRequest, pageInfo);
    }

    @Override
    public PagedGridResult searchFromInterviewResult(String keywords, Integer round, Integer status, Integer status2, Integer page, Integer pageSize) {
        SearchRequest searchRequest = new SearchRequest("cs-join-admin");

        PageInfo pageInfo = new PageInfo(page, pageSize, keywords);
        SearchSourceBuilder searchSourceBuilder = getSearchSourceBuilderToInterviewResult(pageInfo, round, status, status2);

        //增加查询条件
        searchRequest.source(searchSourceBuilder);

        return getResultFromInterviewResult(searchRequest, pageInfo);
    }

    private SearchSourceBuilder getSearchSourceBuilderToSignUpRecords(PageInfo pageInfo, SortBuilder sortBuilder) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 查询所有条目
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", pageInfo.getKey());
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from((pageInfo.getPage() - 1) * pageInfo.getSize());
        searchSourceBuilder.size(pageInfo.getSize());
        searchSourceBuilder.sort(sortBuilder);
        return searchSourceBuilder;
    }

    private SearchSourceBuilder getSearchSourceBuilderToInterviewResult(PageInfo pageInfo, Integer round, Integer status, Integer status2) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // DSL bool查询
        QueryBuilder queryBuilder = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("name", pageInfo.getKey()))
                .must(QueryBuilders.termQuery("round", round))
                .should(QueryBuilders.termQuery("status", status))
                .should(QueryBuilders.termQuery("status2", status2));
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from((pageInfo.getPage() - 1) * pageInfo.getSize());
        searchSourceBuilder.size(pageInfo.getSize());
        return searchSourceBuilder;
    }

    private PagedGridResult getResultFromSignUpRecords(SearchRequest searchRequest, PageInfo pageInfo) {
        SearchResponse search = null;
        try {
            search = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es search error: [{}]", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        // 将从 es 中查询的结果装入 list
        List<JoinInfo> joinInfos = new ArrayList<>();

        for (SearchHit documentFields : search.getHits().getHits()) {
            // 将结果转成 jsonString
            String sourceAsString = documentFields.getSourceAsString();
            joinInfos.add(JSON.parseObject(sourceAsString, JoinInfo.class));
        }

        List<SignUpRecordVO> signUpRecordVOList = new ArrayList<>();

        for (JoinInfo joinInfo : joinInfos) {
            SignUpRecordVO signUpRecordVO = new SignUpRecordVO();

            signUpRecordVO.setSno(joinInfo.getSno());
            signUpRecordVO.setName(joinInfo.getName());
            signUpRecordVO.setClassName(joinInfo.getClassName());
            signUpRecordVO.setMobile(joinInfo.getMobile());

            Integer round = joinInfo.getRound();
            Integer status = joinInfo.getStatus();
            InterviewStatusVO interviewStatusVO = InterviewStatusVO.builder().round(round).status(status).build();

            com.xiyoulinux.join.pojo.factory.InterviewStatus interviewStatus = InterviewStatusFactory.getInterviewStatus(interviewStatusVO);

            signUpRecordVO.setInterviewStatus(interviewStatus.getInterviewStatus());

            signUpRecordVOList.add(signUpRecordVO);
        }

        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(signUpRecordVOList);
        gridResult.setPage(pageInfo.getPage());
        gridResult.setTotal((int) (search.getHits().getTotalHits().value / pageInfo.getSize()));
        gridResult.setRecords(search.getHits().getTotalHits().value);

        return gridResult;
    }

    private PagedGridResult getResultFromInterviewResult(SearchRequest searchRequest, PageInfo pageInfo) {
        SearchResponse search = null;
        try {
            search = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es search error: [{}]", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        // 将从 es 中查询的结果装入 list
        List<IntervieweeInfoAndGradeVO> intervieweeInfoAndGradeVOList = new ArrayList<>();

        for (SearchHit documentFields : search.getHits().getHits()) {
            // 将结果转成 jsonString
            String sourceAsString = documentFields.getSourceAsString();
            intervieweeInfoAndGradeVOList.add(JSON.parseObject(sourceAsString, IntervieweeInfoAndGradeVO.class));
        }

        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(intervieweeInfoAndGradeVOList);
        gridResult.setPage(pageInfo.getPage());
        gridResult.setTotal((int) (search.getHits().getTotalHits().value / pageInfo.getSize()));
        gridResult.setRecords(search.getHits().getTotalHits().value);

        return gridResult;
    }

}
