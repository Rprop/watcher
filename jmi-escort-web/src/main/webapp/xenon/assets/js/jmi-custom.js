/**
 * sku组合逻辑
 */

/**
 *销售属性对象
 * @param caption
 * @param pid
 * @constructor
 */
function JmiSellProp(pid, caption, need) {
    this.pid = pid;
    this.caption = caption;
    this.need = need;
}

/**
 *当前销售属性的值
 * @param vid
 * @param title
 * @constructor
 */
function JmiSellPropValue(id, value, title) {
    this.id = id;
    this.value = value;
    this.title = title;
}

$(function () {
    //绑定label点击事件
    $("#J_SellProperties label").bind("change", function () {
        step.Creat_Table();
    });

    var step = {
        //SKU信息组合
        Creat_Table: function () {

            //合并方法
            step.mergeLine();

            //销售属性数组列表
            var sellProperties = new Array();
            //选中的销售属性值列表
            var sellPropValues = new Array();
            var skuAttrValues= new Array();
            //需要合并的列
            var arrayColumn = new Array();

            var J_SKUMapContainer =  $("#J_SKUMapContainer");
            var tableDiv = J_SKUMapContainer.find("div.col-sm-10");
            var stockNumber = $("#stockNumber");

            var bCheck = true;//是否全选
            var columnIndex = 0;

            //封装销售属性
            $("#J_SellProperties div.form-group").each(function (i, item) {
                arrayColumn.push(columnIndex);
                columnIndex++;

                //封装销售属性列表
                sellProperties.push(new JmiSellProp($(this).attr("data-pid"), $(this).attr("data-caption"), 0));

                //选中的CHeckBox取值
                var checkedArray = new Array();

                //查找当前属性下面选中的列表
                $(this).find("input[type=checkbox]:checked").each(function () {
                    checkedArray.push($(this).attr("title")+"@"+$(this).attr("value") + ":" + $(this).attr("data-type"));
                    sellPropValues.push(new JmiSellPropValue($(this).attr("id"),$(this).attr("value"),$(this).attr("title")));
                });

                skuAttrValues.push(checkedArray);

                if (checkedArray.join() == "") {
                    bCheck = false;
                }

                //清空数据 & 隐藏
                tableDiv.empty();
                J_SKUMapContainer.hide();
                stockNumber.removeAttr("disabled");
            });

            //console.log(sellPropValues);

            //开始创建Table表
            if (bCheck == true) {
                console.log(1)
                J_SKUMapContainer.show();
                stockNumber.attr("disabled",true);
                var RowsCount = 0;
                //创建表
                var table = $("<table class=\"table table-bordered table-striped\" id=\"dynamic-sku-filetable\"></table>");
                table.appendTo(tableDiv);
                var thead = $("<thead></thead>");
                thead.appendTo(table);
                var trHead = $("<tr></tr>");
                trHead.appendTo(thead);

                //创建表头
                $.each(sellProperties, function (index, item) {
                    var th = $("<th><span>" + sellProperties[index].caption + "</span></th>");
                    th.appendTo(trHead);
                });

                var itemColumHead = $("<th><span class='required'>价格</span></th><th><span class='required'>数量</span></th><th style='display:none'><span>商家编码</span></th>");
                itemColumHead.appendTo(trHead);

                var tbody = $("<tbody></tbody>");
                tbody.appendTo(table);


                //生成组合
                var data = step.doExchange(skuAttrValues);
                if (data.length > 0) {
                    //创建行
                    $.each(data, function (index, item) {
                        var td_array = item.split(",");
                        var tr = $("<tr></tr>");
                        tr.appendTo(tbody);
                        $.each(td_array, function (i, values) {
                            //选号投注-1:13
                            var skuAttrValue = values.split("@");
                            var td = $("<td class='dynamic-sku-attr' data-id='"+skuAttrValue[1]+"'>" + skuAttrValue[0] + "</td>");
                            td.appendTo(tr);
                        });

                        //<input data-id="21676-27384_8557920-20213_123390659-147706196_140744446-3231816" id="J_SkuField_price_21676-27384_8557920-20213_123390659-147706196_140744446-3231816" class="J_MapPrice text" data-type="price" type="text" value="">
                        var price = $("<td class='col-xs-2 price'><input data-type=\"price\" type=\"text\" value=\"\"></td>");
                        price.appendTo(tr);
                        var quantity = $("<td class='col-xs-2 quantity'><input  data-type=\"quantity\" type=\"text\" value=\"\"></td>");
                        quantity.appendTo(tr);
                        var tsc = $("<td class='col-xs-2 tsc' style='display:none'><input data-type=\"tsc\" type=\"text\" value=\"\"></td>");
                        tsc.appendTo(tr);
                    });
                }
                //结束创建Table表
                arrayColumn.pop();//删除数组中最后一项
                //合并单元格
                $(table).mergeCell({
                    // 目前只有cols这么一个配置项, 用数组表示列的索引,从0开始
                    cols: arrayColumn
                });

                //$(document.body).append($(table))
            }
        },

        //合并行
        mergeLine: function () {
            $.fn.mergeCell = function (options) {
                return this.each(function () {
                    var cols = options.cols;
                    for (var i = cols.length - 1; cols[i] != undefined; i--) {
                        // fixbug console调试

                        mergeCell($(this), cols[i]);
                    }
                    destroy($(this));
                });
            };

            // 如果对javascript的closure和scope概念比较清楚, 这是个插件内部使用的private方法
            function mergeCell($table, colIndex) {
                $table.data('col-content', ''); // 存放单元格内容
                $table.data('col-rowspan', 1); // 存放计算的rowspan值 默认为1
                $table.data('col-td', $()); // 存放发现的第一个与前一行比较结果不同td(jQuery封装过的), 默认一个"空"的jquery对象
                $table.data('trNum', $('tbody tr', $table).length); // 要处理表格的总行数, 用于最后一行做特殊处理时进行判断之用

                // 我们对每一行数据进行"扫面"处理 关键是定位col-td, 和其对应的rowspan
                $('tbody tr', $table).each(function (index) {
                    // td:eq中的colIndex即列索引
                    var $td = $('td:eq(' + colIndex + ')', this);
                    // 取出单元格的当前内容
                    var currentContent = $td.html();
                    // 第一次时走此分支
                    if ($table.data('col-content') == '') {
                        $table.data('col-content', currentContent);
                        $table.data('col-td', $td);
                    } else {
                        // 上一行与当前行内容相同
                        if ($table.data('col-content') == currentContent) {
                            // 上一行与当前行内容相同则col-rowspan累加, 保存新值
                            var rowspan = $table.data('col-rowspan') + 1;
                            $table.data('col-rowspan', rowspan);
                            // 值得注意的是 如果用了$td.remove()就会对其他列的处理造成影响
                            $td.hide();
                            // 最后一行的情况比较特殊一点
                            // 比如最后2行 td中的内容是一样的, 那么到最后一行就应该把此时的col-td里保存的td设置rowspan
                            if (++index == $table.data('trNum'))
                                $table.data('col-td').attr('rowspan', $table.data('col-rowspan'));
                        } else { // 上一行与当前行内容不同
                            // col-rowspan默认为1, 如果统计出的col-rowspan没有变化, 不处理
                            if ($table.data('col-rowspan') != 1) {
                                $table.data('col-td').attr('rowspan', $table.data('col-rowspan'));
                            }
                            // 保存第一次出现不同内容的td, 和其内容, 重置col-rowspan
                            $table.data('col-td', $td);
                            $table.data('col-content', $td.html());
                            $table.data('col-rowspan', 1);
                        }
                    }
                });
            }

            // 同样是个private函数 清理内存之用
            function destroy($table) {
                $table.removeData();
            }
        },

        //组合数组
        doExchange: function (doubleArrays) {
            var len = doubleArrays.length;
            if (len >= 2) {
                var arr1 = doubleArrays[0];
                var arr2 = doubleArrays[1];
                var len1 = doubleArrays[0].length;
                var len2 = doubleArrays[1].length;
                var newLen = len1 * len2;
                var temp = new Array(newLen);
                var index = 0;

                for (var i = 0; i < len1; i++) {
                    for (var j = 0; j < len2; j++) {
                        temp[index] = arr1[i] + "," + arr2[j];
                       // console.log( "===>"+arr1[i] + "," + arr2[j]);
                        index++;
                    }
                }

                var newArray = new Array(len - 1);
                newArray[0] = temp;
                if (len > 2) {
                    var _count = 1;
                    for (var i = 2; i < len; i++) {
                        newArray[_count] = doubleArrays[i];
                        _count++;
                    }
                }
                return step.doExchange(newArray);
            }else {
                return doubleArrays[0];
            }
        }
    };
    return step;
});

/**
 * 错误提示
 * @param msg
 */

var success="success";
var error="error";
var warn="warn";

function notify(level,msg){
    var title = "提示";
    if(level==success){
        var opts = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-bottom-right",
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.success(msg,title,opts);
    }else if(level==error){
        var opts = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-top-full-width",
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };

        toastr.error(msg,title,opts);
    }else if(level==warn){
        var opts = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-bottom-left",
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.warning(msg,title,opts);
    }else{
        toastr.info(msg);
    }
}


//var formTypes = ['text','textarea','box','image','images','number','datetime','linkage'];
var formTypes = ['text','textarea','box','linkage','datetime','fulltext'];

// 看过jquery源码就可以发现$.fn就是$.prototype, 只是为了兼容早期版本的插件
// 才保留了jQuery.prototype这个形式
$.prototype.mergeCell = function (options) {
    return this.each(function () {
        var cols = options.cols;
        for (var i = cols.length - 1; cols[i] != undefined; i--) {
            // fixbug console调试
            // console.debug(cols[i]);
            mergeCell($(this), cols[i]);
        }
        dispose($(this));
    });
};

// 如果对javascript的closure和scope概念比较清楚, 这是个插件内部使用的private方法
// 具体可以参考本人前一篇随笔里介绍的那本书
    function mergeCell($table, colIndex) {
        $table.data('col-content', ''); // 存放单元格内容
        $table.data('col-rowspan', 1); // 存放计算的rowspan值 默认为1
        $table.data('col-td', $()); // 存放发现的第一个与前一行比较结果不同td(jQuery封装过的), 默认一个"空"的jquery对象
        $table.data('trNum', $('tbody tr', $table).length); // 要处理表格的总行数, 用于最后一行做特殊处理时进行判断之用
        // 我们对每一行数据进行"扫面"处理 关键是定位col-td, 和其对应的rowspan
        $('tbody tr', $table).each(function (index) {
            // td:eq中的colIndex即列索引
            var $td = $('td:eq(' + colIndex + ')', this);
            // 取出单元格的当前内容
            var currentContent = $td.html();
            // 第一次时走此分支
            if ($table.data('col-content') == '') {
                $table.data('col-content', currentContent);
                $table.data('col-td', $td);
            } else {
                // 上一行与当前行内容相同
                if ($table.data('col-content') == currentContent) {
                    // 上一行与当前行内容相同则col-rowspan累加, 保存新值
                    var rowspan = $table.data('col-rowspan') + 1;
                    $table.data('col-rowspan', rowspan);
                    // 值得注意的是 如果用了$td.remove()就会对其他列的处理造成影响
                    $td.hide();
                    // 最后一行的情况比较特殊一点
                    // 比如最后2行 td中的内容是一样的, 那么到最后一行就应该把此时的col-td里保存的td设置rowspan
                    if (++index == $table.data('trNum'))
                        $table.data('col-td').attr('rowspan', $table.data('col-rowspan'));
                } else { // 上一行与当前行内容不同
                    // col-rowspan默认为1, 如果统计出的col-rowspan没有变化, 不处理
                    if ($table.data('col-rowspan') != 1) {
                        $table.data('col-td').attr('rowspan', $table.data('col-rowspan'));
                    }
                    // 保存第一次出现不同内容的td, 和其内容, 重置col-rowspan
                    $table.data('col-td', $td);
                    $table.data('col-content', $td.html());
                    $table.data('col-rowspan', 1);
                }
            }
        });
    }

// 同样是个private函数 清理内存之用
    function dispose($table) {
        $table.removeData();
    }