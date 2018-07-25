system:
  description: '
    `${system_title}` 是一个学习英语单词的Web系统！
  '
  columns:
    - title: 单词、短语存储
      content: '
        对单词进行增删改查，并能从Excel表格中导入单词，可以存放在不同的词库中
      '
    - title: 导出单词文档
      content: '
        可筛选出不同的单词，根据不同的模板导出不同的单词文档，例如生词表、抄写表、排查表
      '
    - title: 学生学习情况统计
      content: '
        可将学生已学习过的单词标记为已掌握或者未掌握，并且可以根据学生掌握情况筛选单词
      '

devtools:
  description: '
    `${system_title}` 后端采用的是SpringBoot技术，
    前端采用的LayUI技术，
    利用SQLite数据库存储数据，
    利用POI进行Word和Excel的读写操作。
  '
  columns:
    - title: SpringBoot
      content: '
        SpringBoot是由Pivotal团队提供的全新框架，
        其设计目的是用来简化新Spring应用的初始搭建以及开发过程。
        该框架使用了特定的方式来进行配置，从而使开发人员不再需要定义样板化的配置。
      '
    - title: LayUI
      content: '
        LayUI是一款采用自身模块规范编写的前端UI框架，遵循原生HTML/CSS/JS的书写与组织形式，
        门槛极低，拿来即用。其外在极简，却又不失饱满的内在，体积轻盈，组件丰盈，
        从核心代码到 API 的每一处细节都经过精心雕琢，非常适合界面的快速开发。
      '
    - title: SQLite
      content: '
        SQLite是一个进程内的库，实现了自给自足的、无服务器的、零配置的、事务性的 SQL 数据库引擎。
        它是一个零配置的数据库，这意味着与其他数据库一样，您不需要在系统中配置。
      '
