package com.itheima.service.impl;

import com.itheima.mapper.CategoryMapper;
import com.itheima.pojo.Category;
import com.itheima.service.CategoryService;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category findByCategoryName(String categoryName) {
        return categoryMapper.findByCategoryName(categoryName);
    }

    @Override
    public void add(Category category) {
        Map<String, Object> userInfo = (Map<String, Object>) ThreadLocalUtil.get();
        Integer userId = (Integer) userInfo.get("id");
        category.setCreateUser(userId);
        categoryMapper.add(category);
    }

    @Override
    public List<Category> list() {
        Map<String, Object> userInfo = (Map<String, Object>) ThreadLocalUtil.get();
        Integer userId = (Integer) userInfo.get("id");
        return categoryMapper.list(userId);
    }

    @Override
    public Category findById(Integer categoryId) {
        return categoryMapper.findById(categoryId);
    }

    @Override
    public void update(Category category) {
        categoryMapper.update(category);
    }

    @Override
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }
}
