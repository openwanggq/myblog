package org.sang.reptile;

import org.apache.http.util.TextUtils;
import org.sang.bean.ArticleReptile;
import org.sang.mapper.ArticleReptileMapper;
import org.sang.service.ArticleReptileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.sql.Timestamp;
import java.util.Map;

// 自定义实现Pipeline接口
@Component
public class MysqlPipeline implements Pipeline {

    @Autowired
    private ArticleReptileService articleReptileService;


    public void process(ResultItems resultitems, Task task) {
        Map<String, Object> mapResults = resultitems.getAll();
        String title = (String) mapResults.get("Title");
        String content = (String) mapResults.get("Content");
//        System.out.println("MysqlPipeline: content" + mapResults.get("Content"));
        // 持久化
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            ArticleReptile articleReptile = new ArticleReptile();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            articleReptile.setTitle(title);
            articleReptile.setMdContent(content);
            articleReptile.setHtmlContent(content);
            articleReptile.setPublishDate(timestamp);
            articleReptile.setEditTime(timestamp);
            articleReptileService.addNewArticleReptile(articleReptile);
        }
    }
}
