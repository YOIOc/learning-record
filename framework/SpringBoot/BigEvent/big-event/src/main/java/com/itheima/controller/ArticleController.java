package com.itheima.controller;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.ArticleService;
import com.itheima.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody @Validated Article article){
        if (categoryService.findById(article.getCategoryId()) == null) {
            return Result.error("当前文章分类不存在");
        }
        articleService.add(article);
        return Result.success("添加成功");
    }

    @GetMapping
    public Result<PageBean<Article>> list(Integer pageNum, Integer pageSize,
                                 @RequestParam(required = false) Integer categoryId,
                                 @RequestParam(required = false) String state){
        PageBean<Article> pb = articleService.list(pageNum, pageSize, categoryId, state);
        return Result.success(pb);
    }

    @GetMapping("/detail")
    public Result<Article> detail(Integer id){
        Article article = articleService.detail(id);
        if (article == null) {
            return Result.error("该文章不存在");
        }
        return Result.success(article);
    }

    @PutMapping
    public Result update(@RequestBody @Validated Article article){
        if (articleService.detail(article.getId()) == null) {
            return Result.error("该文章不存在");
        } else if (categoryService.findById(article.getCategoryId()) == null) {
            return Result.error("当前文章分类不存在");
        }
        articleService.update(article);
        return Result.success("修改成功");
    }

    @DeleteMapping
    public Result delete(Integer id){
        if (articleService.detail(id) == null) {
            return Result.error("该文章不存在");
        }
        articleService.delete(id);
        return Result.success("文章删除成功");
    }
}
