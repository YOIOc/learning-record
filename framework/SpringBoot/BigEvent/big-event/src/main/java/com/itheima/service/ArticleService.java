package com.itheima.service;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;

public interface ArticleService {
    // 新增文章
    void add(Article article);

    // 根据Id查询
    Article detail(Integer id);

    // 更新
    void update(Article article);

    // 删除
    void delete(Integer id);

    // 分页查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);
}
