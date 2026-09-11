<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>5r7 store</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="5r7 store">
    <meta name="description" content="五人齐商铺 一个买卖东西的地方">

    <link rel="shortcut icon" href="${ctx}/static/img/steam_logo.png">
    <link rel="stylesheet" href="${ctx}/static/layui/css/layui.css">

    <link rel="stylesheet" href="${ctx}/static/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" media="all">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/personal.css" media="all">
    <link rel="stylesheet" href="${ctx}/static/css/main.css">
    <link rel="stylesheet" href="${ctx}/static/css/fontcss.css">
    <link rel="stylesheet" href="${ctx}/static/css/home.css">

    <script src="${ctx}/static/js/jquery-1.8.3.js"></script>
    <script src="${ctx}/static/js/jquery.leoweather.min.js"></script>

    <script type="text/javascript" src="${ctx}/static/layui/layui.js"></script>

</head>
<body>
<div class="layui-fluid">
    <div class="layui-low">
        <div class="layui-col-md12 home-head">
        </div>
    </div>
</div>
<script type="text/javascript">
    var $;
    layui.config({
        base: "${ctx}/static/js/"
    }).use(['form', 'jquery', 'layer'], function () {
        $ = layui.$;
        var common = layui.commCms;

        //首页卡片tab添加
        $(".panel a").on("click", function () {
            window.parent.addTab($(this));
        });

        //浏览器大小改变时重置大小
        /**window.onresize = function () {
            psLineChar.resize();

        };*/


    })
    ;

</script>
</body>
</html>