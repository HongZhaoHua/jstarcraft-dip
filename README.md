# JStarCraft DIP

****

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Total lines](https://tokei.rs/b1/github/HongZhaoHua/jstarcraft-dip?category=lines)](https://tokei.rs/b1/github/HongZhaoHua/jstarcraft-dip?category=lines)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8e39a24e1be740c58b83fb81763ba317)](https://www.codacy.com/project/HongZhaoHua/jstarcraft-dip/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=HongZhaoHua/jstarcraft-dip&amp;utm_campaign=Badge_Grade_Dashboard)

希望路过的同学,顺手给JStarCraft框架点个Star,算是对作者的一种鼓励吧!

****

## 目录

* [介绍](#介绍)
* [特性](#特性)
* [安装](#安装)
    * [安装JStarCraft Core框架](#安装JStarCraft-Core框架)
    * [安装JStarCraft AI框架](#安装JStarCraft-AI框架)
    * [安装JStarCraft DIP引擎](#安装JStarCraft-DIP引擎)
* [使用](#使用)
* [架构](#架构)
* [概念](#概念)
* [示例](#示例)
* [对比](#对比)
* [版本](#版本)
* [参考](#参考)
* [协议](#协议)
* [作者](#作者)
* [致谢](#致谢)
* [捐赠](#捐赠)

****

## 介绍

**JStarCraft DIP是一个面向数字图像处理领域的轻量级引擎.遵循Apache 2.0协议.**

专注于解决数字图像处理领域的几个核心问题:
* 图像转换
* 图像相似度
* 目标定位
* 目标检测
* 图像分割
* 图像聚类
* 图像分类

涵盖了多种数字图像处理,计算机视觉,计算机图形算法.为相关领域的研发人员提供提供满足工业级别场景要求的通用设计与参考实现,普及面向数字图像处理在Java领域的应用.

****

## 特性

* 1.图像转换
* 2.图像哈希
    * 均值哈希(Average Hash)
    * 感知哈希(Perceptual Hash)
    * 差异哈希 (Different Hash)
    * 小波哈希(Wavelet Hash)
* 3.目标定位
    * 分词
    * 词性标注
* 4.目标检测
    * 句法结构分析
    * 依存关系分析
* 5.图像分割
* 6.图像聚类
* 7.图像分类

****

## 安装

JStarCraft RNS要求使用者具备以下环境:
* JDK 8或者以上
* Maven 3

#### 安装JStarCraft-Core框架

```shell
git clone https://github.com/HongZhaoHua/jstarcraft-core.git

mvn install -Dmaven.test.skip=true
```

#### 安装JStarCraft-AI框架

```shell
git clone https://github.com/HongZhaoHua/jstarcraft-ai.git

mvn install -Dmaven.test.skip=true
```

####  安装JStarCraft-DIP引擎

```shell
git clone https://github.com/HongZhaoHua/jstarcraft-dip.git

mvn install -Dmaven.test.skip=true
```

****

## 使用

#### 设置依赖

* 设置Maven依赖

```maven
<dependency>
    <groupId>com.jstarcraft</groupId>
    <artifactId>dip</artifactId>
    <version>1.0</version>
</dependency>
```

* 设置Gradle依赖

```gradle
compile group: 'com.jstarcraft', name: 'dip', version: '1.0'
```

****

## 架构

****

## 概念

****

## 示例

****

## 对比

****

## 版本

****

## 参考

****

## 协议

JStarCraft DIP遵循[Apache 2.0协议](https://www.apache.org/licenses/LICENSE-2.0.html),一切以其为基础的衍生作品均属于衍生作品的作者.

****

## 作者

| 作者 | 洪钊桦 |
| :----: | :----: |
| E-mail | 110399057@qq.com, jstarcraft@gmail.com |

****

## 致谢

****

## 捐赠

****