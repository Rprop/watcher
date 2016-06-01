function initAddOrEdit(){
    jQuery.validator.addMethod("isSelectBox", function(value, element) {
        if(value!=-1){
            return true;
        }
        return false;
    }, "请选择!");
    jQuery.validator.addMethod("maxValue", function(value, element) {
        return this.optional(element) || checkMaxValue(value,200);
    }, "触发规则次数不能大于200!");
    $("#blackSyncRuleForm").validate({
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
function checkMaxValue(value,maxValue){
    return   value <= maxValue;
}
function toAdd(){
    window.location.href = "add.html";
}
function toEdit(id){
    window.location.href = "/man/blackSyncRule/edit/"+id;
}
function save(){
    $("#saveButton").attr("disable",true).val("保存中…");
    var url = '/man/blackSyncRule/save';
    if($("#syncRuleId").val()>0){
        url = '/man/blackSyncRule/update';
    }
    $.post(url,
        $("#blackSyncRuleForm").serialize(),
        function (data){
            $("#saveButton").attr("disable",false).val("保存");
            if(data.success){
                window.location.href = "/man/blackSyncRule/list";
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
