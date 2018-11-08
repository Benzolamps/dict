<#-- @ftlvariable name="student" type="com.benzolamps.dict.bean.Student" -->
<#-- @ftlvariable name="wordStudyLogs" type="java.util.List<com.benzolamps.dict.bean.StudyLog>" -->
<#-- @ftlvariable name="phraseStudyLogs" type="java.util.List<com.benzolamps.dict.bean.StudyLog>" -->
<#-- @ftlvariable name="wordStudyProcess" type="com.benzolamps.dict.bean.StudyProcess" -->
<#-- @ftlvariable name="phraseStudyProcess" type="com.benzolamps.dict.bean.StudyProcess" -->
<#-- @ftlvariable name="averageWordStudyProcess" type="com.benzolamps.dict.bean.StudyProcess" -->
<#-- @ftlvariable name="averagePhraseStudyProcess" type="com.benzolamps.dict.bean.StudyProcess" -->
<#assign title>学生详情</#assign>
<blockquote class="layui-elem-quote" style="margin-top: 10px; line-height: 20px">
  ${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
  <button class="layui-btn layui-btn-normal layui-btn-sm" onclick="history.back();">
    <i class="fa fa-arrow-circle-left" style="font-size: 20px;"></i> &nbsp; 返回
  </button>
  <button class="layui-btn layui-btn-warm layui-btn-sm" onclick="location.reload(true);">
    <i class="fa fa-refresh" style="font-size: 20px;"></i> &nbsp; 刷新
  </button>
  <button id="import-word-process" class="layui-btn layui-btn-primary layui-btn-sm">
    <i class="fa fa-download" style="font-size: 20px;"></i> &nbsp; 导入单词学习进度
  </button>
  <button id="import-phrase-process" class="layui-btn layui-btn-primary layui-btn-sm">
    <i class="fa fa-download" style="font-size: 20px;"></i> &nbsp; 导入短语学习进度
  </button>
</blockquote>
<div class="layui-row">
  <div class="layui-col-xs4" style="vertical-align: center; text-align: center">
    <br><br>
    &nbsp; &nbsp; &nbsp; &nbsp;
    <button id="mastered-words" class="layui-btn layui-btn-primary layui-btn-sm">
      查看已掌握单词
    </button>
    <button id="mastered-phrases" class="layui-btn layui-btn-primary layui-btn-sm">
      查看已掌握短语
    </button>
    <br><br>
    &nbsp; &nbsp; &nbsp; &nbsp;
    <button id="failed-words"  class="layui-btn layui-btn-primary layui-btn-sm">
      查看未掌握单词
    </button>
    <button id="failed-phrases" class="layui-btn layui-btn-primary layui-btn-sm">
      查看未掌握短语
    </button>
    <br><br>
    &nbsp; &nbsp; &nbsp; &nbsp;
    <button id="personal-word-group" class="layui-btn layui-btn-primary layui-btn-sm">
      查看专属单词分组
    </button>
    <button id="personal-phrase-group" class="layui-btn layui-btn-primary layui-btn-sm">
      查看专属短语分组
    </button>
  </div>
  <div class="layui-col-xs4">
    <div class="layui-card">
      <div class="layui-card-body">
        <table class="layui-table">
          <colgroup>
            <col width="30">
            <col width="70">
          </colgroup>
          <thead></thead>
          <tbody>
            <tr>
              <th title="姓名">姓名</th>
              <td>${student.name}</td>
            </tr>
            <tr>
              <th title="学号">学号</th>
              <td>${student.number}</td>
            </tr>
            <tr>
              <th title="描述">描述</th>
              <td>${student.description!''}</td>
            </tr>
            <tr>
              <th title="班级">班级</th>
              <td>${student.clazz.name}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <div class="layui-col-xs4">
    <div class="layui-card">
      <div class="layui-card-body">
        <table class="layui-table">
          <thead></thead>
          <tbody>
            <tr>
              <th title="已掌握单词数">已掌握单词数</th>
              <td>${student.masteredWordsCount}</td>
            </tr>
            <tr>
              <th title="未掌握单词数">未掌握单词数</th>
              <td>${student.failedWordsCount}</td>
            </tr>
            <tr>
              <th title="已考核学生数">已掌握短语数</th>
              <td>${student.masteredPhrasesCount}</td>
            </tr>
            <tr>
              <th title="未掌握短语数">未掌握短语数</th>
              <td>${student.failedPhrasesCount}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
<div class="layui-row" style="margin-top: 20px">
  <div class="layui-col-xs4">
    <div class="layui-card" style="height: 450px;">
      <div class="layui-card-body">
        <div id="word-study-logs" style="width: 100%; height: 410px"></div>
      </div>
    </div>
  </div>
  <div class="layui-col-xs4">
    <div class="layui-card" style="height: 450px;">
      <div class="layui-card-body">
        <div id="process-pie" style="width: 100%; height: 410px"></div>
      </div>
    </div>
  </div>
  <div class="layui-col-xs4">
    <div class="layui-card" style="height: 450px;">
      <div class="layui-card-body">
        <div id="phrase-study-logs" style="width: 100%; height: 410px"></div>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript" src="${base_url}/js/echarts.min.js"></script>
<script>
  <#escape x as x?js_string>
    var wordStudyLogs = [
      <#list wordStudyLogs as log>
        {
          groupName: '${log.groupName}',
          logDate: '${log.logDate?string("M月d日 H:mm")}',
          groupCreateDate: '${log.groupCreateDate?string("M月d日 H:mm")}',
          wordsCount: '${log.wordsCount}',
          masteredWordsCount: '${log.masteredWordsCount}',
          masteredWordsPercent: '${(log.masteredWordsCount?double / log.wordsCount?double)?string.percent}',
          libraryName: '${log.libraryName}'
        },
      </#list>
    ];

    var phraseStudyLogs = [
      <#list phraseStudyLogs as log>
        {
          groupName: '${log.groupName}',
          logDate: '${log.logDate?string("M月d日 H:mm")}',
          groupCreateDate: '${log.groupCreateDate?string("M月d日 H:mm")}',
          phrasesCount: '${log.phrasesCount}',
          masteredPhrasesCount: '${log.masteredPhrasesCount}',
          masteredPhrasesPercent: '${(log.masteredPhrasesCount?double / log.phrasesCount?double)?string.percent}',
          libraryName: '${log.libraryName}'
        },
      </#list>
    ];

    echarts.init($('#word-study-logs')[0]).setOption({
      title: {
        text: '近10次单词考核掌握情况',
        x: 'center'
      },
      color: ['#3398DB'],
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
          axisTick: {
            alignWithLabel: true
          }
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        },
        formatter: function (params) {
          var log = wordStudyLogs[params[0].dataIndex];
          return '短语分组名称：' + log.groupName + '<br>' +
            '时间：' + log.logDate + '<br>' +
            '分组创建时间：' + log.groupCreateDate + '<br>' +
            '单词数：' + log.wordsCount + '<br>' +
            '掌握单词数：' + log.masteredWordsCount + '<br>' +
            '掌握单词占比：' + log.masteredWordsPercent + '<br>' +
            '词库名称：' + log.libraryName;
        }
      },
      series: [
        {
          name: '掌握单词数',
          type: 'bar',
          barWidth: '60%',
          data: wordStudyLogs.map(function (item) {
            return item.masteredWordsCount;
          })
        }
      ]
    });

    echarts.init($('#phrase-study-logs')[0]).setOption({
      title: {
        text: '近10次短语考核掌握情况',
        x: 'center'
      },
      color: ['#3398DB'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        },
        formatter: function (params) {
          var log = phraseStudyLogs[params[0].dataIndex];
          return '短语分组名称：' + log.groupName + '<br>' +
            '时间：' + log.logDate + '<br>' +
            '分组创建时间：' + log.groupCreateDate + '<br>' +
            '短语数：' + log.phrasesCount + '<br>' +
            '掌握短语数：' + log.masteredPhrasesCount + '<br>' +
            '掌握短语占比：' + log.masteredPhrasesPercent + '<br>' +
            '词库名称：' + log.libraryName;
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
          axisTick: {
            alignWithLabel: true
          }
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: [
        {
          name: '掌握短语数',
          type: 'bar',
          barWidth: '60%',
          data: phraseStudyLogs.map(function (item) {
            return item.masteredPhrasesCount;
          })
        }
      ]
    });

    echarts.init($('#process-pie')[0]).setOption({
      title: {
        text: '学习进度',
        subtext: '单词总数：${wordStudyProcess.wholeCount} \t\t 短语总数：${phraseStudyProcess.wholeCount}',
        x: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b}: {c} ({d}%)"
      },
      legend: {
        orient: 'vertical',
        x: 'left',
        data: ['已掌握', '未掌握', '未学习']
      },
      series: [
        {
          name: '${student.name}单词学习进度',
          type: 'pie',
          center: ['25%', '50%'],
          radius: ['40%', '50%'],
          avoidLabelOverlap: false,
          label: {
            normal: {
              show: false
            }
          },
          data:[
            {value: ${wordStudyProcess.masteredCount}, name: '已掌握'},
            {value: ${wordStudyProcess.failedCount}, name: '未掌握'},
            {value: ${wordStudyProcess.unstudiedCount}, name: '未学习'}
          ]
        },
        {
          name: '班级平均单词学习进度',
          type: 'pie',
          center: ['25%', '50%'],
          radius: ['0%', '30%'],
          avoidLabelOverlap: false,
          label: {
            normal: {
              show: false
            }
          },
          data:[
            {value: ${averageWordStudyProcess.masteredCount}, name: '已掌握'},
            {value: ${averageWordStudyProcess.failedCount}, name: '未掌握'},
            {value: ${averageWordStudyProcess.unstudiedCount}, name: '未学习'}
          ]
        },
        {
          name: '${student.name}短语学习进度',
          type: 'pie',
          center: ['75%', '50%'],
          radius: ['40%', '50%'],
          avoidLabelOverlap: false,
          label: {
            normal: {
              show: false
            }
          },
          data:[
            {value: ${phraseStudyProcess.masteredCount}, name: '已掌握'},
            {value: ${phraseStudyProcess.failedCount}, name: '未掌握'},
            {value: ${phraseStudyProcess.unstudiedCount}, name: '未学习'}
          ]
        },
        {
          name: '班级平均短语学习进度',
          type: 'pie',
          center: ['75%', '50%'],
          radius: ['0%', '30%'],
          avoidLabelOverlap: false,
          label: {
            normal: {
              show: false
            }
          },
          data:[
            {value: ${averagePhraseStudyProcess.masteredCount}, name: '已掌握'},
            {value: ${averagePhraseStudyProcess.failedCount}, name: '未掌握'},
            {value: ${averagePhraseStudyProcess.unstudiedCount}, name: '未学习'}
          ]
        }
      ]
    });

    $('#mastered-words').click(function () {
      var baseUrl = '${base_url}/word/list.html?' +
        <#--encodeURIComponent('searches.field[0]') + '=' + encodeURIComponent('studentId') + '&' +-->
        <#--encodeURIComponent('searches.value[0]') + '=' + encodeURIComponent('${student.id}') + '&' +-->
        <#--encodeURIComponent('searches.field[1]') + '=' + encodeURIComponent('studentName') + '&' +-->
        <#--encodeURIComponent('searches.value[1]') + '=' + encodeURIComponent('${student.name}') + '&' +-->
        <#--encodeURIComponent('searches.field[2]') + '=' + encodeURIComponent('mastered') + '&' +-->
        <#--encodeURIComponent('searches.value[2]') + '=true';-->
        encodeURIComponent(JSON.stringify({
          searches: [
            {field: 'studentId', value: '${student.id}' },
            {field: 'studentName', value: '${student.name}'},
            {field: 'isMastered', value: 'true'}
          ]
        }));
        console.log(baseUrl);
      location.href = baseUrl;
    });

    $('#failed-words').click(function () {
      var baseUrl = '${base_url}/word/list.html?' +
      encodeURIComponent(JSON.stringify({
        searches: [
          {field: 'studentId', value: '${student.id}' },
          {field: 'studentName', value: '${student.name}'},
          {field: 'isMastered', value: 'false'}
        ]
      }));
      console.log(baseUrl);
      location.href = baseUrl;
    });

    $('#personal-word-group').click(function () {
      var baseUrl = '${base_url}/word_group/list.html?' +
        encodeURIComponent(JSON.stringify({
          searches: [
            {field: 'studentId', value: '${student.id}' },
            {field: 'studentName', value: '${student.name}'}
          ]
        }));
      console.log(baseUrl);
      location.href = baseUrl;
    });

    $('#mastered-phrases').click(function () {
      var baseUrl = '${base_url}/phrase/list.html?' +
      encodeURIComponent(JSON.stringify({
        searches: [
          {field: 'studentId', value: '${student.id}' },
          {field: 'studentName', value: '${student.name}'},
          {field: 'isMastered', value: 'true'}
        ]
      }));
      console.log(baseUrl);
      location.href = baseUrl;
    });

    $('#failed-phrases').click(function () {
      var baseUrl = '${base_url}/phrase/list.html?' +
        encodeURIComponent(JSON.stringify({
          searches: [
            {field: 'studentId', value: '${student.id}' },
            {field: 'studentName', value: '${student.name}'},
            {field: 'isMastered', value: 'false'}
          ]
        }));
      console.log(baseUrl);
      location.href = baseUrl;
    });

    $('#personal-phrase-group').click(function () {
      var baseUrl = '${base_url}/phrase_group/list.html?' +
        encodeURIComponent(JSON.stringify({
          searches: [
            {field: 'studentId', value: '${student.id}' },
            {field: 'studentName', value: '${student.name}'}
          ]
        }));
      console.log(baseUrl);
      location.href = baseUrl;
    });

    $('#import-word-process').click(function () {
      dict.uploadFile({
        action: '${base_url}/word_group/import.json',
        multiple: true,
        data: {
          studentId: ${student.id}
        },
        accept: 'image/*',
        success: function (data, delta) {
          location.reload(true);
          parent.layer.alert('导入单词学习进度成功！<br>用时 ' + delta + ' 秒！', {icon: 1});
        }
      });
    });

    $('#import-phrase-process').click(function () {
      dict.uploadFile({
        action: '${base_url}/phrase_group/import.json',
        multiple: true,
        data: {
          studentId: ${student.id}
        },
        accept: 'image/*',
        success: function (data, delta) {
          location.reload(true);
          parent.layer.alert('导入短语学习进度成功！<br>用时 ' + delta + ' 秒！', {icon: 1});
        }
      });
    });
  </#escape>
</script>