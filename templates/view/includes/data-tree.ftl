<#--noinspection JSUnusedGlobalSymbols-->
<#macro data_tree id setting value variable>
  !function () {
    var init = function (selector, node) {
      var tree = $.fn.zTree.init($(selector), ${setting}, node);
      tree.checkAllNodes(true);
      tree.checkAllNodes(false);
      return tree;
    };

    var $tree = $('#' + '${id}');

    ${variable} = init($tree, ${value});

    $tree.parent().find('.select-all').unbind().click(function () {
      ${variable}.checkAllNodes(true);
    });

    $tree.parent().find('.select-none').unbind().click(function () {
      ${variable}.checkAllNodes(false);
    });

    $tree.parent().find('.select-reverse').unbind().click(function () {
      var nodes = ${variable}.transformToArray(${variable}.getNodes());
      $.each(nodes, function (index, item) {
        if (!item.isParent) {
          if (item.checked) {
            ${variable}.checkNode(item, false, true);
          } else {
            ${variable}.checkNode(item, true, true);
          }
        }
      });
    });
  }();
</#macro>