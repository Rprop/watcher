var jnrdm_defin = $("#jnrdm-defin");
var ruleDiv = $("#ruleDiv");
function initAddOrEdit(){
    var userModelCheck = new jsTempo('userModelCheck');
    makeSelectBoxIt("businessTypeId");
    jnrdm_defin.html("");
    var size = userModelsJSONARR.length;
    for(var i=0;i<size;i++){
        jnrdm_defin.html(jnrdm_defin.html()+userModelCheck.set(userModelsJSONARR[i]));
    }
    setRuleHTML(userModelsJSONARR,ruleDiv);
    $.each( jnrdm_defin.find("input[type='checkbox']"), function(i, obj){
        $(obj).click(function(){
            ruleCheckChange(i,jnrdm_defin,ruleDiv,userModelsJSONARR);
        });
    });
    jQuery.validator.addMethod("isSelectBox", function(value, element) {
        if(value!=-1){
            return true;
        }
        return false;
    }, "请选择!");
    $("#userModelForm").validate({
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
function toAdd(){
    window.location.href = "add.html";
}
function toEdit(id){
    window.location.href = "/man/riskUserModel/edit/"+id;
}
function save(){
    $("#saveButton").attr("disable",true).val("保存中…");
    var url = '/man/riskUserModel/save';
    if($("#riskUserId").val()>0){
        url = '/man/riskUserModel/update';
    }
    $.post(url,
        $("#userModelForm").serialize(),
        function (data){
            $("#saveButton").attr("disable",false).val("保存");
            if(data.success){
                window.location.href = "/man/riskUserModel/list";
            }else{
                showWarnWin(data.mes);
            }
        }
    );
}
var ruleDataNow = "";
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
    ruleDataNow = $("#ipDiv").html();
    $("#ipDiv").html("");
    $("#ipCheckbox").removeAttr("checked").click(function(){
        var ipDiv = $("#ipDiv");
        var ruleDiv = $("#ruleDiv");
        if($(this).is(':checked')){
            ruleDiv.hide();
            ruleDiv.find("input").attr('disabled', 'disabled');
            ruleDiv.find("select").attr('disabled', 'disabled');
            ipDiv.html(ruleDataNow).show();
            $.each($("#jnrdm-defin").find("input[type='checkbox']"), function(i, obj){
                $(this).attr('disabled', 'disabled');
            });
            $("#type").val(2);
        }else{
            ipDiv.html("").hide();
            ruleDiv.show();
            ruleDiv.find("input").removeAttr('disabled');
            ruleDiv.find("select").removeAttr('disabled');
            $.each($("#jnrdm-defin").find("input[type='checkbox']"), function(i, obj){
                $(this).removeAttr('disabled');
            });
            $("#type").val(1);
        }
    });
});
function showWarnWin(mes){
    $("#modal-warn").modal('show', {backdrop: 'static'});
    jQuery('#modal-warn .modal-body').html(mes);
}
