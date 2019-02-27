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
	$("#upload-target").on("click",function(){
        $(this).next().trigger("click")
	})

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
		distribution: {
            activityRules : [],
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.distribution = {
                activityRules : [],
			};
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

            var data = Object.assign({},vm.distribution);
            if(vm.distribution.activityRules){
                data.activityRules = JSON.stringify(vm.distribution.activityRules);
			}
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(data),
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
        send: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "/distribution/send",
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
				if(r.distribution.activityRules){
                    r.distribution.activityRules = JSON.parse(r.distribution.activityRules);
				}else{
                    r.distribution.activityRules = [];
				}
                vm.distribution = r.distribution;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        uploadActivityRules : function(e){
			var file = e.target.files[0];
            myUpload(baseURL + 'distribution/upload',file,function(data){
                vm.distribution.activityRules = vm.distribution.activityRules || [];
                vm.distribution.activityRules.push({
                    "type":"uploadImg",
                    "img": data
				})
                e.target.value = "";
			},function(){
                e.target.value = "";
			});
		},
        delActivityRulesUpload : function (index) {
            vm.distribution.activityRules = vm.distribution.activityRules || [];
            vm.distribution.activityRules.splice(index,1);
        }
	}
});

function log(msg) {
    document.getElementById("log").innerHTML += (msg != undefined ? msg : "") + "<br />";
}
