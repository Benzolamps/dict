<@nothing><script type="text/javascript"></@nothing>
  <#assign submit_handler>
    !function (result, status, request) {
      parent.location.reload(true);
    }(result, status, request);
  </#assign>

  <#assign error_handler>
    !function (result, status, request) {
      parent.layer.alert(result.message, {
        icon: 2,
        title: result.status
      });
    }(result, status, request);
  </#assign>
<@nothing></script></@nothing>

<@data_edit
id='shuffle_solution'
title='修改个人资料'
fields=get_dict_property('com.benzolamps.dict.bean.User')
values=current_user()
messages={
'nickname': {'pattern': '昵称中存在非法字符'}
}
submit_handler=submit_handler
error_handler=error_handler
/>
