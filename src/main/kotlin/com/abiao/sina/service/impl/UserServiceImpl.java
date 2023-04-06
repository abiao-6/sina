package com.abiao.sina.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.abiao.sina.common.utils.DateFormatUtils;
import com.abiao.sina.common.utils.HtmlUtils;
import com.abiao.sina.dao.MysqlUserMapper;
import com.abiao.sina.entity.*;
import com.abiao.sina.entity.Bot.InformationTemplate;
import com.abiao.sina.feign.entity.RealTimeKeyword;
import com.abiao.sina.forest.BotClient;
import com.abiao.sina.forest.BotSinaClient;
import com.abiao.sina.service.MonitorContentService;
import com.abiao.sina.service.ProxyService;
import com.abiao.sina.service.UserService;
import com.abiao.web.infrastructure.common.IdGenerator;
import com.abiao.web.infrastructure.model.JsonResultMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String COOKIE = "_T_WM=4e661fef4c394d44b19b6cf2d7e45d00; __bid_n=184fcad6f42d5db25a4207; FEID=v10-264b156dcb8fba1e65afe72c66e9317ee15b71a5; __xaf_fpstarttimer__=1671518385236; __xaf_thstime__=1671518385255; FPTOKEN=FKITfFGBEyA9SJQs7ObBIljxMvMJvmzgOKKGqw45OatwohbCP/pJjnsWnU05nZAIyjnrJb62z2PxaEjDwUNouHyiakggezu9guURiKod80KHNA3u9XUxwh0dLRjvO9LZMD+DufeqglnRba33mWwSeWyXSKfG/9gnBQaKEG2ssJ4+C+AH8U0i3c5qb0gHVYTD0eEWs6zDsBduLR8ox87Q6l72rG88bjl9E6Lixwkhevom2oz7I/S8Ryj5aUkeJCNE80OpJIS1ljm5wZ3nRAnjFE/aYKARvpK0ORxybY3iPz8Q31n1d4gGNEgsAmnJJxadFFl2EBI9MEMywoHd7bsdHUY/bU78O50m56hh2RJfRZ3dzpC6VVKyeBHmd51xBCRRAPuI5cdsHB9H3GF2CdnUHA==|N0lM6zCd9czEqZ5jNUANwNIdytQAWzpj3/yYD6/8auo=|10|177d894af944d7826ff6441bf84ef6c6; __xaf_fptokentimer__=1671518385285; SUB=_2A25OrSzyDeRhGeFJ71EX9yrNzj6IHXVqUbS6rDV6PUJbkdANLUf9kW1Nf_1NJ37s8i1JSLKNF_aA7RrHH0VfpUh4; SCF=AqjRNogLdZWjgOcnMEyd5IWigWUqPB4D1jXXX7qS6K1evEUT8LrThdQXRdSj4wialygxrui5NmXyQ7-Xhi_L1LU.; SSOLoginState=1672043682";

    private final HashMap<String, String> HEADERS = getHeaders();

    // 穷尽一生的天堂
    private static final String USER_UID = "7743674152";

    private static final String REQUEST_USER_INFO = "https://weibo.cn/";

    private static final String YUN_BAO_CHANG_URL = "https://m.weibo.cn/api/container/getIndex?containerid=100103type%3D61%26q%3D" + "keyword" + "%26t%3D&page_type=searchall";

    private static final String MY_SERVER_KEY = "SCT106131TbSFsQmqJUBDxieVemMMsM98s";

    private static final String WDD_SERVER_KEY = "SCT172375T1QXvzzvKBXuS4SiieM5omhGG";

    private static final String REDIS_KEY_SINA = "sina";

    private static final String SUPER_USER = "wxid_bpnzbauuobh822";

    private static final String WDD_WxId = "wxid_9wz0jvnedms422";

    private static final String WDD_XH1_WxId = "wxid_iy5m5w9ffu1m22";

    private static final String WDD_XH2_WxId = "wxid_euv2xsil8b2512";
    private static final String WDD_DX_WxId = "wxid_dl0wkl61lmeu22";

    private static final String AB_WxId = "wxid_bpnzbauuobh822";

    volatile private static List<Proxy> threadUsedIp = new ArrayList<>();

    static ExecutorService cloudPackageStoreExecutor = Executors.newFixedThreadPool(5);

    @Resource
    private MysqlUserMapper mysqlUserMapper;

    @Resource
    private IdGenerator idGenerator;

    @Resource
    private ExecutorService executorService;

    @Resource
    private ProxyService proxyService;


    private final BotClient botClient = BotClient.Companion.invoke();

    private final BotSinaClient botSinaClient = BotSinaClient.Companion.invoke();

    @Autowired
    private MonitorContentService monitorContentService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<MysqlUser> get(String userUid) {
        if (StringUtils.isEmpty(userUid)) {
            userUid = USER_UID;
        }
        List<MysqlUser> mysqlUsers = mysqlUserMapper.queryFolloweds(userUid);
        if (CollectionUtils.isEmpty(mysqlUsers)) {
            return null;
        }
        return mysqlUsers;
    }

    private HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("cookie", COOKIE);
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        return headers;
    }

    @Override
    public void refresh() {
        String infoUrl = String.format("https://weibo.cn/%s/info", USER_UID);
        String body = HttpUtil.createGet(infoUrl).addHeaders(HEADERS).execute().body();
        List<String> userNameList = get("<title>(.*?)</title>", body);
        String userName = userNameList.get(0).replaceAll("的资料", "");
        List<MysqlUser> mysqlUserList = new ArrayList<>();
        Integer onePage = getOnePage(HEADERS, userName, 0, mysqlUserList);
        if (onePage == null) {
            return;
        }

        for (int i = 1; i <= onePage; i++) {
            getOnePage(HEADERS, userName, i, mysqlUserList);
        }
        List<MysqlUser> mysqlUsers = mysqlUserMapper.queryFolloweds(USER_UID);
        if (CollectionUtils.isEmpty(mysqlUsers)) {
            mysqlUserMapper.insert(mysqlUserList);
            return;
        }

        createThread(mysqlUsers);

        HashSet<String> followedSet = (HashSet<String>) mysqlUsers.stream().map(MysqlUser::getFollowedId).collect(Collectors.toSet());
        List<MysqlUser> addMysqlUserList = mysqlUserList.stream().filter(mysqlUser -> !followedSet.contains(mysqlUser.getFollowedId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(addMysqlUserList)) {
            return;
        }
        mysqlUserMapper.insert(addMysqlUserList);
    }

    private void createThread(List<MysqlUser> mysqlUsers) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fillFollowedName(mysqlUsers);
            }
        }).start();
    }

    private void fillFollowedName(List<MysqlUser> mysqlUsers) {
        List<MysqlUser> nullFollowedNames = mysqlUsers.stream().filter(mysqlUser -> StringUtils.isEmpty(mysqlUser.getFollowedName())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(nullFollowedNames)) {
            return;
        }
        for (MysqlUser mysqlUser : nullFollowedNames) {
            try {
                Thread.sleep(1);
                String userInfoUrl = String.format("https://weibo.cn/%s/info", mysqlUser.getFollowedId());
                String body = HttpUtil.createGet(userInfoUrl).addHeaders(HEADERS).execute().body();
                List<String> followedNames = ReUtil.findAll("昵称:[\\u4e00-\\u9fa5A-Z0-9a-z._-]+", body, 0);
                if (!CollectionUtils.isEmpty(followedNames)) {
                    String followedName = followedNames.get(0);
                    followedName = followedName.replace("昵称:", "");
                    mysqlUser.setFollowedName(followedName);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        mysqlUserMapper.updateFollowedName(nullFollowedNames);
    }

    @Override
    public MysqlUser getFollowed(MysqlUser user) {
        return mysqlUserMapper.getFollowedByNameOrId(user);
    }

    @Override
    public void replaceFollowed(MysqlUser mysqlUser) {
        List<String> followedIds = Arrays.asList(mysqlUser.getFollowedIds());
        mysqlUserMapper.initIsMonitored(USER_UID);
        mysqlUserMapper.updateIsSpider(followedIds);
    }

    private Integer getOnePage(HashMap<String, String> headers, String userName, int page, List<MysqlUser> mysqlUserList) {
        if (page == 0) {
            String followUrl = String.format("https://weibo.cn/%s/follow?page=%d", USER_UID, 1);
            String followBody = HttpUtil.createGet(followUrl).addHeaders(headers).execute().body();
            List<String> strings = get(" value=\"跳页\" />&nbsp;(.*)页</div></form>", followBody);
            if (CollectionUtils.isEmpty(strings)) {
                return 1;
            }
            String pageNumber = strings.get(0);
            System.out.println("pageNumber = " + pageNumber);
            String[] split = pageNumber.split("/");
            return Integer.parseInt(split[1]);
        }
        String followUrl = String.format("https://weibo.cn/%s/follow?page=%d", USER_UID, page);
        String followBody = HttpUtil.createGet(followUrl).addHeaders(headers).execute().body();
        List<String> followedIds = get("<td valign=\"top\"><a href=\"https://weibo.cn/u/(\\d+\">([\\u4e00-\\u9fa5A-Z0-9a-z._-]+))</a>", followBody);

        List<String> followedUids = get("uid=(\\d+)", followBody);


        for (String followedId : followedIds) {
            MysqlUser mysqlUser = convertToMysqlUser(followedId, userName);
            mysqlUserList.add(mysqlUser);
        }
        HashSet<String> followedSet = (HashSet<String>) mysqlUserList.stream().map(MysqlUser::getFollowedId).collect(Collectors.toSet());
        List<String> notNameFolloweds = followedUids.stream().distinct().filter(followedUid -> !followedSet.contains(followedUid)).collect(Collectors.toList());
        for (String notNameFollowed : notNameFolloweds) {
            MysqlUser mysqlUser = new MysqlUser();
            String id = idGenerator.newId();
            mysqlUser.setId(id);
            mysqlUser.setUserUid(USER_UID);
            mysqlUser.setFollowedId(notNameFollowed);
            mysqlUser.setCreator(userName);
            mysqlUserList.add(mysqlUser);
        }

        return null;
    }

    private List<String> get(String str, String body) {
        return ReUtil.findAll(str, body, 1);
    }

    private MysqlUser convertToMysqlUser(String followedId, String userName) {
        String[] followeds = followedId.split("\">");
        MysqlUser mysqlUser = new MysqlUser();
        String id = idGenerator.newId();
        mysqlUser.setId(id);
        mysqlUser.setUserUid(USER_UID);
        mysqlUser.setFollowedId(followeds[0]);
        mysqlUser.setFollowedName(followeds[1]);
        mysqlUser.setCreator(userName);
        return mysqlUser;
    }

    @Override
    public void execSpider() {
        List<MysqlUser> mysqlUsers = mysqlUserMapper.queryMonitored(USER_UID);
        List<MonitorContent> weiboContentList = new ArrayList<>();
        List<MonitorContent> monitorContents = monitorContentService.query();
        Map<String, List<MonitorContent>> userUidMap = monitorContents.stream().collect(Collectors.groupingBy(MonitorContent::getUserUid));

        CompletableFuture[] futures = new CompletableFuture[mysqlUsers.size()];
        for (int i = 0; i < mysqlUsers.size(); i++) {
            MysqlUser mysqlUser = mysqlUsers.get(i);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                getUserInfo(weiboContentList, mysqlUser, userUidMap);
            }, executorService);
            futures[i] = future;
        }

        threadUsedIp.clear();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("线程休眠两秒失败 ！！！");
        }
        CompletableFuture.allOf(futures).join();
        if (!CollectionUtils.isEmpty(weiboContentList)) {
            monitorContentService.insert(weiboContentList);
        }
    }

    @Override
    public void yunBaoChang() {

        JsonResultMessage<List<RealTimeKeyword>> query = botSinaClient.query(new RealTimeKeyword());
        List<RealTimeKeyword> realTimeKeywords = query.getData();
        for (RealTimeKeyword timeKeyword : realTimeKeywords) {
            CompletableFuture.runAsync(()->{
                executeCloudPackageStore(timeKeyword);
            },cloudPackageStoreExecutor);
        }
    }

    @Override
    public List<StringBuilder> search(RealTimeKeyword timeKeyword) {
        timeKeyword.setSearch(true);
        List<StringBuilder> contents = new ArrayList<>(10);
        List<CloudPackageStoreWeiBo> searchContentList = getSearchContent(timeKeyword);
        searchContentList.forEach(card -> {
            CloudPackageWeiBoContent mblog = card.getMblog();
            String text = mblog.getText();
            text = HtmlUtils.delHTMLTag(text);
            String url = card.getScheme();
            StringBuilder searchContent = new StringBuilder();
            searchContent.append(text);
            searchContent.append("\n 时间: ").append(mblog.getCreated_at());
            searchContent.append("\n url: ").append(url);
            contents.add(searchContent);
        });
        return contents;
    }

    private String getUserInfoAddIpSend(String url) {
        Proxy proxy = proxyService.get();
        return getResponseJson(url, proxy, 1000);
    }

    synchronized private static void saveIp(Proxy proxy) {
        if (threadUsedIp.contains(proxy)) {
            return;
        }
        threadUsedIp.add(proxy);
    }

    private String getResponseJson(String url, Proxy proxy, int timeout) {
        String ip = proxy.getIp();
        int post = proxy.getPost();
        String responseJson = null;
        try {
            responseJson = HttpUtil.createGet(url)
                    .setHttpProxy(ip, post)
                    .timeout(timeout)
                    .addHeaders(getHeaders())
                    .execute()
                    .body();
            if (responseJson.contains("status: 418")) {
                return "";
            }
            saveIp(proxy);
            return responseJson;
        } catch (Exception e) {
            return "";
        }
    }

    private List<CloudPackageStoreWeiBo> executeCloudPackageStore(RealTimeKeyword timeKeyword) {
        String keyWord = timeKeyword.getKeyword();
        List<CloudPackageStoreWeiBo> existCards = getSearchContent(timeKeyword);
        existCards.forEach(existCard -> {
            CloudPackageWeiBoContent mblog = existCard.getMblog();
            String text = mblog.getText();
            text = HtmlUtils.delHTMLTag(text);
            String url = existCard.getScheme();
            StringBuilder cloudPackageStoreContent = new StringBuilder();
            cloudPackageStoreContent.append(text);
            cloudPackageStoreContent.append(" url: ").append(url);
            sendFangTang(cloudPackageStoreContent, timeKeyword);
            cloudPackageStoreContent.append("\n" + "关键字：" + keyWord);
//            sendBotGroupMsg(cloudPackageStoreContent);
            sendBotMsg(cloudPackageStoreContent, timeKeyword);
        });
        return existCards;
    }

    private List<CloudPackageStoreWeiBo> getSearchContent(RealTimeKeyword timeKeyword){
        String keyWord = timeKeyword.getKeyword();
        long startTime = System.currentTimeMillis();
        String responseJson = addIpSend(keyWord);
        long endTime = System.currentTimeMillis();
        long time = (endTime - startTime) / 1000L + 10L;
        if (!JSONUtil.isTypeJSON(responseJson)) {    //如果响应格式不是json,说明响应是异常的,直接结束方法
            log.error("请求异常，response:"+responseJson);
            return new ArrayList<>(1);
        }
        JSONObject responseObj = JSONUtil.parseObj(responseJson);
        Object obj = responseObj.get("data");
        JSONObject cloudPackageStoreObj = JSONUtil.parseObj(obj);
        List<CloudPackageStoreWeiBo> cards = cloudPackageStoreObj.getBeanList("cards", CloudPackageStoreWeiBo.class);
        if (CollUtil.isEmpty(cards)) {
            return new ArrayList<>(1);
        }
        if (timeKeyword.isSearch()){
            return cards;
        }
        List<CloudPackageStoreWeiBo> existCards = cards.stream()
                .filter(card -> checkTime(card, time))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(existCards)) {
            return new ArrayList<>(1);
        }
        return existCards;
    }

    private String addIpSend(String keyWord) {
        String encode = URLUtil.encode(keyWord);
        String requestUrl = YUN_BAO_CHANG_URL.replace("keyword", encode);

        String responseJson = null;
        Proxy proxy = proxyService.get();
        String ip = proxy.getIp();
        int post = proxy.getPost();
        try {
            HttpResponse response = HttpUtil.createGet(requestUrl)
                    .setHttpProxy(ip, post)
                    .timeout(1000)
                    .addHeaders(HEADERS)
                    .execute();
            responseJson = response.body();
            if (!JSONUtil.isTypeJSON(responseJson)) {    //如果响应格式不是json,说明响应是异常的,直接结束方法
                log.error("请求异常，response.status :"+response.getStatus());
                return addIpSend(keyWord);
            }
            if (StringUtils.isEmpty(responseJson)) {
                return addIpSend(keyWord);
            }
            if (responseJson.contains("status: 418")) {
                return addIpSend(keyWord);
            }
//            out.println("responseJson = " + responseJson.substring(0,20));
            return responseJson;
        } catch (Exception e) {
            return addIpSend(keyWord);
        }
    }

    private boolean checkTime(CloudPackageStoreWeiBo card, long time) {
        Date weiboDate = card.getMblog().getCreated_at();
        Date date = new Date();
        long weiboTime = weiboDate.getTime();
        long nowTime = date.getTime();
        long diff = Math.abs(nowTime - weiboTime) / 1000;
        return diff <= time;
    }

    private void getUserInfo(List<MonitorContent> weiboContentList, MysqlUser user, Map<String, List<MonitorContent>> userUidMap) {

        String userUid = user.getFollowedId();
        String getNowWeiBoUrl = REQUEST_USER_INFO + userUid + "/profile";
        String followedName = user.getFollowedName();
        String userNowWeiBo = "";
        while (true){
            userNowWeiBo = getUserInfoAddIpSend(getNowWeiBoUrl);
            if (!StringUtils.isEmpty(userNowWeiBo)) {
                break;
            }
        }

        List<String> urlList = ReUtil.findAll("https://weibo.cn/comment/[a-zA-Z0-9]*", userNowWeiBo, 0);
        String url = "";
        if (!urlList.isEmpty()) {
            url = urlList.get(0);
        }
        String delHTMLContext = HtmlUtils.delHTMLTag(userNowWeiBo);
        if (StringUtils.isEmpty(delHTMLContext)) {
            return;
        }
        int index = delHTMLContext.indexOf("筛选") + 1;
        String weiBoText = delHTMLContext.substring(index + 1);
        String[] split = weiBoText.split("&nbsp");
        List<String> weiboList = Arrays.asList(split);
        List<MonitorContent> contents = userUidMap.get(userUid);
        Set<String> monitorContentSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(contents)) {
            monitorContentSet = contents.stream().map(MonitorContent::getWeiboContent).collect(Collectors.toSet());
        }

        Date latestTime = null;
        String monitorContent = "";
        int firstWeiBoIndex = 0;
        for (int i = 0; i < weiboList.size(); i++) {
            String weiBoContent = weiboList.get(i);
            if (";1分钟前".equals(weiBoContent)) {
                firstWeiBoIndex = i;
                monitorContent = weiboList.get(0);
                if (!monitorContentSet.contains(monitorContent)) {
                    latestTime = new Date();
//                    String hourMinute = DateFormatUtils.formatYearMonthDayHourMinuteSecondDate(latestTime);
//                    setRedisContent(userUid + hourMinute, "0");
                    saveWeiboContent(monitorContent, userUid, weiboContentList);
                }
            }
        }
        StringBuilder firstWeiBoContent = new StringBuilder();
        if (firstWeiBoIndex == 0) {
            return;
        }
        firstWeiBoContent.append(weiboList.get(0));
        firstWeiBoContent.append("url: ").append(url);

        if (latestTime == null) {
            return;
        }
        String hourMinute = DateFormatUtils.formatYearMonthDayHourMinuteSecondDate(latestTime);
//        boolean result = getRedisContent(userUid + hourMinute);
//        if (result) {
            sendNotification(firstWeiBoContent, followedName, userUid + hourMinute);
//        }
    }

    private void setRedisContent(String sinakey, String value) {
//        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//        opsForValue.set(userUid,"0");
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(REDIS_KEY_SINA, sinakey, value);
//        opsForHash.getOperations().expire(userUid,61, TimeUnit.SECONDS);
    }

    private boolean getRedisContent(String sinaKey) {
//        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//        String content = opsForValue.get(sinaKey);
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        Object content = opsForHash.get(REDIS_KEY_SINA, sinaKey);
        return "0".equals(content);
    }

    private synchronized void saveWeiboContent(String weiBoContent, String userUid, List<MonitorContent> weiboContentList) {
        MonitorContent monitorContent = new MonitorContent();
        monitorContent.setId(idGenerator.newId());
        monitorContent.setUserUid(userUid);
        monitorContent.setWeiboContent(weiBoContent);
        monitorContent.setWeiboDate(new Date());
        weiboContentList.add(monitorContent);
    }

    private void sendNotification(StringBuilder firstWeiBoContent, String followedName, String sinaKey) {
        RealTimeKeyword realTimeKeyword = new RealTimeKeyword();
        realTimeKeyword.setKeyword(followedName);
        sendFangTang(firstWeiBoContent, realTimeKeyword);
        sendBotMsg(firstWeiBoContent, null);
//        setRedisContent(sinaKey, "1");
    }

    public void sendBotMsg(StringBuilder firstWeiBoContent, RealTimeKeyword timeKeyword) {
        InformationTemplate informationTemplate = new InformationTemplate();
        informationTemplate.setMsg(firstWeiBoContent.toString());

        if (timeKeyword != null) {
            String userUid = timeKeyword.getUserUid();
            if (userUid.equals(SUPER_USER)) {
                informationTemplate.setToUserUid(AB_WxId);
                botClient.sendMsg(informationTemplate);
                return;
            }
        }

        informationTemplate.setToUserUid(WDD_WxId);
        botClient.sendMsg(informationTemplate);
        informationTemplate.setToUserUid(AB_WxId);
        botClient.sendMsg(informationTemplate);
        informationTemplate.setToUserUid(WDD_XH1_WxId);
        botClient.sendMsg(informationTemplate);
        informationTemplate.setToUserUid(WDD_XH2_WxId);
        botClient.sendMsg(informationTemplate);
        informationTemplate.setToUserUid(WDD_DX_WxId);
        botClient.sendMsg(informationTemplate);
    }

    private void sendFangTang(StringBuilder firstWeiBoContent, RealTimeKeyword timeKeyword) {
        String userUid = timeKeyword.getUserUid();

        String keyword = timeKeyword.getKeyword();
        String my_server_url = "https://sc.ftqq.com/" + MY_SERVER_KEY + ".send";
        String wdd_server_url = "https://sc.ftqq.com/" + WDD_SERVER_KEY + ".send";
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", keyword);
        map.put("desp", firstWeiBoContent);
        map.put("channel", 9);

        if (!StringUtils.isEmpty(userUid) && userUid.equals(SUPER_USER)) {
            HttpRequest.post(my_server_url).form(map).execute();
            return;
        }

        HttpRequest.post(my_server_url).form(map).execute();
        HttpRequest.post(wdd_server_url).form(map).execute();
    }

}
