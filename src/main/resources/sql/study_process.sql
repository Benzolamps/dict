/* 学习进度查询SQL语句 */
select
  cast(d.mastered_count as signed) as masteredCount,
  cast(d.failed_count as signed) as failedCount,
  /* 计算出未学习的个数, cast强制转换 */
	cast(case d.is_word when 1 then w.wc else p.pc end - d.failed_count - d.mastered_count as signed) as unstudiedCount,
  /* 总数 */
  cast(case d.is_word when 1 then w.wc else p.pc end as signed) as wholeCount
from
  /* 查出单词的总数 */
  (select count(1) as wc from dict_word) as w,
  /* 查出短语的总数 */
  (select count(1) as pc from dict_phrase) as p,
  (select
      1 as is_word,
      /* 已掌握的单词数 */
      (select count(1) from dict_sw as sw where sw.student = :student_id) as mastered_count,
      /* 未掌握的单词数 */
      (select count(1) from dict_swf as swf where swf.student = :student_id) as failed_count
    union all
    select
      0 as is_word,
      /* 已掌握的短语数 */
      (select count(1) from dict_sp as sp where sp.student = :student_id) as mastered_count,
      /* 未掌握的短语数 */
      (select count(1) from dict_spf as spf where spf.student = :student_id) as failed_count
    union all
    select
      1 as is_word,
      /* 取全班已掌握的单词数平均值 */
      (select avg((select count(1) from dict_sw as sw where sw.student = s.id)) from dict_student as s
      where s.class = (select s1.class from dict_student as s1 where s1.id = :student_id)) as mastered_count,
      /* 取全班未掌握的单词数平均值 */
      (select avg((select count(1) from dict_swf as swf where swf.student = s.id)) from dict_student as s
      where s.class = (select s1.class from dict_student as s1 where s1.id = :student_id)) as failed_count
    union all
    select
      0 as is_word,
      /* 取全班已掌握的短语数平均值 */
      (select avg((select count(1) from dict_sp as sp where sp.student = s.id)) from dict_student as s
      where s.class = (select s1.class from dict_student as s1 where s1.id = :student_id)) as mastered_count,
      /* 取全班未掌握的短语数平均值 */
      (select avg((select count(1) from dict_spf as spf where spf.student = s.id)) from dict_student as s
      where s.class = (select s1.class from dict_student as s1 where s1.id = :student_id)) as failed_count) as d;