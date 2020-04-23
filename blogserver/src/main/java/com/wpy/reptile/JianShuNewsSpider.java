package com.wpy.reptile;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

@Component
public class JianShuNewsSpider implements PageProcessor {


    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public Site getSite() {
        return site;
    }

    public void process(Page page) {
        // 文章页，匹配 https://voice.hupu.com/nba/七位数字.html
        if (page.getUrl().regex("https://www\\.jianshu\\.com/p/\\w+").match()) {
            page.putField("Title", page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[1]//section/h1/text()").toString());
            page.putField("Content",
                    page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[1]//section/article/tidyText()").toString());
//            page.putField("Content",
//                    page.getHtml().xpath("@/article/text()").all().toString());
        }
        // 列表页
        else {
            // 文章url
            List<String> selectable2 = page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[@id='list-container']/ul/li/div[1]/a/@href").all();
            page.addTargetRequests(selectable2);

            // 翻页url
            List<String> selectable_more = page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[@id='list-container']/a/@href").all();
            page.addTargetRequests(selectable_more);
        }
    }




}
