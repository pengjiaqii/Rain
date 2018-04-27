# 看一看,瞧一瞧
波浪式的loading效果-贝塞尔曲线,本来先画成正弦函数的,结果好像没成功.....
增加了一个监听,加载到100%后跳转到数字雨
数字雨由整体的父布局加一列一列的item组成

九宫格的手势解锁,由三部分组成
九个按钮的父容器-用来确定每个按钮的位置,以及用户画线的轨迹
每一个小按钮-通过画两个圆来实现的布局,被选中和未被选中时颜色不同
每个按钮所在的区域-通过判断当前手指所在的x,y值是否在当前区域里面从来得出按钮是否被选中


![image](https://github.com/pengjiaqii/Rain/blob/master/NumRain/preview.gif)
![image](https://github.com/pengjiaqii/Rain/blob/master/NumRain/gestureunlock.gif)

