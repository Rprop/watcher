var decideSelectHTML = "",firstRiskUserModelHTML = "";
var userModels = {},decideModels = {};
var userModelNum = 1;
var levelSelect,levelSelectVal = 0;

function init(userModelIds,decideIdValue,processTypeVaule){
    makeData();
    $("#riskUserModelDiv").text("");
    var orderTypeSelect = makeSelectBoxIt("orderType");
    levelSelect = makeSelectBoxIt("level");
    var executeRuleSelect = makeSelectBoxIt("executeRule");
    executeRuleSelect.change(function(){
        executeRuleCange();
    });
    executeRuleCange();
    orderTypeSelect.change(function(){
        orderTypeChange("","","");
    });
    decideSelectHTML = $("#decideDiv").html();
    firstRiskUserModelHTML = $("#firstRiskUserModel").html();
    orderTypeChange(userModelIds,decideIdValue,processTypeVaule);
    jQuery.validator.addMethod("isSelectBox", function(value, element) {
        if(value!=-1){
            return true;
        }
        return false;
    }, "请选择!");
    $("#effectForm").validate({
        errorElement: 'span',
        errorClass: 'validate-has-error',
        meta: "validate",
        ignore: "",//隐藏域验证
        highlight: function (element) {
            $(element).closest('.form-group').addClass('validate-has-error');
        },
        unhighlight: function (element) {
            $(element).closest('.form-group').removeClass('validate-has-error');
        },
        submitHandler: function(form){
            save();
        }
    });
    $("input[name='releaseType']").click(function(){
        changeReleaseType($(this).val(),true);
    });
    levelSelectVal = $("#level").val();
    changeReleaseType($("input[name='releaseType']:checked").val(),false);
}
function changeReleaseType(value,flag){
    var level = 0;
    if(value==2){
        levelSelectVal = $("#level").val();
        levelSelect.selectBoxIt("selectOption","0").selectBoxIt("disable");
    }else{
        levelSelect.selectBoxIt("selectOption",levelSelectVal).selectBoxIt("enable");
        level = levelSelectVal;
    }
    $("#level").attr("disabled",false);
    if(flag){
        $("#levelTip").text("注意:风险程度指数为"+level).show();
        setTimeout(function(){
            $("#levelTip").hide();
        },1000);
    }
}
function makeData(){

}
function save(){
    if($("#executeRule").val()=="-1"){
        showWarnWin("请选择生效规则");
        return false;
    }
    var userModel = $("#userModelSelectF").val();
    if(userModel == "" || userModel=="-1"){
        showWarnWin("请选择用户模型");
        return false;
    }
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();
    if(startTime ==""){
        showWarnWin("生效开始时间不能为空");
        return false;
    }
    if( endTime== ""){
        showWarnWin("生效结束时间不能为空");
        return false;
    }
    var beDate = new Date(startTime.replace(/-/g,"/"));
    var endDate = new Date(endTime.replace(/-/g,"/"));
    if (beDate > endDate) {
        showWarnWin("生效开始时间不能大于结束时间");
        return;

    }
    var flag = true;
    $.each($("#riskUserModelDiv select"), function(){
        var value = $(this).val();
        if(value == "" || value=="-1"){
            showWarnWin("还有用户模型没有选择");
            flag = false;
            return false;
        }
        if(userModel == value){
            showWarnWin("用户模型不能重复选择");
            flag = false;
            return false;
        }
        userModel += ","+value;
    });
    if(!flag){
        return false;
    }
    if($("#decideId").val()<0){
        showWarnWin("请选择判定模型");
        return false;
    }
    $("#userModelIds").val(userModel);
    $("#saveButton").attr("disable",true).val("保存中…");
    var url = '/man/riskEffectRule/save';
    if($("#effectId").val()>0){
        url = '/man/riskEffectRule/update';
    }
    $.post(url,
        $("#effectForm").serialize(),
        function (data){
            $("#saveButton").attr("disable",false).val("保存");
            if(data.success){
                window.location.href = "/man/riskEffectRule/list";
            }else{
                showWarnWin(data.mes);
            }
        }
    );
}

function strToDate(str,defDate) {try{var dateStrs = str.split("-");var year = parseInt(dateStrs[0], 10);var month = parseInt(dateStrs[1], 10) - 1;var day = parseInt(dateStrs[2], 10);var date = new Date(year, month, day);return date;}catch(e) {return defDate;}}
//订单类型改变 同步用户下拉和规则下拉
function orderTypeChange(userModelIds,decideIdValue,processTypeVaule){
    var orderType = $("#orderType").val();
    $("#decideDiv").html(decideSelectHTML);
    $("#firstRiskUserModel").html(firstRiskUserModelHTML);
    $("#riskUserModelDiv").html("");
    $("#translateDiv").hide();
    var firstUserModel = $("#userModelSelectF");
    var decideId = $("#decideId");
    $("#riskUserModelDiv").text("");

    var processType = $("#processType");
    if(orderType > 0){
        if(userModels[orderType]){
            var box = makeSelectData(firstUserModel,userModels[orderType],"modelName");
            box.change(function(){
                translate();
            });
        }else{
            $.post("/man/riskEffectRule/getUserModels/"+orderType,
                function (data){
                    userModels[orderType] = data;
                    var box = makeSelectData(firstUserModel,data,"modelName");
                    box.change(function(){
                        translate();
                    });
                    initUserModel(box,userModelIds);
                }
            );
        }
        if(decideModels[orderType]){
            var select = makeSelectData(decideId,decideModels[orderType],"decideName");
            select.change(function(){
                translate();
            });
        }else{
            $.post("/man/riskEffectRule/getRiskRiskDecideModels/"+orderType,
                function (data){
                    decideModels[orderType] = data;
                    var select = makeSelectData(decideId,data,"decideName");
                    select.change(function(){
                        translate();
                    });
                    if(decideIdValue && decideIdValue!=""){
                        select.selectBoxIt("selectOption",decideIdValue);
                    }
                }
            );
        }

        $.post("/man/processType/ajaxProcessType/"+orderType,
            function (data){
                var select = makeSelectData(processType,data,"name");
                if(processTypeVaule && processTypeVaule!=""){
                    select.selectBoxIt("selectOption",processTypeVaule);
                }
            }
        );
    }else{
        makeSelectBox(firstUserModel);
        makeSelectBox(decideId);
    }
}
function initUserModel(box,userModelIds){
    if(userModelIds && userModelIds!=""){
        var ids = userModelIds.split(",");
        //box.selectBoxIt("refresh");
        box.selectBoxIt("selectOption",ids[0]);
        for(var i=1;i<ids.length;i++){
            box = addUserModel(true);
            if(box){
                box.selectBoxIt("selectOption",ids[i]);
            }
        }
    }
}
////初始化下拉列表
//function makeUserSelectData(select,data){
//    if(data && data.length>0){
//        for(var i=0;i<data.length;i++){
//            $("<option></option>").attr("value",data[i].id).text(data[i].modelName).appendTo(select);
//        }
//        return makeSelectBox(select);
//    }else{
//        return makeSelectBox(select);
//    }
//}
////初始化下拉列表
//function makeDecideSelectData(select,data){
//    if(data && data.length>0){
//        for(var i=0;i<data.length;i++){
//            $("<option></option>").attr("value",data[i].id).text(data[i].decideName).appendTo(select);
//        }
//        return makeSelectBox(select);
//    }else{
//        return makeSelectBox(select);
//    }
//}


//初始化下拉列表
function makeSelectData(select,data,valueName){
    if(data && data.length>0){
        for(var i=0;i<data.length;i++){
            $("<option></option>").attr("value",data[i].id).text(data[i][valueName]).appendTo(select);
        }
        return makeSelectBox(select);
    }else{
        return makeSelectBox(select);
    }
}


//添加用户模型
function addUserModel(r){
    var orderType = $("#orderType").val();
    if(orderType>0){
        userModelNum++;
        var userTem = new jsTempo('userTem');
        $("<div></div>").attr("id","user_div_"+userModelNum).appendTo($("#riskUserModelDiv"));
        $("#user_div_"+userModelNum).html(userTem.set({id:userModelNum}));
        var sel = makeSelectData($("#user_select_"+userModelNum),userModels[orderType],"modelName");
        sel.change(function(){
            translate();
        });
        $("#addUserModelButton").hide();
        if(r){
            return sel;
        }
    }else{
        showWarnWin("请先选择订单类型");
        if(r){
            return false;
        }
    }
}
//删除用户模型
function delDecide(id){
    $("#user_div_"+id).remove();
    $("#addUserModelButton").show();
}
/**区域相关  */

function showRegion(){
    var html = $("#region_div").html();
    if(html!=""){
        $('#modal-region .modal-body').html(html);
        $("#region_div").html("");
        var regionVal = $("#region").val();
        makeRegion(regionVal);
    }
    $("#modal-region").modal('show', {backdrop: 'static'});
}
function saveRegion(){
    var value = getRegionStr();
    $("#region").val(value);
    $("#region_only").val(value);
    $('#modal-region').modal('hide');
}
var regions = [
   {name:"华东地区",value:"华东地区",provinces:[{name:"江苏",value:"江苏"},{name:"安徽",value:"安徽"},
         {name:"浙江",value:"浙江"},{name:"福建",value:"福建"},{name:"上海",value:"上海"},{name:"广西",value:"广西"}]},
    {name:"华南地区",value:"华南地区",provinces:[{name:"广西",value:"广西"},{name:"广东",value:"广东"},{name:"海南",value:"海南"}]},
    {name:"华中地区",value:"华中地区",provinces:[{name:"湖北",value:"湖北"},{name:"湖南",value:"湖南"},{name:"河南",value:"河南"}]},
    {name:"华北地区",value:"华北地区",provinces:[{name:"北京",value:"北京"},{name:"天津",value:"天津"},{name:"河北",value:"河北"}
        ,{name:"山西",value:"山西"},{name:"内蒙古",value:"内蒙古"}]},
    {name:"西北地区",value:"西北地区",provinces:[{name:"宁夏",value:"宁夏"},{name:"新疆",value:"新疆"},{name:"青海",value:"青海"}
        ,{name:"陕西",value:"陕西"},{name:"甘肃",value:"甘肃"}]},
    {name:"西南地区",value:"西南地区",provinces:[{name:"四川",value:"四川"},{name:"云南",value:"云南"},{name:"贵州",value:"贵州"}
        ,{name:"西藏",value:"西藏"},{name:"重庆",value:"重庆"}]},
    {name:"东北地区",value:"东北地区",provinces:[{name:"辽宁",value:"辽宁"},{name:"吉林",value:"吉林"},{name:"黑龙江",value:"黑龙江"}]},
    {name:"港澳台地区",value:"港澳台地区",provinces:[{name:"台湾",value:"台湾"},{name:"香港",value:"香港"},{name:"澳门",value:"澳门"}]}
];
function makeRegion(regionVal){
    var region_list = $("#region_list");
    var areaTem =  new jsTempo('areaTem');
    var provinceTem = new jsTempo('provinceTem');
    for(var i=0;i<regions.length;i++){
        var tr = $("<tr></tr>").appendTo(region_list);;
        var region = regions[i];
        region.id= i;
        $(areaTem.set(region)).appendTo(tr);
        if(region.provinces && region.provinces.length>0){
            var td = $("<td></td>").attr("id","proviece_"+i).appendTo(tr);
            for(var j=0;j<region.provinces.length;j++){
                region.provinces[j].id= i;
                $(provinceTem.set(region.provinces[j])).appendTo(td);
            }
        }
    }
    if(regionVal && regionVal!=""){
        var regionArr = regionVal.split(",");
        for(var i=0;i<regionArr.length;i++){
            region_list.find("input[value='"+regionArr[i]+"']").prop("checked",true);
        }
    }

}
function areaCheckClick(obj,id){
    if($(obj).is(':checked')){
        $.each( $("#proviece_"+id).find("input[type='checkbox']"), function(){
            $(this).prop("checked",true);
        });
    }else{
        $.each( $("#proviece_"+id).find("input[type='checkbox']"), function(){
            $(this).prop("checked",false);
        });
    }
}
function proviceCheckClick(id){
    var check = false;
    $.each( $("#proviece_"+id).find("input[type='checkbox']"), function(){
        if($(this).is(':checked')){
            check = true;
            return false;
        }
    });
    if(check){
        $("#area_checkbox_"+id).prop("checked",true);
    }else{
        $("#area_checkbox_"+id).prop("checked",false);
    }
}
function getRegionStr(){
    var str = "";
    $.each( $("#region_list").find("input[type='checkbox']"), function(){
        if($(this).is(':checked')){
            str += $(this).val()+",";
        }
    });
    if(str.length>0){
        str = str.substring(0,str.length-1);
    }
    return str;
}
/**区域相关 end */
/** 白话文翻译*/
var userModelTraJson = {};
function getUserModelTranslate(id){
    if(!userModelTraJson[id]){
        $.ajax({
            url : "/man/riskUserModel/translate/"+id,
            async:false,
            success : function(data){
                if(data && data!=""){
                    userModelTraJson[id] = data;
                }
            }
        });
    }
    return userModelTraJson[id];
}
function getUserModelTranslateStr(id){
    var html ="";
    var json = getUserModelTranslate(id);
    if(json && json!=""){
        try{
            var rule = eval(json);
            for(var i=0;i<rule.length;i++){
                if(i==0){
                    html+="<p>"+rule[i]+"</p>";
                }else{
                    html+="<p>且   "+rule[i]+"</p>";
                }
            }
        }catch(e){}
    }
    return html;
}

var decideTraJson = {};
function getDecideTranslate(id){
    if(!decideTraJson[id]){
        $.ajax({
            url : "/man/riskDecideModel/translate/"+id,
            async:false,
            success : function(data){
                if(data && data!=""){
                    decideTraJson[id] = data;
                }
            }
        });
    }
    return decideTraJson[id];
}
function getDecideTranslateStr(id){
    var html ="";
    var json = getDecideTranslate(id);
    if(json && json!=""){
        try{
            var rule = eval(json);
            for(var i=0;i<rule.length;i++){
                if(i==0){
                    html+="<p>"+rule[i]+"</p>";
                }else{
                    html+="<p>且   "+rule[i]+"</p>";
                }
            }
        }catch(e){}
    }
    return html;
}

function translate(){
    var orderType = $("#orderType").val();
    if(!orderType && orderType <0){
        $("#translateDiv").hide();
        return false;
    }
    $("#uTranslateDiv").html("");
    $("#dTranslateDiv").html("");
    var isShow = false;
    var uhtml = "";
    var umId = $("#userModelSelectF").val();
    if(umId > 0){
        var uStr = getUserModelTranslateStr(umId);
        if(uStr!=""){
            uhtml += uStr;
            isShow = true;
        }
    }
    $.each($("#riskUserModelDiv select"), function(){
        umId = $(this).val();
        if(umId > 0){
            var uStr = getUserModelTranslateStr(umId);
            if(uStr!=""){
                if(isShow){
                    uStr = "<p>或</p>" + uStr;
                }
                uhtml +=  uStr;
                isShow = true;
            }
        }
    });
    var decideId = $("#decideId").val();
    if(decideId >0){
        var dStr = getDecideTranslateStr(decideId);
        if(dStr!=""){
            $("#dTranslateDiv").html(dStr);
            isShow = true;
        }
    }
    if(isShow){
        if(uhtml!="" && uhtml.length>4){
            $("#uTranslateDiv").html(uhtml.substring(0,uhtml.length-4)+"  时</p>");
        }
        $("#translateDiv").show();
    }else{
        $("#translateDiv").hide();
    }
}
function executeRuleCange(){
    $("#executeTranslateDiv").html("则  " + $("#executeRule option:selected").text());
}
/** 白话文翻译 end*/

function toAdd(){
    window.location.href = "add.html";
}
function toEdit(id){
    window.location.href = "/man/riskEffectRule/edit/"+id;
}