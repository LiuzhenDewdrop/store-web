<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>STEAM HELPER</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="STEAM HELPER">
    <meta name="description" content="STEAM助手 管理账号和游戏相关信息">
    <link rel="shortcut icon" href="${ctx}/static/img/steam_logo.png">

    <link rel="stylesheet" href="${ctx}/static/layui/css/layui.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">

    <script src="${ctx}/static/layui/layui.js"></script>
    <style type="text/css">
        a {
            cursor: pointer;
        }
    </style>
</head>
<body class="childrenBody" style="font-size:12px;margin: 10px 10px 0;">
<fieldset class="layui-elem-field ">
    <legend style="font-size: 12px;color:#FF5722;">双击选择图标</legend>
    <div class="layui-field-box">
        <form class="layui-form layui-form-pane">
            <div class="layui-form-item" style="margin-bottom:0px;">
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-rate-half" title="半星"><i class="layui-icon layui-icon-rate-half" ></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-rate" title="星星-空心"><i class="layui-icon layui-icon-rate"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-rate-solid" title="星星-实心"><i class="layui-icon layui-icon-rate-solid"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-cellphone" title="手机"><i class="layui-icon layui-icon-cellphone"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-vercode" title="验证码"><i class="layui-icon layui-icon-vercode"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-login-wechat" title="微信"><i class="layui-icon layui-icon-login-wechat"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-login-qq" title="QQ"><i class="layui-icon layui-icon-login-qq"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-login-weibo" title="微博"><i class="layui-icon layui-icon-login-weibo"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-password" title="密码"><i class="layui-icon layui-icon-password"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-username" title="用户名"><i class="layui-icon layui-icon-username"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-refresh-3" title="刷新-粗"><i class="layui-icon layui-icon-refresh-3"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-auz" title="授权"><i class="layui-icon layui-icon-auz"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-spread-left" title="左向右伸缩菜单"><i class="layui-icon layui-icon-spread-left"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-shrink-right" title="右向左伸缩菜单"><i class="layui-icon layui-icon-shrink-right"></i></a>
                    </div>
                </div>
                
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-snowflake" title="雪花"><i class="layui-icon layui-icon-snowflake"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-tips" title="提示说明"><i class="layui-icon layui-icon-tips"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-note" title="便签"><i class="layui-icon layui-icon-note"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-home" title="主页"><i class="layui-icon layui-icon-home"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-senior" title="高级"><i class="layui-icon layui-icon-senior"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-refresh" title="刷新"><i class="layui-icon layui-icon-refresh"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-refresh-1" title="刷新"><i class="layui-icon layui-icon-refresh-1"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-flag" title="旗帜"><i class="layui-icon layui-icon-flag"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-theme" title="主题"><i class="layui-icon layui-icon-theme"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-notice" title="消息-通知"><i class="layui-icon layui-icon-notice"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-website" title="网站"><i class="layui-icon layui-icon-website"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-console" title="控制台"><i class="layui-icon layui-icon-console"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-face-surprised" title="表情-惊讶"><i class="layui-icon layui-icon-face-surprised"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-set" title="设置-空心"><i class="layui-icon layui-icon-set"></i></a>
                    </div>
                </div>
                
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-template-1" title="模板"><i class="layui-icon layui-icon-template-1"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-app" title="应用"><i class="layui-icon layui-icon-app"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-template" title="模板"><i class="layui-icon layui-icon-template"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-praise" title="赞"><i class="layui-icon layui-icon-praise"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-tread" title="踩"><i class="layui-icon layui-icon-tread"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-male" title="男"><i class="layui-icon layui-icon-male"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-female" title="女"><i class="layui-icon layui-icon-female"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-camera" title="相机-空心"><i class="layui-icon layui-icon-camera"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-camera-fill" title="相机-实心"><i class="layui-icon layui-icon-camera-fill"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-more" title="菜单-水平"><i class="layui-icon layui-icon-more"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-more-vertical" title="菜单-垂直"><i class="layui-icon layui-icon-more-vertical"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-rmb" title="金额-人民币"><i class="layui-icon layui-icon-rmb"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-dollar" title="金额-美元"><i class="layui-icon layui-icon-dollar"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-diamond" title="钻石-等级"><i class="layui-icon layui-icon-diamond"></i></a>
                    </div>
                </div>
                
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-fire" title="火"><i class="layui-icon layui-icon-fire"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-return" title="返回"><i class="layui-icon layui-icon-return"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-location" title="位置-地图"><i class="layui-icon layui-icon-location"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-read" title="办公-阅读"><i class="layui-icon layui-icon-read"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-survey" title="调查"><i class="layui-icon layui-icon-survey"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-face-smile" title="表情-微笑"><i class="layui-icon layui-icon-face-smile"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-face-cry" title="表情-哭泣"><i class="layui-icon layui-icon-face-cry"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-cart-simple" title="购物车"><i class="layui-icon layui-icon-cart-simple"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-cart" title="购物车"><i class="layui-icon layui-icon-cart"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-next" title="下一页"><i class="layui-icon layui-icon-next"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-prev" title="上一页"><i class="layui-icon layui-icon-prev"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-upload-drag" title="上传-空心-拖拽"><i class="layui-icon layui-icon-upload-drag"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-upload" title="上传-实心"><i class="layui-icon layui-icon-upload"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-download-circle" title="下载-圆圈"><i class="layui-icon layui-icon-download-circle"></i></a>
                    </div>
                </div>
                
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-component" title="组件"><i class="layui-icon layui-icon-component"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-file-b" title="文件-粗"><i class="layui-icon layui-icon-file-b"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-user" title="用户"><i class="layui-icon layui-icon-user"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-find-fill" title="发现-实心"><i class="layui-icon layui-icon-find-fill"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-loading" title="loading"><i class="layui-icon layui-icon-loading"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-loading-1" title="loading"><i class="layui-icon layui-icon-loading-1"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-add-1" title="添加"><i class="layui-icon layui-icon-add-1"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-play" title="播放"><i class="layui-icon layui-icon-play"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-pause" title="暂停"><i class="layui-icon layui-icon-pause"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-headset" title="音频-耳机"><i class="layui-icon layui-icon-headset"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-video" title="视频"><i class="layui-icon layui-icon-video"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-voice" title="语音-声音"><i class="layui-icon layui-icon-voice"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-speaker" title="消息-通知-喇叭"><i class="layui-icon layui-icon-speaker"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-fonts-del" title="删除线"><i class="layui-icon layui-icon-fonts-del"></i></a>
                    </div>
                </div>
                
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-fonts-code" title="代码"><i class="layui-icon layui-icon-fonts-code"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-fonts-html" title="HTML"><i class="layui-icon layui-icon-fonts-html"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-fonts-strong" title="字体加粗"><i class="layui-icon layui-icon-fonts-strong"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-unlink" title="删除链接"><i class="layui-icon layui-icon-unlink"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-picture" title="图片"><i class="layui-icon layui-icon-picture"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-link" title="链接"><i class="layui-icon layui-icon-link"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-face-smile-b" title="表情-笑-粗"><i class="layui-icon layui-icon-face-smile-b"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-align-left" title="左对齐"><i class="layui-icon layui-icon-align-left"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-align-right" title="右对齐"><i class="layui-icon layui-icon-align-right"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-align-center" title="居中对齐"><i class="layui-icon layui-icon-align-center"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-fonts-u" title="字体-下划线"><i class="layui-icon layui-icon-fonts-u"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-fonts-i" title="字体-斜体"><i class="layui-icon layui-icon-fonts-i"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-tabs" title="Tabs 选项卡"><i class="layui-icon layui-icon-tabs"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-radio" title="单选框-选中"><i class="layui-icon layui-icon-radio"></i></a>
                    </div>
                </div>
                
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-circle" title="单选框-候选"><i class="layui-icon layui-icon-circle"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-edit" title="编辑"><i class="layui-icon layui-icon-edit"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-share" title="分享"><i class="layui-icon layui-icon-share"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-delete" title="删除"><i class="layui-icon layui-icon-delete"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-form" title="表单"><i class="layui-icon layui-icon-form"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-cellphone-fine" title="手机-细体"><i class="layui-icon layui-icon-cellphone-fine"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-dialogue" title="聊天 对话 沟通"><i class="layui-icon layui-icon-dialogue"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-fonts-clear" title="文字格式化"><i class="layui-icon layui-icon-fonts-clear"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-layer" title="窗口"><i class="layui-icon layui-icon-layer"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-date" title="日期"><i class="layui-icon layui-icon-date"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-code-circle" title="水 下雨"><i class="layui-icon layui-icon-code-circle"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-carousel" title="轮播组图"><i class="layui-icon layui-icon-carousel"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-code-circle" title="代码-圆圈"><i class="layui-icon layui-icon-code-circle"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-prev-circle" title="翻页"><i class="layui-icon layui-icon-prev-circle"></i></a>
                    </div>
                </div>
                
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-layouts" title="布局"><i class="layui-icon layui-icon-layouts"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-util" title="工具"><i class="layui-icon layui-icon-util"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-templeate-1" title="选择模板"><i class="layui-icon layui-icon-templeate-1"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-upload-circle" title="上传-圆圈"><i class="layui-icon layui-icon-upload-circle"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-tree" title="树"><i class="layui-icon layui-icon-tree"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-table" title="表格"><i class="layui-icon layui-icon-table"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-chart" title="图表"><i class="layui-icon layui-icon-chart"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-chart-screen" title="图标 报表 屏幕"><i class="layui-icon layui-icon-chart-screen"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-engine" title="引擎"><i class="layui-icon layui-icon-engine"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-triangle-d" title="下三角"><i class="layui-icon layui-icon-triangle-d"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-triangle-r" title="右三角"><i class="layui-icon layui-icon-triangle-r"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-file" title="文件"><i class="layui-icon layui-icon-file"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-set-sm" title="设置-小型"><i class="layui-icon layui-icon-set-sm"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-add-circle" title="添加-圆圈"><i class="layui-icon layui-icon-add-circle"></i></a>
                    </div>
                </div>
                
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-404" title="404"><i class="layui-icon layui-icon-404"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-about" title="关于"><i class="layui-icon layui-icon-about"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-up" title="箭头 向上"><i class="layui-icon layui-icon-up"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-down" title="箭头 向下"><i class="layui-icon layui-icon-down"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-left" title="箭头 向左"><i class="layui-icon layui-icon-left"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-right" title="箭头 向右"><i class="layui-icon layui-icon-right"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-circle-dot" title="圆点"><i class="layui-icon layui-icon-circle-dot"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-search" title="搜索"><i class="layui-icon layui-icon-search"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-set-fill" title="设置-实心"><i class="layui-icon layui-icon-set-fill"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-group" title="群组"><i class="layui-icon layui-icon-group"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-friends" title="好友"><i class="layui-icon layui-icon-friends"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-reply-fill" title="回复 评论 实心"><i class="layui-icon layui-icon-reply-fill"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-menu-fill" title="菜单 隐身 实心"><i class="layui-icon layui-icon-menu-fill"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-log" title="记录"><i class="layui-icon layui-icon-log"></i></a>
                    </div>
                </div>
                
                <div class="layui-inline" style="margin-bottom:0px;">
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-picture-fine" title="图片-细体"><i class="layui-icon layui-icon-picture-fine"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-face-smile-fine" title="表情-笑-细体"><i class="layui-icon layui-icon-face-smile-fine"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-list" title="列表"><i class="layui-icon layui-icon-list"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-release" title="发布 纸飞机"><i class="layui-icon layui-icon-release"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-ok" title="对 OK"><i class="layui-icon layui-icon-ok"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-help" title="帮助"><i class="layui-icon layui-icon-help"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-chat" title="客服"><i class="layui-icon layui-icon-chat"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-top" title="top 置顶"><i class="layui-icon layui-icon-top"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-star" title="收藏-空心"><i class="layui-icon layui-icon-star"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-star-fill" title="收藏-实心"><i class="layui-icon layui-icon-star-fill"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-close-fill" title="关闭-实心"><i class="layui-icon layui-icon-close-fill"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-close" title="关闭-空心"><i class="layui-icon layui-icon-close"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-ok-circle" title="正确"><i class="layui-icon layui-icon-ok-circle"></i></a>
                    </div>
                    <div class="layui-form-mid layui-word-aux">
                        <a class="select_img" data-id="layui-icon-add-circle-fine" title="添加-圆圈-细体"><i class="layui-icon layui-icon-add-circle-fine"></i></a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</fieldset>
<script type="text/javascript">
    layui.config({
        base : "${ctx}/static/js/"
    }).use(['form','layer','jquery','commCms'],function(){
        var $ = layui.$,
                form = layui.form,
                common = layui.commCms,
                layer = layui.layer;

        /**选择图标*/
        $("body").on("dblclick",".select_img",function(){
            //得到当前iframe层的索引
            var index = parent.layer.getFrameIndex(window.name);
            //赋值
            var resImage = $(this).attr("data-id");
            $('#resImage', window.parent.document).val(resImage);
            parent.layer.close(index); //执行关闭
        });
    });
</script>
</body>
</html>