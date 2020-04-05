package org.sang.service;

import org.apache.http.util.TextUtils;
import org.sang.bean.ArticleReptile;
import org.sang.mapper.ArticleReptileMapper;
import org.sang.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by sang on 2017/12/20.
 */
@Service
@Transactional
public class ArticleReptileService {

    @Autowired
    ArticleReptileMapper articleReptileMapper;

    public int addNewArticleReptile(ArticleReptile article) {
        //处理文章摘要
        if (!TextUtils.isEmpty(article.getHtmlContent())) {
            //直接截取
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        int i = articleReptileMapper.addNewArticleReptile(article);
        return i;
    }

    public List<ArticleReptile> getArticles(String keywords, int page, int count) {
        int start = (page - 1) * count;
        return articleReptileMapper.getArticleReptile(keywords, start, count);
    }

    public ArticleReptile getArticleById(int id) {
        ArticleReptile article = articleReptileMapper.getArticleById(id);
        articleReptileMapper.pvIncrement(id);
        return article;
    }


    public String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

}
