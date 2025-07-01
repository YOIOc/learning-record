# sql语句练习50题

## 基础查询

### 1、查询所有学生的课程及分数情况

```sql
select
	st.s_id,
	st.s_name,
	co.c_name,
	sc.s_score
from
	student st
	inner join
	score sc on st.s_id = sc.s_id
	inner join
	course co on sc.c_id = co.c_id;
```

### 2、查询任何一门课程成绩在70分以上的姓名、课程名称和分数

```sql
select
	st.s_name,
	co.c_name,
	sc.s_score
from
	student st
	inner join
	score sc on st.s_id = sc.s_id and sc.s_score > 70
	inner join
	course co on sc.c_id = co.c_id;
```

### 3、查询不及格的课程

```sql
select
	st.*,
	co.c_name,
	sc.s_score
from
	student st
	inner join
	score sc on st.s_id = sc.s_id and sc.s_score < 60
	inner join
	course co on sc.c_id = co.c_id
```

### 4、查询课程编号为01且课程成绩在80分以上的学生的学号和姓名

```sql
select
	st.s_id,
	st.s_name
from
	student st
	left join
	score sc on st.s_id = sc.s_id and sc.c_id = '01' and sc.s_score > 80;
```

### 5、查询不同课程成绩相同的学生的学生编号、课程编号、学生成绩

```sql
select
	s1.s_id student1_id,
	s2.s_id student2_id,
	s1.c_id course1_id,
	s2.c_id course2_id,
	s1.s_score
from
	score s1
	inner join
	score s2 on s1.c_id != s2.c_id and s1.s_score = s2.s_score
```

## 1.自连接 - join

### 1、查询"01"课程比"02"课程成绩高的学生的信息及课程分数

- **学生表** 连接 **分数表** 两次，一次连接课程01的成绩数据，一次连接课程02的成绩数据，最后筛选出符合成绩要求的学生信息和分数

```sql
select
	stu.s_id,
	stu.s_name,
	sc1.s_score 01_score,
	sc2.s_score 02_score
from
	student stu
	inner join
	score sc1 on stu.s_id = sc1.s_id and sc1.c_id = '01'
	inner join
	score sc2 on stu.s_id = sc2.s_id and sc2.c_id = '02'
where
	sc1.s_score > sc2.s_score;
```

## 2.分组 - group by / having

### 1、查询平均成绩大于等于60分的每个同学的学生编号和学生姓名和平均成绩

- group by：将具有相同值的行分组，并对每组数据执行聚合操作（如count、sum、avg等），通常与聚合函数一起使用，**执行顺序在where之后**
- having：用于在group by分组之后对分组数据进行过滤，与where类似，但是针对聚合函数的结果进行过滤，而where只针对原始数据进行过滤，**执行顺序在group by之后**

```sql
select
	st.s_id,
	st.s_name,
	avg(sc.s_score) avg_score
from
	student st
	inner join
	score sc on st.s_id = sc.s_id
group by
	st.s_id, st.s_name
having
	avg(sc.s_score) >= 60;
```

### 2、查询平均成绩小于60分的同学的学生编号和学生姓名和平均成绩 (包括有成绩和无成绩)

- having条件 `avg(sco.s_score) < 60` 不会匹配平均成绩为null的数据，所以需要添加额外条件

```sql
select
	st.s_id,
	st.s_name,
	avg(sc.s_score) avg_score
from
	student st
	left join
	score sc on st.s_id = sc.s_id
group by
	st.s_id, st.s_name
having
	avg(sc.s_score) < 60 or avg_score is null;
```

### 3、查询所有同学的学生编号、学生姓名、选课总数、所有课程的总成绩

- （**严格模式下**）注意在mysql中使用group by后，select后出现的的字段要么是**分组字段**要么是**聚合函数**不然会报错

```sql
select
	st.*,
	count(sc.c_id) sum_course,
	sum(sc.s_score) sum_score
from
	student st
	left join
	score sc on st.s_id = sc.s_id
group by
	st.s_id, st.s_name;
```

### 4、查询两门及其以上不及格课程的同学的学号，姓名及其平均成绩

```sql
select
	st.*,
	avg(sc.s_score) avg_score
from
	student st
	left join
	score sc on st.s_id = sc.s_id
where
	s_score < 60
group by
	s_id
having
	count(c_id) >= 2;
```

### 5、查询每门课程被选修的学生数

```sql
select
	c_id,
	count(s_id) sum_student
from
	score
group by
	c_id;
```

### 6、查询出只有两门课程的全部学生的学号和姓名

```sql
select
	st.*
from
	student st
	left join
	score sc on st.s_id = sc.s_id
group by
	s_id
having
	count(c_id) = 2;
```

### 7、查询男生、女生人数

```sql
select
	s_sex,
	count(s_sex) '人数'
from
	student
group by
	s_sex
```

### 8、查询同名同性学生名单，并统计同名人数

```sql
select
	s_name,
	count(s_name)-1 '同名人数'
from
	student
group by
	s_name
having
	count(s_name) >= 2;
```

### 9、查询每门课程的平均成绩，结果按平均成绩降序排列，平均成绩相同时，按课程编号升序排列

```sql
select
	c_id,
	avg(s_score) avg_score
from
	score
group by
	c_id
order by
	avg_score desc, c_id;
```

### 10、查询平均成绩大于等于85的所有学生的学号、姓名和平均成绩

```sql
select
	st.s_id,
	st.s_name,
	avg(sc.s_score) avg_score
from
	student st
	left join
	score sc on st.s_id = sc.s_id
group by
	st.s_id, st.s_name
having
	avg(sc.s_score) >= 85;
```

### 11、求每门课程的学生人数

```sql
select
	co.c_name '课程名',
	count(*) '学生人数'
from
	score sc
	left join
	course co on sc.c_id = co.c_id
group by
	co.c_name
```

### 12、检索至少选修两门课程的学生学号

```sql
select
	s_id
from
	score
group by
	s_id
having
	count(*) >= 2;
```

### 13、查询选修了全部课程的学生信息

```sql
select
	st.s_id,
	st.s_name
from
	student st
	inner join
	score sc on st.s_id = sc.s_id
group by
	st.s_id, st.s_name
having
	count(distinct sc.c_id) = (select count(*) from course)
```

## 3.模糊匹配 - like

### 1、查询"李"姓老师的数量

- 按照性能排序：**count(*) = count(1) > count(主键字段) > count(字段)**

```sql
select
	count(*)
from
	teacher
where
	t_name like 李%;
```

### 2、查询名字中含有"风"字的学生信息

```sql
select
	*
from
	student
where
	s_name like '%风%'
```

### 3、查询1990年出生的学生名单

```sql
select
	*
from
	student
where
	s_birth like '1990%';
```

## 4.匹配筛选 - in

### 1、查询没学过"张三"老师授课的同学的姓名

```sql
select
	st.s_name
from
	student st
	left join
	score sc on st.s_id = sc.s_id
	left join
	course co on sc.c_id = co.c_id
	left join
	teacher te on co.t_id = te.t_id
where
	te.t_name != '张三';
```

- 主查询中`not exists` 代替 `s_id not in`提高性能：`not exists`不比较值，只检查是否返回行，有值则返回false，否则返回true
- `select 1`：表示只检查是否返回行，不关心具体的列值（有数据结果集为‘1’，没有数据结果集为空）
- 子查询中`sc.s_id = st.s_id`：外层查询会逐行处理student表中的每个学生，并将当前学生的s_id传递给子查询，子查询会根据sc.s_id = st.s_id 筛选出该学生的成绩记录，并进一步检查这些成绩是否由“张三”老师讲授的课程产生

```sql
select
	s_name
from
	student
where
	not exists (select
                	1
                from
                	score sc
                	inner join
                	course co on sc.c_id = co.c_id
                	inner join
                	teacher te on co.t_id = te.t_id
                where
                	st.s_id = sc.s_id and te.t_name = '张三'
               );
```

### 2、查询学过编号为"01"并且也学过编号为"02"的课程的同学的信息

```sql
select
	*
from
	student
where
	s_id
in(
	select sc1.s_id from score sc1 join score sc2 on sc1.s_id = sc2.s_id where sc1.c_id = '01' and sc2.c_id = '02'
);
```

### 3、查询学过编号为"01"但是没有学过编号为"02"的课程的同学的信息(偏难)

```sql
select
	*
from
	student
where
	s_id in( select s_id from score where c_id = '01' )
	and s_id not in( select s_id from score where c_id = '02' );
```

### 4、查询没有学全所有课程的同学的信息

```sql
select
	*
from
	student
where
	s_id in( select s_id from score group by s_id having count(c_id) < (select count(*) from course) );
```

### 5、查询至少有一门课与学号为"01"的同学所学相同的同学的信息

```sql
select
	*
from
	student
where
	s_id in(select s_id from score where c_id in(select c_id from score where s_id = '01'));
```

## 5.将多个值连接成一个字符串 - group_concat

### 1、查询和"01"号的同学学习的课程完全相同的其他同学的信息

```sql
select
	*
from
	student
where
	s_id in(select 
             	s_id
            from
             	score
            group by
             	s_id
            having 
            	group_concat(c_id order by c_id separator ',') = (select
                        	group_concat(c_id order by c_id separator ',') c_id
                        from 
                        	score 
                        where
                        	s_id = '01'));
```

## 6.规则排序 - order by

### 1、查询"01"课程分数小于60，按分数降序排列的学生信息

```sql
select
	st.*
from
	student st
	left join
	score sc on st.s_id = sc.s_id
where
	c_id = '01' and s_score < 60
order by
	sc.s_score desc;
```

### 2、查询不同老师所教不同课程平均分从高到低显示

```sql
SELECT
	c.t_id,
	t.t_name,
	s.c_id,
	ROUND(AVG(s_score),2) avgsc
FROM
	teacher t,course c,score s
WHERE
	t.t_id=c.t_id AND c.c_id=s.c_id
GROUP BY
	s.c_id,c.t_id,t.t_name
ORDER BY
	avgsc DESC
```

### 3、查询选修"张三"老师所授课程的学生中，成绩最高的学生信息及其成绩

```sql
select
	st.s_id,
	st.s_name,
	sc.s_score '最高成绩'
from
	student st
	inner join
	score sc on st.s_id = sc.s_id
where
	c_id in (select c_id from course where t_id = (select t_id from teacher where t_name = '张三'))
order by
	sc.s_score desc
limit
	1;
```

### 4、统计每门课程的学生选修人数（超过5人的课程才统计）。要求输出课程号和选修人数，查询结果按人数降序排列，若人数相同，按课程号升序排列

```sql
select
	c_id '课程号',
	count(*) '选修人数'
from
	score
group by
	c_id
having
	count(*) > 5
order by
	选修人数 desc, c_id;
```

## 7.子查询

### 1、按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩

- 子查询：对于外层查询的每一行，子查询会使用该行的字段值作为条件，这意味着子查询的执行是动态的，**子查询的结果集必须有别名**
- 在group by / order by后面使用中文时，不用加单引号，否则不生效

```sql
select
	st.s_id,
	(select s_score from score where c_id = '01' and st.s_id = s_id) as '语文',
	(select s_score from score where c_id = '02' and st.s_id = s_id) as '数学',
	(select s_score from score where c_id = '03' and st.s_id = s_id) as '英语',
	(select avg(s_score) from score where st.s_id = s_id group by s_id) as '平均分'
from
	student st
order by
	平均分 desc
```

### 34、查询课程名称为"数学"，且分数低于60的学生姓名和分数

```sql
select
	st.s_name,
	sc.s_score
from
	student st
	left join
	score sc on st.s_id = sc.s_id
where
	sc.c_id in (select c_id from course where c_name = '数学') and sc.s_score < 60;
```

## 8.条件表达 - case when... then...else...

### 1、查询各科成绩最高分、最低分和平均分：以如下形式显示：课程ID，课程name，最高分，最低分，平均分，及格率，中等率，优良率，优秀率，及格为>=60，中等为：70-80，优良为：80-90，优秀为：>=90

```sql
select
	sc.c_id,
	co.c_name,
	max(sc.s_score) as '最高分',
	min(sc.s_score) as '最低分',
	avg(sc.s_score) as '平均分',
	100 * sum(case when s_score>=60 then 1 else 0 end)/sum(case when s_score then 1 else 0 end) as '及格率',
	100 * sum(case when s_score>=70 and s_score<80 then 1 else 0 end)/sum(case when s_score then 1 else 0 end) as '中等率',
	100 * sum(case when s_score>=80 and s_score<90 then 1 else 0 end)/sum(case when s_score then 1 else 0 end) as '优良率',
	100 * sum(case when s_score>=90 then 1 else 0 end)/sum(case when s_score then 1 else 0 end) as '优秀率'
from
	score sc
	left join
	course co on sc.c_id = co.c_id
group by
	sc.c_id;
```

### 23、统计各科成绩各分数段人数：课程编号,课程名称,[100-85],[85-70],[70-60],[0-60]及所占百分比

```sql
select
	sc.c_id,
	co.cname,
	sum(case when s_score >= 85 and s_score <= 100 then 1 else 0 end) "100-85",
	sum(case when s_score >= 85 and s_score <= 100 then 1 else 0 end)/count(*) "100-85百分比",
	sum(case when s_score >= 70 and s_score <= 84 then 1 else 0 end) "84-70",
	sum(case when s_score >= 70 and s_score <= 84 then 1 else 0 end)/count(*) "84-70百分比",
	sum(case when s_score >= 60 and s_score <= 69 then 1 else 0 end) "69-60",
	sum(case when s_score >= 60 and s_score <= 69 then 1 else 0 end)/count(*) "69-60百分比",
	sum(case when s_score >= 0 and s_score <= 59 then 1 else 0 end) "0-59",
	sum(case when s_score >= 0 and s_score <= 59 then 1 else 0 end)/count(*) "0-59百分比"
from
	score sc
	left join
	course co on sc.c_id = co.c_id
group by
	sc.c_id;
```

## 9.窗口函数

**窗口函数**：用于在查询结果集中执行计算函数，与聚合函数不同，窗口函数不会将多行数据合并为一行

**语法**：`<窗口函数>() over (partition by 列名 order by 列名)` (partition by：用于将结果集划分为多个分区，并在每个分区独立的应用窗口函数)

**常用的窗口函数**：

1. 行号
   - row_number()：为每行分配一个唯一行号，从1开始
   - rank()：为每行分配排名，排名相同占用相同名次，后续排名会跳过
2. 聚合函数
   - sum(字段名) / avg(字段名) / max(字段名) / min(字段名) / count(字段名)
3. 统计函数
   - cume_dist()：到当前行的累计分布值
   - percent_rank()：百分比排名
   - ratio_to_report()：当前行值占窗口中所有值总和的比例
4. 偏移函数
   - lag(字段名, 偏移量, 默认值) / lead(字段名, 偏移量, 默认值)：获取当前行之 前/后 的值
   - first_value(字段名) / last_value(字段名)：获取窗口中 第一/最后 行的值
   - nth_value(字段名, n)：获取窗口中第n行的值

### 1、按各科成绩进行排序，并显示排名

- rank：为排序好的数据，分配排名值（并不起到排序的作用）

```sql
select
	c_id,
	s_id,
	s_score,
	rank() over (partition by c_id order by s_score desc)
from
	score;
```

### 2、查询学生的总成绩并进行排名

```sql
select
	s_id,
	sum_score，
	rank() over (order by sum_score desc) rank
from
	(select s_id, sum(s_score) sum_score from score group by s_id ) aggregated_scores;
```

### 3、查询学生平均成绩及其名次

```sql
select
	s_id,
	avg(s_score) avg_score,
	rank() over (order by avg_score desc)
from
	score sc
group by
	s_id
```

### 4、查询每门功课成绩最好的前两名(偏难)

```sql
select
	c_id,
	s_id,
	s_score
from
	(select s_id, c_id, s_score, row_number() over (partition by c_id order by s_score desc) rank from score) ranked
where
	rank <= 2;
```

## 10.公用表达式CTE - with

- 适用情况：
  - 代码复用
  - 当需要对窗口函数的计算结果进行筛选时，需要先执行窗口函数再进行筛选，在这种情况下应该使用子查询或CTE，不能将窗口函数与筛选条件放在一层查询当中

### 1、查询所有课程的成绩第2名到第3名的学生信息及该课程成绩

```sql
with RankedScores as (
	select
		st.*
		sc.s_id,
		sc.c_id,
		sc.s_score,
		rank() over (partition by sc.c_id order by sc.s_score desc) rank
	from
		student st
		left join
		score sc on st.s_id = sc.s_id	
)
select
	*
from
	RankedScores
where
	rank between 2 and 3;
```

### 2、查询各科成绩前三名的记录

```sql
with RankedScoreS as(
	select
    	c_id,
    	s_id,
    	rank() over (partition by c_id order by s_score desc) rank
    from
    	score
)
select
	sc.c_id,
	st.s_name,
	sc.s_score
from
	student st
	left join
	RankedScoreS sc on st.s_id = sc.s_id
where
	sc.rank between 1 and 3;
```
