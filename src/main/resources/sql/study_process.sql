select
  cast(d.mastered_count as signed) as masteredCount,
  cast(d.failed_count as signed) as failedCount,
	cast(case is_word when 1 then w.wc else p.pc end - failed_count - mastered_count as signed) as unstudiedCount,
  cast(case is_word when 1 then w.wc else p.pc end as signed) as wholeCount
from
(
  select
	  1 as is_word,
	  (select count(1) from dict_sw as sw where sw.student = :student_id) as mastered_count,
	  (select count(1) from dict_swf as swf where swf.student = :student_id) as failed_count
	union all
  select
	  0 as is_word,
	  (select count(1) from dict_sp as sp where sp.student = :student_id) as mastered_count,
	  (select count(1) from dict_spf as spf where spf.student = :student_id) as failed_count
	union all
	select
    1 as is_word,
    (
      select avg((select count(1) from dict_sw as sw where sw.student = s.id)) from dict_student as s
      where s.class = (select s1.class from dict_student as s1 where s1.id = :student_id)
    ) as mastered_count,
    (
      select avg((select count(1) from dict_swf as swf where swf.student = s.id)) from dict_student as s
      where s.class = (select s1.class from dict_student as s1 where s1.id = :student_id)
    ) as failed_count
	union all
  select
    0 as is_word,
    (
      select avg((select count(1) from dict_sp as sp where sp.student = s.id)) from dict_student as s
      where s.class = (select s1.class from dict_student as s1 where s1.id = :student_id)
    ) as mastered_count,
    (
      select avg((select count(1) from dict_spf as spf where spf.student = s.id)) from dict_student as s
      where s.class = (select s1.class from dict_student as s1 where s1.id = :student_id)
    ) as failed_count
) as d,
(select count(1) as wc from dict_word) as w,
(select count(1) as pc from dict_phrase) as p;