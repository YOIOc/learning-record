package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.ArticleMapper;
import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.service.ArticleService;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        Map<String, Object> userInfo = (Map<String, Object>) ThreadLocalUtil.get();
        Integer id = (Integer) userInfo.get("id");
        article.setCreateUser(id);
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        // 1.创建PageBean对象(封装分页数据)
        PageBean<Article> pb = new PageBean<>();

        // 2.开启分页查询 - PageHelper(使用需要导入依赖)
        PageHelper.startPage(pageNum, pageSize);

        // 3.调用Mapper(Page继承于List，并提供了针对分页查询功能的相关方法)
        Map<String, Object> userInfo = (Map<String, Object>) ThreadLocalUtil.get();
        Integer userId = (Integer) userInfo.get("id");
        Page<Article> p = articleMapper.list(categoryId, state, userId);

        // 4.把数据封装进PageBean中
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public Article detail(Integer id) {
        return articleMapper.detail(id);
    }

    @Override
    public void update(Article article) {
        Map<String, Object> userInfo = (Map<String, Object>) ThreadLocalUtil.get();
        Integer id = (Integer) userInfo.get("id");
        article.setCreateUser(id);
        articleMapper.update(article);
    }

    @Override
    public void delete(Integer id) {
        articleMapper.delete(id);
    }
}
