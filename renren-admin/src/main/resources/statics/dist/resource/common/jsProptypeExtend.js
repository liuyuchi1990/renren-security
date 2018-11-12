Date.prototype.format = function(format){
    var o = {
        "M+" : (this.getMonth()+1)<10?'0'+(this.getMonth()+1):(this.getMonth()+1), //month
        "d+" : this.getDate()<10?'0'+this.getDate():this.getDate(),    //day
        "h+" : this.getHours()<10?'0'+this.getHours():this.getHours(),   //hour
        "H+" : this.getHours()<10?'0'+this.getHours():this.getHours(),   //hour
        "m+" : this.getMinutes()<10?'0'+this.getMinutes():this.getMinutes(), //minute
        "s+" : this.getSeconds()<10?'0'+this.getSeconds():this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
        "S" : this.getMilliseconds()<10?'0'+this.getMilliseconds():this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format)){
        format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o){
        if(new RegExp("("+ k +")").test(format)){
            format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
        }
    }
    return format;
}
