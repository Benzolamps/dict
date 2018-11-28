<#-- @ftlvariable name="docSolutions" type="java.util.List<com.benzolamps.dict.bean.DocSolution>" -->
<#-- @ftlvariable name="shuffleSolutions" type="java.util.List<com.benzolamps.dict.bean.ShuffleSolution>" -->

<div class="layui-card">
  <div class="layui-card-body">
    <form class="layui-form" id="phrase-export">
      <table class="layui-table"></table>
      <input type="submit" value="确定" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>
</div>

<script>
  var docSolutions = (<@json_dump obj=docSolutions/>).filter(function (docSolution) {
    return docSolution.exportFor.toLowerCase() == 'phrase';
  });
  var shuffleSolutions = <@json_dump obj=shuffleSolutions/>;
  var $form = $('#phrase-export');
  var $table = $('#phrase-export table');
  var $tbody = $(document.createElement('tbody'));
  $table.append($tbody);
  var $tbody1 = $(document.createElement('tbody'));
  $table.append($tbody1);
  dict.dynamicForm($tbody, [
    {
      name: 'title',
      type: 'string',
      display: '标题',
      description: '文档的标题',
      notEmpty: true,
      maxLength: 20
    },
    {
      name: 'compareStrategy',
      type: 'string',
      display: '排序',
      description: '文档的排序',
      options: [
        {id: 0, value: '索引 ↑'},
        {id: 1, value: '字典顺序 ↑'},
        {id: 2, value: '已掌握学生数 ↑'},
        {id: 3, value: '未掌握学生数 ↑'},
        {id: 4, value: '词频 ↑'},
        {id: 5, value: '索引 ↓'},
        {id: 6, value: '字典顺序 ↓'},
        {id: 7, value: '已掌握学生数 ↓'},
        {id: 8, value: '未掌握学生数 ↓'},
        {id: 9, value: '词频 ↓'}
      ]
    },
    {
      name: 'docSolution',
      type: 'string',
      display: '模板',
      description: '文档的模板',
      notEmpty: true,
      options: docSolutions.map(function (docSolution) {
        return {id: docSolution.id, value: docSolution.name};
      })
    },
    {
      name: 'shuffleSolution',
      type: 'string',
      display: '乱序方案',
      description: '文档的乱序方案',
      notEmpty: true,
      options: shuffleSolutions.map(function (shuffleSolution) {
        return {id: shuffleSolution.id, value: shuffleSolution.name};
      })
    }
  ], '', {
    docSolution: docSolutions[0].id,
    shuffleSolution: shuffleSolutions[0].id
  }, [], [], function () {
    parent.exportData = dict.generateObject(dict.serializeObject($form));
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
    return false;
  });

  var toggleShuffleSolution = function (index) {
    var docSolution = docSolutions.filter(function (item) {
      return item.id == index;
    })[0];
    var tr = $tbody.find('tr').last();
    if (!docSolution || !docSolution.needShuffle) {
      tr.hide();
    } else {
      tr.show();
    }
  };

  toggleShuffleSolution(docSolutions[0].id);

  form.on('select(docSolution)', function (data) {
    toggleShuffleSolution(data.value);
  });
</script>
