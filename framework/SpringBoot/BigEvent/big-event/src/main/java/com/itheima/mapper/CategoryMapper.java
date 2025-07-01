package com.itheima.mapper;

import com.itheima.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 根据类名查询
    @Select("select * from category where category_name=#{categoryName}")
    Category findByCategoryName(String categoryName);

    // 添加文章分类
    @Insert("insert into category(category_name, category_alias, create_user, create_time, update_time) " +
            "values (#{categoryName}, #{categoryAlias},#{createUser},now(),now())")
    void add(Category category);

    // 查询所有
    @Select("select * from category where create_user=#{userId}")
    List<Category> list(Integer userId);

    // 根据Id查询
    @Select("select * from category where id=#{categoryId}")
    Category findById(Integer categoryId);

    // 修改分类信息
    @Update("update category set category_name=#{categoryName}, category_alias=#{categoryAlias}, update_time=now() where id=#{id}")
    void update(Category category);

    // 删除分类
    @Delete("delete from category where id=#{id}")
    void deleteById(Integer id);
}
