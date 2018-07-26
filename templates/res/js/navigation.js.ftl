/**
 * 导航栏选中变色
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-10 22:02:50
 */
$(function () {
    var columns = null;

    /* 请求栏目JSON */
    $.ajax({
        type: "get",
        url: '${base_url}/res/json/columns.json',
        cache: true,
        dataType: "json",
        success: function(data) {
            columns = data;
        },
        error: function(msg) {
            console.log(msg);
        }
    });

    /* 将当前页面地址与JSON中逐个比对 */
    var href = location.href, column = -1, child = -1, title = '${request.getParameter("title")!""}';
    for (var i = 0; i < columns.length; i++) {
        var children = columns[i].children;
        for (var j = 0; j < children.length; j++) {
            if (href.indexOf(children[j].href) > -1) {
                column = i;
                child = j;
                title || title.trim().length > 0 || (title = children[j].title);
                break;
            }
        }
    }

    /* 将找到的栏目设为选中 */
    if (column >= 0 && child >= 0) {
        var $li = $('ul.dict-nav-tree>li').eq(column)
        $li.addClass('layui-nav-itemed');
        var $dd = $li.find('dl.layui-nav-child>dd').eq(child);
        $dd.addClass('layui-this');
    }

    /* 修改标题 */
    $('.content-title').text(title);
    document.title = title + ' - ${system_title} - ${system_version}';
});