<#--
  -- 系统设置界面
  -- author: Benzolamps
  -- version: 2.1.1
  -- datetime: 2018-7-17 23:15:40
  -->

<div id="shuffle-solutions-container"></div>

<script type="text/javascript">
  dict.loadText({
    url: '${base_url}/shuffle_solution/list.html',
    success: function (data) {
      $('#shuffle-solutions-container').html(data);
    }
  });
</script>