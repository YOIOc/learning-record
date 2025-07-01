package com.itheima.mapper;

import com.github.pagehelper.Page;
import com.itheima.pojo.Article;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticleMapper {
    // 新增文章
    @Insert("insert into article(title, content, cover_img, state, category_id, create_user, create_time, update_time)" +
            "values(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},now(),now())")
    void add(Article article);

    // 根据Id查询
    @Select("select * from article where id=#{id}")
    Article detail(Integer id);

    // 更新
    @Update("update article set title=#{title},content=#{content},cover_img=#{coverImg},state=#{state}," +
                               "category_id=#{categoryId},create_user=#{createUser},create_time=now(), update_time=now()" +
            "where id = #{id}")
    void update(Article article);

    // 删除
    @Delete("delete from article where id=#{id}")
    void delete(Integer id);

    // 分页查询
    Page<Article> list(Integer categoryId, String state, Integer userId);
}
