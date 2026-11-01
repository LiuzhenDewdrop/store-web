<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>乐活益家运营平台</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="乐活益家运营平台">
    <meta name="description" content="五人齐商铺 一个买卖东西的地方">
    <link rel="shortcut icon" href="${ctx}/static/img/5r7_logo.png">

    <link rel="stylesheet" href="${ctx}/static/layui/css/layui.css">
    <link rel="stylesheet" href="${ctx}/static/css/global.css">

    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" media="all">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/personal.css" media="all">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">

    <script src="${ctx}/static/layui/layui.js"></script>
	<script src="${ctx}/static/js/util.js"></script>
	<style type="text/css">
		.item-img a.img{ display: block; width: 76px; height: 76px; margin: 0 auto; margin-bottom: 15px;}
		.layui-table tr { height: 100px; }
	</style>

<body>
<div class="larry-grid layui-anim layui-anim-upbit larryTheme-A" >
    <div class="larry-personal" >
        <div class="layui-tab" >
            <blockquote class="layui-elem-quote mylog-info-tit">
                    <div class="layui-inline">
                        <form class="layui-form" id="cusSearchForm">
							<div class="layui-form-item" style="margin-bottom:3px;">
                                <label class="layui-form-label">用户名</label>
                                <div class="layui-input-inline" style="width:140px;">
                                    <input type="text" name="nickname" value="" placeholder="请输入关键字" class="layui-input search_input">
                                </div>
								<shiro:hasPermission name="shop:customer:list">
                                <a class="layui-btn layui-btn-normal searchList_btn" lay-submit lay-filter="searchFilter"><i class="layui-icon  layui-icon-search"></i>查询</a>
								</shiro:hasPermission>
                            </div>
                        </form>
                    </div>
            </blockquote>
            <div class="larry-separate"></div>
            <!-- 列表 -->
            <div class="layui-tab-item layui-show" style="padding: 10px 15px;">
                <table id="resTableList" lay-filter="resTableId"></table>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    layui.config({
        base : "${ctx}/static/js/"
    }).use(['form', 'table', 'layer', 'commCms', 'bodyTab'], function () {
        var $ = layui.$,
                form = layui.form,
                table = layui.table,
                layer = layui.layer,
                common = layui.commCms;
		var tab = layui.bodyTab();

		<shiro:hasPermission name="shop:customer:list">
		function searchList(data) {
			var field = data && data.field
			table.render({
				elem: '#resTableList',
				url: '${ctx}/customer/list.do',
				id:'resTableId',
				method: 'post',
				loading:true,
				skin:'row',
				even:'true',
				size: 'lg',
				where: field,
				cols: [[
					{field: 'id', title: '用户id', width: '10%', align:'center'},
					// {field: 'openid', title: '微信openid，唯一标识', width: '10%', align:'center'},
					// {field: 'unionid', title: '微信unionid，多平台统一标识', width: '10%', align:'center'},
					{field: 'nickname', title: '昵称', width: '10%', align:'center'},
					{field: 'avatar', title: '头像URL', width: '10%', align:'center', templet: '#resImageTpl'},
					{field: 'phone', title: '手机号', width: '10%', align:'center'},
					{field: 'gender', title: '性别', width: '10%', align:'center', templet: '#resGenderTpl'},
					{field: 'upperId', title: '上级用户id', width: '10%', align:'center'},
					{field: 'personNum', title: '邀请人数', width: '10%', align:'center'},
					{field: 'unfundAmt', title: '待到账金额（单位：元）', width: '10%', align:'center', templet: '#resUnfundAmtTpl'},
					{field: 'fundAmt', title: '已到账金额（单位：元）', width: '10%', align:'center', templet: '#resFundAmtTpl'},
					{field: 'createTime', title: '注册时间', width: '10%', align:'center', templet: '#createTimeTpl'},
					{field: 'updateTime', title: '最后登录时间', width: '10%', align:'center', templet: '#updateTimeTpl'},
					{field: 'status', title: '禁用状态', width: '10%', align:'center', templet: '#resStatusTpl'},
					{field: 'delStatus', title: '删除状态', width: '10%', align:'center', templet: '#resDelStatusTpl'},
					// {fixed:'right', title: '操作', width: '15%', align:'center',toolbar: '#resBar'}
				]],
				page: true,
				limit: 20
			});
		}
		searchList();

		/**查询*/
		$(".searchList_btn").click(function(){
			form.on('submit(searchFilter)', function (data) {
				searchList(data);
			});
		});
		</shiro:hasPermission>


		/**监听工具条*/
		table.on('tool(resTableId)', function(obj) {
			var data = obj.data;
			var layEvent = obj.event;
			var url;
			switch (layEvent) {
				case 'res_edit':
					url =  '${ctx}/item/upd?id=' + data.id;
					common.cmsLayOpen('编辑商品',url,'880px','600px');
					break;
				case 'res_del':
					url = "${ctx}/item/del.do";
					var param = {id: data.id};
					common.ajaxCmsConfirm('系统提示', '确定删除该商品?',url,param);
					break;
				case 'res_operate':
					url = "${ctx}/item/operate.do";
					var param = {id: data.id};
					common.ajaxCmsConfirm('系统提示', '确定操作该商品?',url,param);
					break;
				default:
					break;
			}
			searchList();
		});
	});
</script>

<!-- 用户头像 tpl-->
<script type="text/html" id="resImageTpl">
	<div class="item-img">
		<a class="img" title="用户头像">
			<img src="{{= d.avatar}}" style="width:50px;height:50px;">
		</a>
	</div>
</script>

<!-- 性别 tpl-->
<script type="text/html" id="resGenderTpl">
	{{# if(d.gender == 0){ }}
	<span class="label label-warning ">未知</span>
	{{# } else if(d.gender == 1){ }}
	<span class="label label-info ">男</span>
	{{# } else if(d.gender == 2){ }}
	<span class="label label-danger ">女</span>
	{{# } }}
</script>

<!-- 待到账金额 tpl-->
<script type="text/html" id="resUnfundAmtTpl">
	{{fenToYuan(d.unfundAmt)}}
</script>

<!-- 已到账金额 tpl-->
<script type="text/html" id="resFundAmtTpl">
	{{fenToYuan(d.fundAmt)}}
</script>

<!-- 状态 tpl-->
<script type="text/html" id="resStatusTpl">
	{{# if(d.status == 0){ }}
	<span class="label label-danger ">禁用</span>
	{{# } else if(d.status == 1){ }}
	<span class="label label-info ">正常</span>
	{{# } }}
</script>

<!-- 删除状态 tpl-->
<script type="text/html" id="resDelStatusTpl">
	{{# if(d.delStatus == 0){ }}
	<span class="label label-info ">未删除</span>
	{{# } else if(d.delStatus == 1){ }}
	<span class="label label-danger ">已删除</span>
	{{# } }}
</script>

<!-- 时间tpl-->
<script type="text/html" id="updateTimeTpl">
	{{layui.util.toDateString(d.updateTime, 'yyyy-MM-dd HH:mm:ss')}}
</script>

<!-- 时间tpl-->
<script type="text/html" id="createTimeTpl">
	{{layui.util.toDateString(d.createTime, 'yyyy-MM-dd HH:mm:ss')}}
</script>

</body>
</html>