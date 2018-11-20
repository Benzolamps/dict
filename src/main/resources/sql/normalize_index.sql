update
  dict_word as wu
set
  /* 行号+1并赋值 */
  wu.indexes = @_row_num := @_row_num + 1
where
  id in
  (
    select
      lq.id as id
    from
      /* 初始化行号 */
      (select @_row_num := 0 as row_num) as rq,
      (select w.id from dict_word as w where w.library = :library_id order by w.indexes asc) as lq
  );

update
  dict_phrase as pu
set
  pu.indexes = @_row_num := @_row_num + 1
where
  id in
  (
    select
      lq.id as id
    from
      (select @_row_num := 0 as row_num) as rq,
      (select w.id from dict_word as w where w.library = :library_id order by w.indexes asc) as lq
  );

