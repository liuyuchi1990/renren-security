$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'distribution/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true ,hidden:true},
			{ label: '活动名称', name: 'activityName', index: 'activity_name', width: 80 },
			{ label: '活动类别', name: 'activityType', index: 'activity_type', width: 80 },
			{ label: '活动开始时间', name: 'startTime', index: 'start_time', width: 80 },
			{ label: '活动结束时间', name: 'endTime', index: 'end_time', width: 80 },
			{ label: '修改人', name: 'updateUser', index: 'update_user', width: 80 },
			{ label: '修改时间', name: 'updateTime', index: 'update_time', width: 80 },
			{ label: '活动状态', name: 'activityState', index: 'activity_state', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        autoScroll: true,
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

$(function () {
    $(".datepicker").datetimepicker({
        autoclose: true,//选中之后自动隐藏日期选择框
        clearBtn: true,//清除按钮
        todayBtn: true,//今日按钮
        minuteStep:1,
        format: 'yyyy-mm-dd hh:ii:ss',
        minView: 0,
        minuteStep:1
    });
});
var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		distribution: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.distribution = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.distribution.id == null ? "distribution/save" : "distribution/update";
			vm.distribution.startTime = $('#startTime').val();
            vm.distribution.endTime = $('#endTime').val();
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.distribution),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "/distribution/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get(baseURL + "distribution/info/"+id, function(r){
                vm.distribution = r.distribution;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});

function log(msg) {
    document.getElementById("log").innerHTML += (msg != undefined ? msg : "") + "<br />";
}

var Uploader = Q.Uploader,
    formatSize = Q.formatSize,
    boxView = document.getElementById("upload-image-view");

var uploader = new Uploader({
    url: baseURL +"/distribution/upload",
    target: document.getElementById("upload-target"),
    view: boxView,

    //将auto配置为false以手动上传
    auto: false,

    allows: ".jpg,.png,.gif,.bmp",

    //图片缩放
    scale: {
        //要缩放的图片格式
        types: ".jpg",
        //最大图片大小(width|height)
        maxWidth: 1024
    },

    on: {
        //添加之前触发
        add: function (task) {
            if (task.disabled) return alert("允许上传的文件格式为：" + this.ops.allows);
        },
        //图片预览后触发
        preview: function (data) {
            //log(data.task.name + " : " + data.src);
        },
        //图片压缩后触发,如果图片或浏览器不支持压缩,则不触发
        scale: function (data) {
            //log(data.task.name + " : 已压缩！");
        },
        upload: function (task) {
            //log(task.name + " : 开始上传");
        },
        remove: function (task) {
            //log(task.name + " : 已移除！");
        }
    }
});

//将auto配置为false以手动上传
//uploader.start();

document.getElementById("start-upload").onclick = function () {
    uploader.start();
};