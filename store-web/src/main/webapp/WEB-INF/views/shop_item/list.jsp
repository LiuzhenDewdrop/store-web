<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">

    <script src="${ctx}/static/layui/layui.js"></script>
	<style type="text/css">
		.item-img a.img{ display: block; width: 76px; height: 76px; margin: 0 auto; margin-bottom: 15px;}
		.item-img a.img img{ display: block; border: none; width: 100%; height: 100%; border-radius: 50%; -webkit-border-radius: 50%; -moz-border-radius: 50%; border: 4px solid #44576b;}
	</style>

<body>
<div class="larry-grid layui-anim layui-anim-upbit larryTheme-A" >
    <div class="larry-personal" >
        <div class="layui-tab" >
            <blockquote class="layui-elem-quote mylog-info-tit">
                    <div class="layui-inline">
                        <form class="layui-form" id="cusSearchForm">
                            <div class="layui-form-item" style="margin-bottom:3px;">
								<label class="layui-form-label">商品类型</label>
								<div class="layui-input-inline" style="width:140px;">
									<select name="itemType" >
										<option selected="selected"></option>
										<option value="1">实体</option>
										<option value="2">虚拟</option>
									</select>
								</div>
							</div>
							<div class="layui-form-item" style="margin-bottom:3px;">
                                <label class="layui-form-label">商品名称</label>
                                <div class="layui-input-inline" style="width:140px;">
                                    <input type="text" name="itemName" value="" placeholder="请输入关键字" class="layui-input search_input">
                                </div>

                                <label class="layui-form-label">商品分类</label>
                                <div class="layui-input-inline" style="width:140px;">
                                	<select name="itemCategory1" id="itemCategory1" lay-filter="categoryFilter">
                                		<option selected="selected" value="">请选择1级分类</option>
                                	</select>
                                </div>
								<div class="layui-input-inline" style="width:140px;">
									<select name="itemCategory2" id="itemCategory2" lay-filter="categoryFilter">
										<option selected="selected" value="">请选择2级分类</option>
									</select>
								</div>
								<div class="layui-input-inline" style="width:140px;">
									<select name="itemCategory3" id="itemCategory3">
										<option selected="selected" value="">请选择3级分类</option>
									</select>
								</div>
                                <label class="layui-form-label" >商品状态</label>
                                <div class="layui-input-inline" style="width:140px;">
                                    <select name="itemStatus" >
                                		<option selected="selected"></option>
                                		<option value="1">上架</option>
                                		<option value="2">下架</option>
                                		<option value="3">草稿</option>
                                	</select>
                                </div>
								<shiro:hasPermission name="shop:item:list">
                                <a class="layui-btn layui-btn-normal searchList_btn" lay-submit lay-filter="searchFilter"><i class="layui-icon  layui-icon-search"></i>查询</a>
								</shiro:hasPermission>
                            </div>
                        </form>
                    </div>
            </blockquote>
            <div class="larry-separate"></div>
            <!-- 商品列表 -->
            <div class="layui-tab-item layui-show" style="padding: 10px 15px;">
                <shiro:hasPermission name="shop:item:add">
                    <div class="layui-inline" style="margin-bottom: 10px;">
                        <a class="layui-btn layui-btn-normal resAdd_btn" href="javascript:;" data-url="/store-web/item/add">
							<i class="layui-icon layui-icon-add-circle " data-icon="layui-icon-add-circle"></i>
							<cite>新增商品</cite>
						</a>
                    </div>
                </shiro:hasPermission>
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

		function queryCategory(level, pid) {
			$.ajax({
				type: "POST",
				url: "${ctx}/category/list.do",
				dataType: "json",
				data:{pId: pid},
				success: function (data) {
					var id = "#itemCategory"+level;
					$(id).empty();
					$(id).append('<option selected="selected" value="">请选择'+level+'级分类</option>');
					switch (level) {
						case 1:
							$('#category2Filter option').not(":first").remove();
							$('#category3Filter option').not(":first").remove();
							break;
						case 2:
							$('#category3Filter option').not(":first").remove();
							break;
						case 3:
							break;
						default:
							break;
					}
					$(data.data).each(function(index, item) {
						$(id).append(
							'<option value="'+item.id+'">'+item.categoryName+'</option>'
						);
					});
					form.render('select');
				}
			});
		}

		// 改变1级分组时，刷新2/3级分组
		// 改变2级分组时，刷新3级分组
		form.on('select(categoryFilter)', function(data){
			if (!data.value) {
				return ;
			}
			switch (data.elem.id) {
				case "itemCategory1":
					queryCategory(2, data.value);
					break;
				case "itemCategory2":
					queryCategory(3, data.value);
					break;
				default:
					break;
			}
		});

		function searchList(data) {
			var field = data && data.field
			table.render({
				elem: '#resTableList',
				url: '${ctx}/item/list.do',
				id:'resTableId',
				method: 'post',
				loading:true,
				skin:'row',
				even:'true',
				size: 'sm',
				where: field,
				cols: [[
					// {field:'id', title: '序号', width: '6%', align:'center'},
					{field:'itemType', title: '商品类型', width: '10%', align:'center',templet: '#resTypeTpl'},
					{field:'itemImage', title: '商品图片', width: '10%', align:'center',templet: '#resImageTpl'},
					{field:'itemName', title: '商品名称', width: '10%', align:'center'},
					{field:'itemCode', title: '商品编码', width: '10%', align:'center'},
					{field:'brandName', title: '商品品牌', width: '10%', align:'center'},
					{field:'shopName', title: '供应商', width: '10%', align:'center'},
					{field:'itemStatus', title: '商品状态', width: '10%', align:'center',templet: '#resStatusTpl'},
					{field:'updateTime', title: '更新时间', width: '10%', align:'center', templet: '#updateTimeTpl'},
					{field:'createTime', title: '创建时间', width: '10%', align:'center', templet: '#createTimeTpl'},
					{fixed:'right', title: '操作', width: '10%', align:'center',toolbar: '#resBar'}
				]],
				page: true,
				limit: 20
			});
		}

		<shiro:hasPermission name="shop:item:list">
		searchList();
		queryCategory(1, 0);
		</shiro:hasPermission>

		/**查询*/
		$(".searchList_btn").click(function(){
			form.on('submit(searchFilter)', function (data) {
				searchList(data);
			});
		});

		/**新增商品*/
		$(".resAdd_btn").click(function(){
			top.addTab($(this));
			//var url = "${ctx}/item/add";
			//common.cmsLayOpen('新增商品',url,'880px','600px');
		});

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
				case 'res_detail':
					url =  '${ctx}/item/detail?id=' + data.id;
					common.cmsLayOpen('查看商品详情',url,'880px','600px');
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
		});
	});
</script>

<!-- 商品图片tpl-->
<script type="text/html" id="resImageTpl">
	<div class="item-img">
		<a class="img" title="商品图片">
			<img src="{{= d.itemImage}}">
		</a>
	</div>
</script>

<!-- 商品类型tpl-->
<script type="text/html" id="resTypeTpl">
    {{# if(d.itemStatus == 1){ }}
    <span class="label label-info ">实体</span>
    {{# } else if(d.itemStatus == 2){ }}
    <span class="label label-danger ">虚拟</span>
    {{# } }}
</script>

<!-- 商品状态tpl-->
<script type="text/html" id="resStatusTpl">
    {{# if(d.itemStatus == 1){ }}
    <span class="label label-info ">上架</span>
    {{# } else if(d.itemStatus == 2){ }}
    <span class="label label-danger ">下架</span>
    {{# } else if(d.itemStatus == 3){ }}
    <span class="label label-warning ">草稿</span>
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

<!--工具条 -->
<script type="text/html" id="resBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="shop:item:upd">
			{{# if(d.itemStatus == 1){ }}
			<a class="layui-btn layui-btn-xs layui-btn-disabled" lay-event="res_edit"><i class="layui-icon  layui-icon-edit"></i>编辑</a>
			{{# } else if(d.itemStatus == 2 || d.itemStatus == 3){ }}
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_edit"><i class="layui-icon  layui-icon-edit"></i>编辑</a>
			{{# } }}
		</shiro:hasPermission>
		<shiro:hasPermission name="shop:item:detail">
			<a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="res_detail"><i class="layui-icon  layui-icon-read"></i>详情</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="shop:item:operate">
			{{# if(d.itemStatus == 1){ }}
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_operate"><i class="layui-icon  layui-icon-down"></i>下架</a>
			{{# } else if(d.itemStatus == 2 || d.itemStatus == 3){ }}
			<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="res_operate"><i class="layui-icon  layui-icon-up"></i>上架</a>
			{{# } }}
		</shiro:hasPermission>
		<shiro:hasPermission name="shop:item:del">
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
			{{# if(d.itemStatus == 1){ }}
			<a class="layui-btn layui-btn-xs layui-btn-disabled" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
			{{# } else if(d.itemStatus == 2 || d.itemStatus == 3){ }}
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
			{{# } }}
		</shiro:hasPermission>
	</div>
</script>

</body>
</html>