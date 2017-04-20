<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>文章列表</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/hadmin/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/font-awesome.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/style.css?v=${version}" rel="stylesheet">
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox ">
                    <div class="ibox-title">
                        <h5>文章管理</h5>
                    </div>
                    <div class="ibox-content">
                        <p>
                        	<@shiro.hasPermission name="system:article:add">
                        		<button class="btn btn-success " type="button" onclick="add();">
									<i class="fa fa-plus"></i>&nbsp;添加
								</button>
                        	</@shiro.hasPermission>
                        </p>
                        <hr>
                        <div class="row row-lg">
		                    <div class="col-sm-12">
		                        <!-- Example Card View -->
		                        <div class="example-wrap">
		                            <div class="example">
		                            	<table id="table_list"></table>
		                            </div>
		                        </div>
		                        <!-- End Example Card View -->
		                    </div>
	                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 全局js -->
	<#include "/admin/common/common.ftl">
    <!-- Page-Level Scripts -->
    <script>
		var colors=['badge badge-info','badge badge-primary','badge badge-success',
		'badge badge-warning','badge badge-danger'];
        $(document).ready(function () {
        	//初始化表格,动态从服务器加载数据
			$("#table_list").bootstrapTable({
			    //使用get请求到服务器获取数据
			    method: "POST",
			    //必须设置，不然request.getParameter获取不到请求参数
			    contentType: "application/x-www-form-urlencoded",
			    //获取数据的Servlet地址
			    url: "${ctx!}/admin/article/list",
			    //表格显示条纹
			    striped: true,
			    //启动分页
			    pagination: true,
			    //每页显示的记录数
			    pageSize: 10,
			    //当前第几页
			    pageNumber: 1,
			    //记录数可选列表
			    pageList: [5, 10, 15, 20, 25,30,50,100,200],
			    //是否启用查询
			    search: true,
			    //是否启用详细信息视图
			    detailView:true,
			    detailFormatter:detailFormatter,
			    //表示服务端请求
			    sidePagination: "server",
			    //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
			    //设置为limit可以获取limit, offset, search, sort, order
			    queryParamsType: "undefined",
			    //json数据解析
			    responseHandler: function(res) {
			        return {
			            "rows": res.content,
			            "total": res.totalElements
			        };
			    },
			    //数据列
			    columns: [{
			        title: "ID",
			        field: "id",
			        sortable: true
			    },
				{
					title: "分类",
					field: "articleSort",
                    formatter:function (value,row,index) {
						if (null!=row.articleSort){
						    var myclass=index%5;
                           var result='<span class="'+colors[myclass]+'">'+row.articleSort.title+'</span>';
						    return result;
						}else{
						    return "";
						}
                    },
                    sortable: true
				},
				{
			        title: "标题",
			        field: "title"
			    },
				{
					title: "作者",
					field: "author"
				},
				{
					title: "标签云",
					field: "label"
				},{
				title: "创建时间",
				field: "createTime",
				sortable: true
			    },{
			        title: "更新时间",
			        field: "updateTime",
			        sortable: true
			    }, {
                        title: "备注",
                        field: "remark"
                    },
					{
                        title: "操作",
                        field: "empty",
                        formatter: function (value, row, index) {
                            var operateHtml = '<@shiro.hasPermission name="system:article:edit"> <button class="btn btn-primary btn-xs" type="button" onclick="edit(\''+ row.id+'\')"><i class="fa fa-edit"></i>&nbsp;修改</button> &nbsp;</@shiro.hasPermission>';
                            operateHtml = operateHtml + '<@shiro.hasPermission name="system:article:deleteBatch"><button class="btn btn-danger btn-xs" type="button" onclick="del(\''+ row.id+'\')"><i class="fa fa-remove"></i>&nbsp;删除</button> &nbsp;</@shiro.hasPermission>';
                            return operateHtml;
                        }
                    },
					{
					    title:"预览",
						field:"yulan",
						formatter:function (value,row) {
							return '<a class="btn btn-danger btn-xs" type="button" href="${ctx!}/blog/detail/'+row.id+'" target="_blank"><i class="internet-explorer"></i>&nbsp;预览</a>';
                        }
					}

			    ]
			});
        });

        function edit(id){
        	layer.open({
        	      type: 2,
        	      title: '问修改',
        	      shadeClose: true,
        	      shade: 0,
				  maxmin: true,
        	      area: ['893px', '800px'],
        	      content: '${ctx!}/admin/article/edit/' + id,
        	      end: function(index){
        	    	  $('#table_list').bootstrapTable("refresh");
       	    	  }
        	    });
        }
        function add(){
        	layer.open({
        	      type: 2,
        	      title: '文章添加',
        	      shadeClose: true,
        	      shade: false,
				  maxmin: true, //开启最大化最小化按钮
        	      area: ['893px', '800px'],
        	      content: '${ctx!}/admin/article/add',
        	      end: function(index){
        	    	  $('#table_list').bootstrapTable("refresh");
       	    	  }
        	    });
        }
        function grant(id){
        	layer.open({
        	      type: 2,
        	      title: '关联角色',
        	      shadeClose: true,
        	      shade: false,
        	      area: ['893px', '600px'],
        	      content: '${ctx!}/admin/article/grant/'  + id,
        	      end: function(index){
        	    	  $('#table_list').bootstrapTable("refresh");
       	    	  }
        	    });
        }
        function del(id){
        	layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
        		$.ajax({
    	    		   type: "POST",
    	    		   dataType: "json",
    	    		   url: "${ctx!}/admin/article/delete/" + id,
    	    		   success: function(msg){
	 	   	    			layer.msg(msg.message, {time: 2000},function(){
	 	   	    				$('#table_list').bootstrapTable("refresh");
	 	   	    				layer.close(index);
	 	   					});
    	    		   }
    	    	});
       		});
        }

        function detailFormatter(index, row) {
	        var html = [];
	        html.push('<p><b>描述:</b> ' + row.remark + '</p>');
	        return html.join('');
	    }
    </script>




</body>

</html>
