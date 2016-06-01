function makeSelectBoxIt(id){return makeSelectBox($("#"+id))}
function makeSelectBox(obj){return obj.selectBoxIt({showEffect: 'fadeIn',hideEffect: 'fadeOut'});}
function jsTempo(id){this.dom=document.getElementById(id);this._parse=this._buildParseFunc(this.dom.innerHTML)}jsTempo.prototype._buildParseFunc=function(html){html=html.replace(/([\'|\\])/gm,"\\$1").replace(new RegExp("{([^{}]*)}","gim"),"'+(typeof data[\"$1\"]!='undefined' ? data[\"$1\"] : '')+'").replace(/[\n\r]/gm," ");html=["return '",html,"';"].join("");return new Function("data",html)};jsTempo.prototype.set=function(data){return this._parse(data)};jsTempo.prototype.setArray=function(arr){for(var i=0,len=arr.length;i<len;i++){arr[i]=this._parse(arr[i])}return this.arrToString(arr)};jsTempo.prototype.setMap=function(map){for(var i in map){map[i]=this._parse(map[i])}return map};jsTempo.prototype.arrToString=function(arr){var s="";for(var i=0;i<arr.length;i++){s+=arr[i]}return s};
function ajaxDefaultError(jqXHR, textStatus, errorThrown){
    switch (jqXHR.status){
        case(500):
            showWarnWin("系统错误!");
            break;
        case(401):
            showWarnWin("未登陆!");
            break;
        case(403):
            showWarnWin("无权限执行此操作");
            break;
        case(408):
            showWarnWin("系统错误!");
            break;
    }
}
function showWarnWin(mes){
    $("#modal-warn").modal('show', {backdrop: 'static'});
    jQuery('#modal-warn .modal-body').html(mes);
}
//验证规则
function checkIsNormal(value){
    return   /^([\u4e00-\u9fa5a-zA-Z0-9_-]*)$/.test(value);
}
function checkIsPositiveInteger(value){
    return   /(^[1-9][0-9]*$)|(^[0]$)/.test(value);
}
function checkIsDigits(value){
    return   /(^[1-9][0-9]*$)/.test(value);
}
function checkIsNormalPunctuation (value){
    return   /^([\u4e00-\u9fa5a-zA-Z0-9_。，！]*)$/.test(value);
}
$(function(){
    jQuery.validator.addMethod("isNormal", function(value, element) {
        return this.optional(element) || checkIsNormal(value);
    }, "只能由中文、英文字母、数字和下划线组成!");
    jQuery.validator.addMethod("isInteger", function(value, element) {
        return this.optional(element) || checkIsPositiveInteger(value);
    }, "只能正整数和零!");
    jQuery.validator.addMethod("isNormalPunctuation", function(value, element) {
        return this.optional(element) || checkIsNormalPunctuation(value);
    }, "只能由中英文及基本标点符号组成!");
    jQuery.validator.addMethod("isDigits", function(value, element) {
        return this.optional(element) || checkIsDigits(value);
    }, "只能正整数!");
});