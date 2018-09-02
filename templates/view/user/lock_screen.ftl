<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=0.8"/>
<link rel='stylesheet prefetch' href='${base_url}/flickity/font/orbitron-bold.css'>
<link rel='stylesheet prefetch' href='${base_url}/flickity/font/Share-TechMono.css'>
<link rel='stylesheet prefetch' href='${base_url}/flickity/css/flickity.css'>
<link rel="stylesheet" href="${base_url}/flickity/css/style.css">
<div class="lock" style="-webkit-transform: scale(1.5);">
  <div class="screen">
    <div class="code">0000</div>
    <div class="status">LOCKED</div>
    <div class="scanlines"></div>
  </div>
  <div class="rows">
    <#list 0..3 as row>
      <div class="row">
        <#list 0..9 as cell>
          <div class="cell">
            <div class="text">${cell}</div>
          </div>
        </#list>
      </div>
    </#list>
  </div>
</div>
<script src='${base_url}/flickity/js/flickity.pkgd.js'></script>
<script src='${base_url}/flickity/js/howler.js'></script>
<script src="${base_url}/res/js/lock-screen.js"></script>
<script src="${base_url}/res/js/dynamic-form.js"></script>
<script id="enter-password" type="text/html">
  <div>
    <div class="layui-card">
      <div class="layui-card-header">设置解锁密码</div>
      <div class="layui-card-body">
        <form class="layui-form" id="lock" method="post">
          <table class="layui-table">
            <tr>
              <td>
                请设置一个四位数解锁密码
              </td>
              <td>
                <input type="password" name="password" required autocomplete="off" maxlength="4" placeholder="请设置一个四位数解锁密码" class="layui-input">
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
  </div>
</script>

<script>
  Lock.checkPassword = function (p) {
    var password = localStorage.getItem('password');
    return null === password || dict.encrypt(p) == password;
  };

  Lock.unlock = function () {
    $('.lock').css('pointer-events', 'none');
    localStorage.removeItem('password');
    setTimeout(function () {
      parent.layer.close(parent.layer.getFrameIndex(window.name));
      parent.$('*').css('pointer-events', 'all');
    }, 5000);
  };


  if (!localStorage.getItem('password')) {
    var index = parent.layer.open({
      type: 1,
      content: $('#enter-password').html(),
      area: ['500px', '500px'],
      cancel: function (index) {
        parent.layer.closeAll();
      },
    });

    parent.dict.validateForm(parent.$('#lock'), {
        password: {
            required: true,
            pattern: /^[0-9]{4}$/
        }
    }, {
        password: '请设置一个四位数解锁密码'
    }, function () {
      localStorage.setItem('password', dict.encrypt(parent.$('#lock [name=password]').val()));
      parent.layer.close(index);
      return false;
    });
  };
</script>