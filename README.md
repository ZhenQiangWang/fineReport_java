# fineReport_java
帆软报表程序数据集

为避免后续更新/新增，每次都需要重启fineReport服务器，通过调用python脚本方式解决 

做了简单的封装跟逻辑

主要逻辑：

1. 初始化获取数据列数、列名、顺序，查询指定数据库实现
2. 调用指定路径pythob脚本，路径及传参(模板参数\查询条件)
3. 脚本接收参数执行指定逻辑并返回给程序数据集
4. 程序数据集获取返回值，判断数据类型：数据？log？并执行相关逻辑
### 调用的python脚本详见：https://github.com/ZhenQiangWang/fineReport_python