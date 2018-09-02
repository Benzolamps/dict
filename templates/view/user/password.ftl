<div class="layui-card">
  <div class="layui-card-header">修改密码</div>
  <div class="layui-card-body">
    <form class="layui-form" id="password" method="post">
      <table class="layui-table">
        <tr>
          <td>
            请输入原密码
          </td>
          <td>
            <input type="password" name="oldPassword" required maxlength="15" autocomplete="off" placeholder="请输入原密码" class="layui-input">
          </td>
        </tr>
        <tr>
          <td>
            请输入新密码
          </td>
          <td>
            <input type="password" name="newPassword" required maxlength="15" autocomplete="off" placeholder="请输入新密码" class="layui-input">
          </td>
        </tr>
        <tr>
          <td>
            请再次输入新密码
          </td>
          <td>
            <input type="password" name="confirmPassword" required maxlength="15" autocomplete="off" placeholder="请再次输入新密码" class="layui-input">
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" value="确定" class="layui-btn layui-btn-normal">
          </td>
        </tr>
      </table>
    </form>
  </div>
</div>
<script type="text/javascript">
  dict.validateForm($('#password'), {
    oldPassword: {
      required: true,
      minlength: 6,
      maxlength: 15,
      pattern: /^[_0-9A-Za-z]+$/,
      remote: 'check_password.json'
    },
    newPassword: {
      required: true,
      minlength: 6,
      maxlength: 15,
      pattern: /^[_0-9A-Za-z]+$/
    },
    confirmPassword: {
      required: true,
      minlength: 6,
      maxlength: 15,
      pattern: /^[_0-9A-Za-z]+$/,
      equalTo: '#password [name=newPassword]',
      notEqualTo: '#password [name=oldPassword]'
    }
  }, {
    oldPassword: {
      required: '旧密码不能为空',
      minlength: '旧密码不能少于6个字符',
      maxlength: '旧密码不能多于15个字符',
      pattern: '旧密码中存在非法字符',
      remote: '旧密码输入错误'
    },
    newPassword: {
      required: '新密码不能为空',
      minlength: '新密码不能少于6个字符',
      maxlength: '新密码不能多于15个字符',
      pattern: '新密码中存在非法字符'
    },
    confirmPassword: {
      required: '新密码不能为空',
      minlength: '新密码不能少于6个字符',
      maxlength: '新密码不能多于15个字符',
      pattern: '新密码中存在非法字符',
      equalTo: '两次输入的密码不一致',
      notEqualTo: '新密码不能与旧密码相同'
    }
  }, function () {
    dict.loadText({
      url: 'save_password.json',
      data: dict.generateObject(dict.serializeObject($('#password'))),
      success: function (result, status, request) {
        parent.layer.alert('修改成功', {
          icon: 1,
          title: '注销成功',
          yes: function (index) {
            parent.location.reload(true);
          }
        });
      },
      error: function (result, status, request) {
        parent.layer.alert(result.message, {
          icon: 2,
          title: result.status,
          yes: function (index) {
            parent.layer.close(index);
            parent.layer.close(parent.layer.getFrameIndex(window.name));
          },
          cancel: function (index) {
            parent.layer.close(index);
            parent.layer.close(parent.layer.getFrameIndex(window.name));
          }
        });
      }
    });
    return false;
  });
</script>