package com.xiyoulinux.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiyoulinux.activity.entity.CsUserActivity;
import com.xiyoulinux.activity.vo.CsUserActivityVo;
import com.xiyoulinux.activity.vo.PageActivityInfo;
import com.xiyoulinux.common.CsUserInfo;
import com.xiyoulinux.common.PageInfo;
import com.xiyoulinux.search.service.ISearchService;
import com.xiyoulinux.search.service.inter.IntelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qkm
 */
@DubboService
@Slf4j
public class SearchFromEsImpl implements ISearchService {


    @Resource
    private RestHighLevelClient client;

    @Resource
    private IntelService intelService;

    @HystrixCommand(
            groupKey = "search",
            // 舱壁模式
            threadPoolKey = "search",
            // 后备模式
            fallbackMethod = "searchByKeyFallBack",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "2000")
            },
            // 舱壁模式
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "80")
            }

    )
    @Override
    public PageActivityInfo searchByKey(PageInfo pageInfo, String userId) {
        SearchRequest searchRequest = new SearchRequest("activity");
        SearchSourceBuilder searchSourceBuilder = getSearchSourceBuilder(pageInfo);

        //增加查询条件
        searchRequest.source(searchSourceBuilder);
        return getResult(searchRequest, pageInfo, userId);
    }

    public PageActivityInfo searchByKeyFallBack(PageInfo pageInfo, String userId, Throwable throwable) {
        log.error("userId [{}] search from es page [{}] into fallback : [{}]", userId, pageInfo.getPage(), throwable.getMessage());
        return null;
    }

    @HystrixCommand(
            groupKey = "search",
            // 舱壁模式
            threadPoolKey = "search",
            // 后备模式
            fallbackMethod = "searchByKeyOrderByCreateTimeFallback",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "2000")
            }

    )
    @Override
    public PageActivityInfo searchByKeyOrderByCreateTime(PageInfo pageInfo, String userId) {
        SearchRequest searchRequest = new SearchRequest("activity");
        SearchSourceBuilder searchSourceBuilder = getSearchSourceBuilder(pageInfo);

        //按时间排序--DESC
        searchSourceBuilder.sort("activityCreateTime", SortOrder.DESC);

        //增加查询条件
        searchRequest.source(searchSourceBuilder);
        return getResult(searchRequest, pageInfo, userId);
    }

    public PageActivityInfo searchByKeyOrderByCreateTimeFallback(PageInfo pageInfo, String userId, Throwable throwable) {
        log.error("userId [{}] search orderby time from es page [{}] into fallback : [{}]", userId, pageInfo.getPage()
                , throwable.getMessage());
        return null;
    }

    @HystrixCommand(
            groupKey = "search",
            // 舱壁模式
            threadPoolKey = "search",
            // 后备模式
            fallbackMethod = "searchBoxAutoCompletionFallback",
            // 断路器模式
            commandProperties = {
                    // 超时时间, 单位毫秒, 超时进 fallback
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1200")
            }

    )
    @Override
    public List<String> searchBoxAutoCompletion(String key) {
        SearchRequest request = new SearchRequest("activity");
        request.source().suggest(new SuggestBuilder().addSuggestion("mySuggestion",
                SuggestBuilders.completionSuggestion("activityTitle")
                        .prefix(key).skipDuplicates(true).size(20)));
        return getBoxResult(request);
    }

    public List<String> searchBoxAutoCompletionFallback(String key, Throwable throwable) {
        log.error("search box auto completion key [{}] into fallback : [{}]", key, throwable.getMessage());
        return null;
    }

    private SearchSourceBuilder getSearchSourceBuilder(PageInfo pageInfo) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询所有条目
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("search", pageInfo.getKey());
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from((pageInfo.getPage() - 1) * pageInfo.getSize());
        searchSourceBuilder.size(pageInfo.getSize());
        return searchSourceBuilder;
    }

    private PageActivityInfo getResult(SearchRequest searchRequest, PageInfo pageInfo, String userId) {
        SearchResponse search = null;
        try {
            search = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es search error: [{}]", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        //将从es中查询的结果装入list
        ArrayList<CsUserActivity> activityList = new ArrayList<>();

        for (SearchHit documentFields : search.getHits().getHits()) {
            //将结果转成jsonString
            String sourceAsString = documentFields.getSourceAsString();
            activityList.add(JSON.parseObject(sourceAsString, CsUserActivity.class));
        }

        PageActivityInfo pageActivityInfo = new PageActivityInfo();
        pageActivityInfo.setActivityInfos(getCsActivityVo(activityList, userId));
        pageActivityInfo.setHasMore((search.getHits().getTotalHits().value / pageInfo.getSize()) + 1
                > pageInfo.getPage());
        return pageActivityInfo;
    }

    private List<String> getBoxResult(SearchRequest searchRequest) {
        SearchResponse search = null;
        try {
            search = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es search box error: [{}]", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        ArrayList<String> searchBox = new ArrayList<>();
        Suggest suggest = search.getSuggest();
        CompletionSuggestion suggestion = suggest.getSuggestion("mySuggestion");
        suggestion.getOptions().forEach(option -> {
            searchBox.add(option.getText().toString());
        });
        return searchBox;
    }

    public List<CsUserActivityVo> getCsActivityVo(List<CsUserActivity> activityList, String userId) {
        if (null == activityList || activityList.size() == 0) {
            return null;
        }
        //用户id为了获取用户信息
        Set<String> idList = activityList.stream().map(CsUserActivity::getUserId).collect(Collectors.toSet());
        log.info("Get activity --- get userId [{}]", JSON.toJSONString(idList));

        //动态id为了获取评论信息
        List<String> activityIdList = activityList.stream().map(CsUserActivity::getId).collect(Collectors.toList());
        log.info("Get activity --- get activityId [{}]", JSON.toJSONString(activityIdList));


        //调用用户服务
        Map<String, CsUserInfo> userMap = intelService.interCallPeopleList(idList);
        log.info("Get activity --- get userInfo success");


        //调用评论服务//记得让前端搞一下降级的 -1L
        Map<String, Long> commentMap = intelService.interCallComment(activityIdList);
        log.info("Get activity --- get comment success");

        List<CsUserActivityVo> csActivityVos = new ArrayList<>();

        activityList.forEach(csActivity -> {
            CsUserActivityVo csActivityVo = new CsUserActivityVo();
            CsUserActivityVo.Activity simpleActivity = CsUserActivityVo.Activity.to(csActivity);
            csActivityVo.setCsUserActivity(simpleActivity);
            csActivityVo.setCommentNumber(commentMap.get(csActivity.getId()));
            csActivityVo.setCsUserInfo(userMap.get(csActivity.getUserId()));
            csActivityVo.setIsModify(userId.equals(csActivity.getUserId()));
            csActivityVos.add(csActivityVo);
        });

        return csActivityVos;

    }
}
