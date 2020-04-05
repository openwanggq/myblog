package org.sang.controller;

import org.sang.bean.Article;
import org.sang.bean.ArticleReptile;
import org.sang.service.ArticleReptileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sang on 2017/12/20.
 */
@RestController
@RequestMapping("/article")
public class ArticleReptileController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    ArticleReptileService articleService;

    @RequestMapping(value = "/reptiles", method = RequestMethod.GET)
    public Map<String, Object> addNewArticle(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "count", defaultValue = "6") Integer count, String keywords) {
        List<ArticleReptile> articles = articleService.getArticles(keywords, page, count);
        Map<String, Object> map = new HashMap<>();
//        map.put("totalCount", totalCount);
        map.put("articles", articles);
        return map;
    }

    @RequestMapping(value = "/reptiles/{aid}", method = RequestMethod.GET)
    public ArticleReptile getArticleById(@PathVariable int aid) {
        return articleService.getArticleById(aid);
    }


}
