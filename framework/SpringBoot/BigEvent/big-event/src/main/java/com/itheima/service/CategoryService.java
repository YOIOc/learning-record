package com.itheima.service;

import com.itheima.pojo.Category;
import com.itheima.pojo.Result;

import java.util.List;

public interface CategoryService {
    // 根据类名查询
    Category findByCategoryName(String categoryName);

    // 新增分类
    void add(Category category);

    // 查询所有
    List<Category> list();

    // 根据id查询
    Category findById(Integer categoryId);

    // 修改分类信息
    void update(Category category);

    // 删除分类
    void deleteById(Integer id);
}
