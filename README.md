# 家庭理财管理系统

## 项目简介

**项目名称**：家庭理财管理系统

**项目介绍**：该系统能够管理家庭收入支出，并且能直观得表现收支状态。主要功能包括用户管理、收支管理、财务管理、统计收支情况等功能。

## 项目介绍

### 项目演示

后端项目`FamilyFinancialManagementSystem`地址：https://github.com/Nusture/FamilyFinancialManagementSystem

项目演示地址： https://miniapp.nusture.top/

![image-20220216210417453](https://typora-1300833172.cos.ap-guangzhou.myqcloud.com/Typora%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87/image-20220216210417453.png)

![image-20220216211512567](https://typora-1300833172.cos.ap-guangzhou.myqcloud.com/Typora%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87/image-20220216211512567.png)

![image-20220216211524999](https://typora-1300833172.cos.ap-guangzhou.myqcloud.com/Typora%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87/image-20220216211524999.png)

![image-20220216211539564](https://typora-1300833172.cos.ap-guangzhou.myqcloud.com/Typora%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87/image-20220216211539564.png)

![image-20220216211551257](https://typora-1300833172.cos.ap-guangzhou.myqcloud.com/Typora%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87/image-20220216211551257.png)

### 技术选型

#### 后端技术

| 技术             | 说明                     |
| ---------------- | ------------------------ |
| SpringBoot       | 容器+MVC框架             |
| Sa-Token         | 认证和授权框架           |
| MyBatis-Plus     | MyBatis的增强工具        |
| MyBatisGenerator | 数据层代码生成           |
| MySQL            | 关系型数据库             |
| Nginx            | 静态资源服务器           |
| Lombok           | 简化对象封装工具         |
| Fastjson         | 阿里巴巴的开源JSON解析库 |
| Spark            | 大数据计算引擎           |

#### 前端技术

| 技术       | 说明                 |
| ---------- | -------------------- |
| Vue        | 前端框架             |
| Vue-router | 路由框架             |
| Vuex       | 全局状态管理框架     |
| Element    | 前端UI框架           |
| Axios      | 前端HTTP框架         |
| AntV       | 蚂蚁数据可视化图表库 |

### 搭建步骤

#### 后端项目启动

* 克隆后端项目到本地，并导入到IDEA中
* 安装MySQL并创建数据库`ffms`，然后使用sql文件来创建对应表
* 将src/main/resources/目录下的application.yaml中的url，username和password更改为自己数据库对应的
* 将src/main/java/com/nusture/util/目录下的SparkSql.java中的所有url，username和password更改为自己数据库对应的
* 运行src/main/java/com/nusture/目录下的FamilyFinancialManagementSystemApplication就启动了后端项目

#### 前端项目启动

* 克隆前端项目到本地
* 将项目导入到IDEA中并在最下方的终端运行`npm install`
* 继续在终端执行`npm run dev`就启动了前端项目
* 在浏览器中输入 http://localhost:3000/ 即可访问项目



## 有关问题

Q：首次注册进入首页或者新增支出和收入之后提示网络延迟并且图表一直转圈怎么回事？

A：首次注册出现这个情况只需要在支出管理或者收入管理新增一条支出或者收入记录就可以解决（可能首页图表不会立即刷新，需要几分钟之后才能刷新，这是因为使用了Spark算子所以有延迟）