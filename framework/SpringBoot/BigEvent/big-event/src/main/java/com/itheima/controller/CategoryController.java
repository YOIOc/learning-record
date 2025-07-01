package com.itheima.controller;

import com.itheima.pojo.Category;
import com.itheima.pojo.Result;
import com.itheima.service.CategoryService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody @Validated(Category.Add.class) Category category){
        // 查询该文章分类是否存在
        if (categoryService.findByCategoryName(category.getCategoryName()) != null) {
            return Result.error("改文章分类已存在");
        }
        // 添加文章分类
        categoryService.add(category);
        return Result.success("'" + category.getCategoryName() + "'类添加成功");
    }

    @GetMapping
    public Result<List<Category>> list(){
        // 根据创建的userId查询
        return Result.success(categoryService.list());
    }

    @GetMapping("/detail")
    public Result<Category> detail(Integer id){
        // 根据文章分类Id查询
        return Result.success(categoryService.findById(id));
    }

    @PutMapping
    public Result update(@RequestBody @Validated(Category.Update.class) Category category){
        if (categoryService.findById(category.getId()) == null) {
            return Result.error("该文章分类不存在");
        }
        categoryService.update(category);
        return Result.success("修改成功");
    }

    @DeleteMapping
    public Result deleteById(Integer id){
        if (categoryService.findById(id) == null) {
            return Result.error("该文章分类不存在");
        }
        categoryService.deleteById(id);
        return Result.error("删除成功");
    }
}
