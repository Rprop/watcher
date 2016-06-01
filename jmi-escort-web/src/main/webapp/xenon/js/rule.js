function jsTempo(id){this.dom=document.getElementById(id);this._parse=this._buildParseFunc(this.dom.innerHTML)}jsTempo.prototype._buildParseFunc=function(html){html=html.replace(/([\'|\\])/gm,"\\$1").replace(new RegExp("{([^{}]*)}","gim"),"'+(typeof data[\"$1\"]!='undefined' ? data[\"$1\"] : '')+'").replace(/[\n\r]/gm," ");html=["return '",html,"';"].join("");return new Function("data",html)};jsTempo.prototype.set=function(data){return this._parse(data)};jsTempo.prototype.setArray=function(arr){for(var i=0,len=arr.length;i<len;i++){arr[i]=this._parse(arr[i])}return this.arrToString(arr)};jsTempo.prototype.setMap=function(map){for(var i in map){map[i]=this._parse(map[i])}return map};jsTempo.prototype.arrToString=function(arr){var s="";for(var i=0;i<arr.length;i++){s+=arr[i]}return s};
function makeSelectBoxIt(id){return makeSelectBox($("#"+id))}
function makeSelectBox(obj){return obj.selectBoxIt({showEffect: 'fadeIn',hideEffect: 'fadeOut'});}
function ruleCheckChange(index,checkDiv,ruleDiv,datas){
    var dataArr = [];
    $.each( checkDiv.find("input[type='checkbox']"), function(i,obj){
        var id = $(this).attr("for");
        if($(this).is(':checked')){
            var data = datas[i];
            if(i!=index){
                data.operator = $("#"+id+"_operator").val();
                data.expectValue = $("#"+id+"_expectValue").val();
            }
            dataArr.push(data);
        }
    });
    setRuleHTML(dataArr,ruleDiv);
}
function setRuleHTML(datas,container){
    container.html("").hide();
    var size = datas.length;
    for(var i=0;i<size;i++){
        var data = datas[i];
        if(data.tem && data.tem=='operator'){
            container.html(container.html()+new jsTempo("operateTem").set(data));
        }else if($("#"+data.id+"Tem").length == 0){
            console.error("Tem模板不存在",data.id);
            continue;
        }else{
            container.html(container.html()+new jsTempo(data.id+"Tem").set(data));
        }
        if(i+1!=size){
            container.html(container.html()+'<div class="div-and">且</div>');
        }
    }
    for(var i=0;i<size;i++){
        var data = datas[i];
        if(data.operator && data.operator!=''){
            $("#"+data.id+"_operator").val(data.operator);
        }
        if(data.expectValue && data.expectValue!=''){
            $("#"+data.id+"_expectValue").val(data.expectValue);
        }
    }
    $.each( container.find("select"), function(i, obj){
        var selectBox = makeSelectBox($(obj));
    });
    container.show();
}

function mergeData(data,value){
    for(var i=0;i<data.length;i++){
        for(var j=0;j<value.length;j++){
            if(data[i].cName == value[j].conditionName){
                if(value[j].operator && value[j].operator!=''){
                    data[i].operator = value[j].operator;
                }
                if(value[j].expectValue && value[j].expectValue!=''){
                    data[i].expectValue = value[j].expectValue;
                }
            }
        }
    }
    return data;
}
function makeArr(str){
    var rule = [];
    try{
        rule = eval(str);
    }catch(e){}
    if($.type(rule) != "array"){
        rule = [];
    }
    return rule;
}
function initEdit(data,checkDiv,ruleDiv){
    for(var i=0;i<data.length;i++){
        if(data[i].operator && data[i].expectValue){
            $("#"+data[i].id+"_checkbox").attr("checked",true);
        }else{
            $("#"+data[i].id+"_checkbox").removeAttr("checked");
        }
    }
    ruleCheckChange(-1,checkDiv,ruleDiv,data)

}


//验证规则
function checkIsNormal(value){
    return   /^([\u4e00-\u9fa5a-zA-Z0-9_]*)$/.test(value);
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
function checkIsMoreNumber(value){
    if(!value || value == ""){
        return false;
    }
    var vArr = value.split(",");
    for(var i=0;i<vArr.length;i++){
        if(!checkIsDigits(vArr[i])){
            return false;
        }
    }
    return true;

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
