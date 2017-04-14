<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> - 表单验证 jQuery Validation</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/hadmin/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/font-awesome.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/hadmin/css/style.css?v=${version}" rel="stylesheet">

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
      <#--  <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>jQuery Validate 简介</h5>
                    </div>
                    <div class="ibox-content">
                        <p>jquery.validate.js 是一款优秀的jQuery表单验证插件。它具有如下特点：</p>
                    </div>
                </div>
            </div>
        </div>-->
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>完整验证表单</h5>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal m-t" id="frm" method="post" action="">
                        	<input type="hidden" id="id" name="id" value="${articleSort.id}">

                            <div class="form-group">
                                <label class="col-sm-3 control-label">名称：</label>
                                <div class="col-sm-9">
                                    <input id="title" name="title" class="form-control" type="text" value="${articleSort.title}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">描述：</label>
                                <div class="col-sm-9" >
                                    <input class="form-control" type="text" id="description" name="description" value=" ${articleSort.description}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">状态：</label>
                                <div class="col-sm-9">
                                	<select name="status" class="form-control">
                                		<option value="0" <#if articleSort.status == 0>selected="selected"</#if>>无效</option>
                                		<option value="1" <#if articleSort.status == 1>selected="selected"</#if>>有效</option>
                                	</select>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-sm-8 col-sm-offset-3">
                                    <button class="btn btn-primary" type="submit">提交</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <!-- 全局js -->
    <#include "/admin/common/common.ftl">
    <script type="text/javascript">
    $(document).ready(function () {
	  	//外部js调用
	  /*  laydate({
	        elem: '#birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	        event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	    });*/

	    $("#frm").validate({
    	    rules: {
    	    	title: {
    	        required: true,
    	        minlength: 1,
    	    	maxlength: 30
    	      },
    	      	status: {
    	        required: true
    	      },
    	      	description: {
    	        required: true,
    	        minlength: 1
    	      }
    	    },
    	    messages: {
                title:{
                    required:"名称必填"
                }
            },
    	    submitHandler:function(form){
    	    	$.ajax({
   	    		   type: "POST",
   	    		   dataType: "json",
   	    		   url: "${ctx!}/admin/article/sort/edit",
   	    		   data: $(form).serialize(),
   	    		   success: function(msg){
	   	    			layer.msg(msg.message, {time: 2000},function(){
	   						var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	   						parent.layer.close(index);
	   					});
   	    		   }
   	    		});
            }
    	});
    });
    </script>

</body>

</html>
