var baseDecideMenus = $("#baseDecideMenus");
var baseDecideContext = $("#baseDecideContext");
var serviceDecideMenus = $("#serviceDecideMenus");
var serviceDecideContext = $("#serviceDecideContext");
var menusCheck = new jsTempo('menusCheck');
function initAddOrEdit(){
    var decideSelectBox = makeSelectBoxIt("decideTypeId");
    baseDecideMenus.html("");
    var size = baseDecideModes.length;
    for(var i=0;i<size;i++){
        baseDecideMenus.html(baseDecideMenus.html()+menusCheck.set(baseDecideModes[i]));
    }
    setRuleHTML(baseDecideModes,baseDecideContext);
    makeServiceMenus();
    decideSelectBox.change(function(){
        makeServiceMenus();
    });
    $.each( baseDecideMenus.find("input[type='checkbox']"), function(i, obj){
        $(obj).click(function(){
            ruleCheckChange(i,baseDecideMenus,baseDecideContext,baseDecideModes);
        });
    });


    jQuery.validator.addMethod("isSelectBox", function(value, element) {
        if(value!=-1){
            return true;
        }
        return false;
    }, "请选择!");
    $("#decideForm").validate({
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
}
function makeServiceMenus(){
    serviceDecideMenus.html("");
    var serviceMenus = getServiceMenus();
    var size = serviceMenus.length;
    for(var i=0;i<size;i++){
        serviceDecideMenus.html(serviceDecideMenus.html()+menusCheck.set(serviceMenus[i]));
    }
    setRuleHTML(serviceMenus,serviceDecideContext);
    $.each( serviceDecideMenus.find("input[type='checkbox']"), function(i, obj){
        $(obj).click(function(){
            ruleCheckChange(i,serviceDecideMenus,serviceDecideContext,serviceMenus);
        });
    });
}
function getServiceMenus(){
    var decideTypeId = $("#decideTypeId").val();
    var menus = [];
    for(var i =0 ;i <oftenDecideModes.length;i++ ){
        var name = oftenDecideModes[i];
        var rule = allServiceDecideModes[name];
        rule.id = name;
        menus.push(rule);
    }
    var modes = serviceDecideModes[decideTypeId];
    if(modes && modes.length >0){
        for(var i =0 ;i <modes.length;i++ ){
            var name = modes[i];
            var rule = allServiceDecideModes[name];
            rule.id = name;
            menus.push(rule);
        }
    }
    return menus;
}
function toAdd(){
    window.location.href = "add.html";
}
function toEdit(id){
    window.location.href = "/man/riskDecideModel/edit/"+id;
}
function save(){
    $("#saveButton").attr("disable",true).val("保存中…");
    var url = '/man/riskDecideModel/save';
    if($("#decideId").val()>0){
        url = '/man/riskDecideModel/update';
    }
    $.post(url,
        $("#decideForm").serialize(),
        function (data){
            $("#saveButton").attr("disable",false).val("保存");
            if(data.success){
                window.location.href = "/man/riskDecideModel/list";
            }else{
                showWarnWin(data.mes);
            }
        }
    );
}
$(function(){
    $.ajaxSetup({
        error: function(jqXHR, textStatus, errorThrown){
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
                default:
                    showWarnWin("未知错误");
            }
            $("#saveButton").attr("disable",false).val("保存");
        }
    });
});
function showWarnWin(mes){
    $("#modal-warn").modal('show', {backdrop: 'static'});
    jQuery('#modal-warn .modal-body').html(mes);
}
