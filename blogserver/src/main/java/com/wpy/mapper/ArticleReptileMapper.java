package com.wpy.mapper;

import com.wpy.bean.ArticleReptile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by sang on 2017/12/20.
 */
@Component
@Mapper
public interface ArticleReptileMapper {

    int addNewArticleReptile(ArticleReptile article);

    List<ArticleReptile> getArticleReptile(@Param("keywords") String keywords, @Param("start") int start, @Param("count") int count);

    ArticleReptile getArticleById(int aid);

    void pvIncrement(int aid);

    int deleteArticleById(int id);

}
