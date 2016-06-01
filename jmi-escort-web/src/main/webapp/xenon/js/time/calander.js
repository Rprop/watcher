/**
 *####日历####
 *
 ***Demo**
 *
 * [calendar](../ui/calendar/1.0.0/example/calendar.html "Demo")
 *
 ***参数**
 *
 *  - `hasFestival` {String}   false  显示节假日信息
 *  - `format`  {String}   'yyyy-mm-dd'  输出日期格式
 *  - `hasToday` {}  false  是否显示今日
 *  - `today`  {String}   '今日'  今日文案
 *  - `isDouble` {Boolse}  false   是否显示一对日历
 *  - `isCompleteMode` {Boolse}  true   是否显示上月下月空的日期
 *  - `isCloseOnSelectDay` {Boolse}  true   当点击日期“天”时，是否关闭窗口
 *  - `defaultDate` {String|Date} null 初始日期，优先级排序text.val() > defaultDate > beginDate > new Date()
 *  - `beginDate` {String}  ''  开始时间,格式2014/05/01
 *  - `endDate` {String}  ''  结束时间,格式2014/05/01
 *  - `monthAjaxParam` {String}  ''  某月要显示的数据,格式{"date" {}  "2014-05","info" {}  [23, 283]}
 *  - `dayTpl` {String}  '<a href="javascript:;"><%=day%></a>'  天tpl模板 '<%=date%> <%=day%> <%=info%>'
 *  - `dayRangeTpl` {String}  '<%=day%>'  超出时间范围的day tpl
 *  - `siteMonthDayTpl` {String}  '<%=day%>'  旁边月份的day tpl
 *  - `type` {String}  'stick'  日历显示方式 {}  stick或者垂直距中center
 *  - `zIndex` {Number}  100 日历z-index
 *  - `confirmButTpl` {String}  '<a href="javascript:;">确定</a>'
 *  - `changeMonth` {Boolse}   false  改变月份
 *  - `changeYear` {Boolse}   false  改变年份
 *  - `changeHour` {Boolse}   false  改变小时
 *  - `changeMinute` {Boolse}   false  改变分钟
 *  - `changeSecond` {Boolse}   false  改变秒
 *  - `onReady` {Function}  null  初始化结束回调，回调的第一个参数为当前元素
 *  - `onShow` {Function}  null  日历显示出来时回调，回调的第一个参数为当前元素
 *  - `onHide` {Function}  null  日历隐藏时回调，回调的第一个参数为当前元素
 *  - `onPrevMonth` {Function}  null  切换至上月回调，回调的第一个参数为当前元素
 *  - `onNextMonth` {Function}  null  切换至下月回调，回调的第一个参数为当前元素
 *  - `onSelectDate` {Function}  null 选择某个日期回调，回调的第一个参数为日期，第二个参数为当前元素
 *  - `specialDate` {Array}  null 指定日期加高亮样式，如['2014/06/18','2015/01/01']
 *  - `specialDateClass` {String}  'special' 特殊日期的样式
 *  - `disabledDate` {Array} [] //可随意指定需要置灰的日期，例如：['2015/04/01', '2015/03/03']
 *  - `enabledDate` {Array} [] //指定可选择的不连续的日期，例如：['2015/04/01', '2015/03/03']
 *  - `footerClass` {String}  'ui-calendar-footer' 脚部样式,不建议修改原来名字，但支持扩展
 *
 ***举例**
 *
 *	$('#calendar').calendar();
 *
 * **update**
 * 2015-04-17 13:50:00 by chenxiaochun
 * 可以随意指定置灰的日期
 *
 * 2015-04-07 11:42:00 by chenxiaochun
 * 每次动态计算日历的显示位置
 *
 * 2015-02-12 10:16:00 by chenxiaochun
 * 选择日期之后，可以获取当前触发的元素
 *
 * 2013-12-25 16:05:39 / 2014-5-5 17:38:05  by liuwei1
 *
 */


;(function($, undefined) {
    //options
    var options = {
        hasCssLink:true,
        baseVersion:'1.0.0',
        cssLinkVersion:'1.0.0',

        hasFestival: false, //显示节假日信息
        format : 'yyyy-mm-dd',//输出日期格式
        hasToday:false,//是否显示今日
        today : '今日', //今日文案
        isDouble:false, // 是否显示一对日历
        isCompleteMode:true, // 是否显示上月下月空的日期
        isCloseOnSelectDay:false,//当点击日期“天”时，是否关闭窗口
        defaultDate:null,//初始日期;text.val() > defaultDate > beginDate > new Date()
        beginDate:'', //开始时间,格式2014/05/01
        endDate:'', //结束时间,格式2014/05/01
        //readonly:true,//允许输入日期，当改变日期后，再次点开日历时，会指向到此日期

        monthAjaxParam:'',//某月要显示的数据,格式{"date":"2014-05","info":[23, 283]}

        dayTpl:'<a href="javascript:;"><%=day%></a>',//天tpl模板 '<%=date%> <%=day%> <%=info%>'
        dayRangeTpl:'<%=day%>',//超出时间范围的day tpl
        siteMonthDayTpl:'<%=day%>',//旁边月份的day tpl
        type:'stick',//日历显示方式:stick或者垂直距中center
        zIndex:100,
        confirmButTpl:'<a href="javascript:;">确定</a>',
        changeMonth: false,//改变月份
        changeYear: false,//改变年份

        changeHour: false,//改变小时
        changeMinute: false,//改变分钟
        changeSecond: false,//改变秒

        onReady:null,//初始化结束回调
        onShow:null,//日历显示出来时回调
        onHide:null,//日历隐藏时回调
        onPrevMonth:null,//切换至上月回调
        onNextMonth:null,//切换至下月回调
        onSelectDate:null,//选择某个日期回调

        specialDate:null,//指定日期加高亮样式['2014/06/18','2015/01/01']
        specialDateClass:'special',//特殊日期,array,加特殊样式
        disabledDate: [],//指定不可选择的不连续日期
        enabledDate: [],//指定可选择的不连续的日期

        footerClass : 'ui-calendar-footer' //脚部样式,不建议修改原来名字，但支持扩展
    };

    //星期和月份
    Date.dayNames = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
    Date.abbrDayNames = ['日', '一', '二', '三', '四', '五', '六'];
    Date.monthNames = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];
    Date.abbrMonthNames = ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二'];

    /**
     * 是否为闫年
     * 闰年的计算方法:被100整除而不能被400整除为平年；被100整除也可被400整除
     * @method Date.isLeapYear
     * @return {Boolse} true|false
     */
    Date.prototype.isLeapYear = function(){
        var y = this.getFullYear();
        return (y%4==0 && y%100!=0) || y%400==0;
    }

    /**
     * 取月共多少天
     * @method Date.getDaysInMonth
     * @return {Number} 天数
     */
    Date.prototype.getDaysInMonth = function(){
        return [31,(this.isLeapYear() ? 29:28),31,30,31,30,31,31,30,31,30,31][this.getMonth()];
    }

    /**
     * 取月数组
     * @method Date.getDaysInMonthArray
     * @return {Array} [1,2,...31]
     */
    Date.prototype.getDaysInMonthArray = function(){
        var month = this.getDaysInMonth();
        var monthArray=[];
        for (var i = 1; i < month + 1; i++) {
            monthArray.push(i);
        };
        return monthArray;
    }

    /**
     * 取月名称
     * @method Date.getMonthName
     * @param {Boolse} abbreviated true "一" | false "1月"
     * @return {String} 月份
     */
    Date.prototype.getMonthName = function(abbreviated){
        return abbreviated ? Date.abbrMonthNames[this.getMonth()] : Date.monthNames[this.getMonth()];
    }

    /**
     * 返回星期几
     * @method Date.getDayName
     * @param {Boolse} abbreviated true 短星期如"一" | false 长星期如"星期一"
     * @return {String} 星期几
     */
    Date.prototype.getDayName = function(abbreviated){
        return abbreviated ? Date.abbrDayNames[this.getDay()] : Date.dayNames[this.getDay()];
    }

    /**
     * 增加月
     * @method Date.addMonths
     * @param {Number} num 月数
     * @return {Objtc} Date对象
     */
    Date.prototype.addMonths = function(num){
        if(typeof(num) == 'undefined') num = 1;
        this.setDate(1);
        this.setMonth(this.getMonth() + num);
        return this;
    }

    /**
     * 减少月
     * @method Date.reduceMonths
     * @param {Number} num 月数
     * @return {Objtc} Date对象
     */
    Date.prototype.reduceMonths = function(num){
        if(typeof(num) == 'undefined') num = 1;
        this.setDate(1);
        this.setMonth(this.getMonth() - num);
        return this;
    }

    /**
     * 增加年
     * @method Date.addYears
     * @param {Number} num 年数
     * @return {Objtc} Date对象
     */
    Date.prototype.addYears = function(num){
        if(typeof(num) == 'undefined') num = 1;
        this.setFullYear(this.getFullYear() + num);
        return this;
    }

    /**
     * 减少年
     * @method Date.reduceYears
     * @param {Number} num 年数
     * @return {Objtc} Date对象
     */
    Date.prototype.reduceYears = function(num){
        if(typeof(num) == 'undefined') num = 1;
        this.setFullYear(this.getFullYear() - num);
        return this;
    }

    /**
     * 日期格式化输出
     * @method Date.format
     * @param {String} format 年:yyyy 2014, yy 14;月 dd 01, d 1, 01;日 DD 01, D 1
     * @return {String} 输出日期
     */
    Date.prototype.format = function(format){
        var _zeroPad = function(num) {
            var s = '0'+num;
            return s.substring(s.length-2)
        };
        var r = format || Date.format;

        return r
            .split('yyyy').join(this.getFullYear())
            .split('yy').join((this.getFullYear() + '').substring(2))
            .split('dd').join(_zeroPad(this.getDate()))
            .split('d').join(this.getDate())
            .split('DD').join(this.getDayName(false))
            .split('D').join(this.getDayName(true))
            .split('mmmm').join(this.getMonthName(false))
            .split('mmm').join(this.getMonthName(true))
            .split('mm').join(_zeroPad(this.getMonth()+1))
            .split('hh').join(_zeroPad(this.getHours()))
            .split('min').join(_zeroPad(this.getMinutes()))
            .split('ss').join(_zeroPad(this.getSeconds()))
            .split('m').join(this.getMonth()+1);
    };

    /**
     * 字符日期转为Date对象
     * 注意：
     * 如果不需要默认值，defaultDate 请设定为 null ;
     * defaultDate等于undefined时，则为默认为当前时间
     * @param strDate 不能为空
     * @param format 不能为空
     * @param defaultDate
     * @returns {*}
     */
    Date.prototype.parseDate = function(strDate, format, defaultDate){
        var _defaultDate = defaultDate === null ? null : defaultDate || new Date();

        if ( isBlank(strDate, format) ) {
            return _defaultDate;
        }

        if ( strDate instanceof Date ) {
            //独立创建一个对象，避免对象引用混乱
            return strDate.clone();
        }else if ( typeof strDate != 'string' ) {
            return _defaultDate;
        }

        var longTime = Date.parse(strDate);
        if ( isNaN(longTime) ){
            var regFormat = /(\w{4})|(\w{3})|(\w{2})|(\w{1})/g;
            var regDate = /(\d{4})|(\d{2})|(\d{1})/g;
            var formats = format.match(regFormat);
            var dates = strDate.match(regDate);
            if( !isBlank(formats, dates) && formats.length == dates.length ) {
                var date = new Date();
                var pInt = function(s){
                    return parseInt(s, 10);
                };
                console.info(formats);
                for(var i = 0; i < formats.length; i++){
                    var fm = formats[i];
                    switch ( fm ) {
                        case 'yyyy' :
                            date.setFullYear(pInt(dates[i]));
                            break;
                        case 'yy' :
                            var prefix = (date.getFullYear() + '').substring(0, 2);
                            var ny = pInt(dates[i]);
                            var year = ('' + ny).length == 4? ny : prefix + (ny < 10 ? '0' + ny : '' + ny).substring(0, 2);
                            date.setFullYear(pInt(year));
                            break;
                        case 'mm' :
                            date.setMonth(pInt(dates[i]) - 1);
                            break;
                        case 'm' :
                            date.setMonth(pInt(dates[i]) - 1);
                            break;
                        case 'dd' :
                            date.setDate(pInt(dates[i]));
                            break;
                        case 'hh' :
                            date.setHours(pInt(dates[i]));
                            break;
                        case 'min' :
                            date.setMinutes(pInt(dates[i]));
                            break;
                        case 'ss' :
                            date.setSeconds(pInt(dates[i]));
                            break;
                    }
                }
                return date;
            }
            return _defaultDate;
        } else {
            return new Date(longTime);
        }
    };

    /**
     * 返回月1号的星期数
     * @method Date.getMonthFirstDay
     * @return {Number} 月1号的星期数
     */
    Date.prototype.getMonthFirstDay=function(){
        var year = this.getFullYear();
        var month = this.getMonth() + 1;
        var date = new Date(year+'/'+month+'/01');
        var day = date.getDay();
        if (day==0) {day=7;}
        return day;
    }

    /**
     * 返回月总天数
     * @method Date.getDaysInPrevMonth
     * @return {Number} 总天数
     */
    Date.prototype.getDaysInPrevMonth=function(){
        var year = this.getFullYear();
        var month = this.getMonth() + 1;
        var date = new Date(year+'/'+month+'/01');
        date.reduceMonths(1);
        return date.getDaysInMonth();
    }

    /**
     * 返回月数组
     * @method Date.getMonthArray
     * @return {Array} 格式 ['old31', 'now1', 'now2'..., 'new1', 'new2']
     */
    Date.prototype.getMonthArray=function(isCompleteMode){
        var currentMonth = [];
        var daysInMonth = this.getDaysInMonth();
        var prevMonthDay = this.getMonthFirstDay();
        var nextMonthDay = 42 - prevMonthDay - daysInMonth;
        var daysInPrevMonth= this.getDaysInPrevMonth();

        if (typeof(isCompleteMode) != 'undefined' && isCompleteMode){
            for (var i = (daysInPrevMonth - prevMonthDay+1) ; i < (daysInPrevMonth+1); i++) {
                currentMonth.push('old'+i);
            };
        }

        for (var i = 1; i < daysInMonth + 1; i++) {
            currentMonth.push('now'+i);
        };

        if (typeof(isCompleteMode) != 'undefined' && isCompleteMode){
            for (var i = 1; i < nextMonthDay+1; i++) {
                currentMonth.push('new'+i);
            };
        }

        return currentMonth;
    }

    /**
     * 返回下月
     * @method Date.getNextMonth
     * @return {String} 格式 yyyy/mm
     */
    Date.prototype.getNextMonth=function(){
        var d = new Date(this.format("yyyy/mm/dd"));
        d.addMonths(1);
        return d.format("yyyy/mm");
    }

    /**
     * 返回上月
     * @method Date.getPrevMonth
     * @return {String} 格式 yyyy/mm
     */
    Date.prototype.getPrevMonth=function(){
        var d = new Date(this.format("yyyy/mm/dd"));
        d.reduceMonths(1);
        return d.format("yyyy/mm");
    }

    /**
     * 返回月
     * @method Date.getCurrentMonth
     * @return {String} 格式 yyyy/mm
     */
    Date.prototype.getCurrentMonth=function(){
        return this.format("yyyy/mm");
    }

    /***
     * 克隆日期
     * @param {(format | string)} 日期格式
     * @method Date.clone
     * @returns {String|*}
     */
    Date.prototype.clone = function(format){
//        format = format || 'yyyy/mm/dd hh:min:ss';
//        return this.parseDate(this.format(format),format);
        return new Date(this.getTime());
    };

    /**
     ***补齐数字位数**
     * getNumTwoBit()
     * @param {(number | string)} n 需要补齐的数字
     * @return {string} 补齐两位后的字符
     */
    function getNumTwoBit(n) {
        return (n > 9 ? '' : '0') + n;
    }

    /**
     ***农历数据表**
     * lunarInfo
     * 从 1900 - 2100，16 进制前 12 表示对应年份 12 个月的大小，大月为 1，小月为 0
     * 最后4位表示是否闰年闰哪个月，或下一年闰的月是大月还是小月，仅当为 0xf 时表示大月
     */
    var lunarInfo = [
        0x4bd8, 0x4ae0, 0xa570, 0x54d5, 0xd260, 0xd950, 0x5554, 0x56af, 0x9ad0,
        0x55d2, 0x4ae0, 0xa5b6, 0xa4d0, 0xd250, 0xd295, 0xb54f, 0xd6a0, 0xada2,
        0x95b0, 0x4977, 0x497f, 0xa4b0, 0xb4b5, 0x6a50, 0x6d40, 0xab54, 0x2b6f,
        0x9570, 0x52f2, 0x4970, 0x6566, 0xd4a0, 0xea50, 0x6a95, 0x5adf, 0x2b60,
        0x86e3, 0x92ef, 0xc8d7, 0xc95f, 0xd4a0, 0xd8a6, 0xb55f, 0x56a0, 0xa5b4,
        0x25df, 0x92d0, 0xd2b2, 0xa950, 0xb557, 0x6ca0, 0xb550, 0x5355, 0x4daf,
        0xa5b0, 0x4573, 0x52bf, 0xa9a8, 0xe950, 0x6aa0, 0xaea6, 0xab50, 0x4b60,
        0xaae4, 0xa570, 0x5260, 0xf263, 0xd950, 0x5b57, 0x56a0, 0x96d0, 0x4dd5,
        0x4ad0, 0xa4d0, 0xd4d4, 0xd250, 0xd558, 0xb540, 0xb6a0, 0x95a6, 0x95bf,
        0x49b0, 0xa974, 0xa4b0, 0xb27a, 0x6a50, 0x6d40, 0xaf46, 0xab60, 0x9570,
        0x4af5, 0x4970, 0x64b0, 0x74a3, 0xea50, 0x6b58, 0x5ac0, 0xab60, 0x96d5,
        0x92e0, 0xc960, 0xd954, 0xd4a0, 0xda50, 0x7552, 0x56a0, 0xabb7, 0x25d0,
        0x92d0, 0xcab5, 0xa950, 0xb4a0, 0xbaa4, 0xad50, 0x55d9, 0x4ba0, 0xa5b0,
        0x5176, 0x52bf, 0xa930, 0x7954, 0x6aa0, 0xad50, 0x5b52, 0x4b60, 0xa6e6,
        0xa4e0, 0xd260, 0xea65, 0xd530, 0x5aa0, 0x76a3, 0x96d0, 0x4afb, 0x4ad0,
        0xa4d0, 0xd0b6, 0xd25f, 0xd520, 0xdd45, 0xb5a0, 0x56d0, 0x55b2, 0x49b0,
        0xa577, 0xa4b0, 0xaa50, 0xb255, 0x6d2f, 0xada0, 0x4b63, 0x937f, 0x49f8,
        0x4970, 0x64b0, 0x68a6, 0xea5f, 0x6b20, 0xa6c4, 0xaaef, 0x92e0, 0xd2e3,
        0xc960, 0xd557, 0xd4a0, 0xda50, 0x5d55, 0x56a0, 0xa6d0, 0x55d4, 0x52d0,
        0xa9b8, 0xa950, 0xb4a0, 0xb6a6, 0xad50, 0x55a0, 0xaba4, 0xa5b0, 0x52b0,
        0xb273, 0x6930, 0x7337, 0x6aa0, 0xad50, 0x4b55, 0x4b6f, 0xa570, 0x54e4,
        0xd260, 0xe968, 0xd520, 0xdaa0, 0x6aa6, 0x56df, 0x4ae0, 0xa9d4, 0xa4d0,
        0xd150, 0xf252, 0xd520
    ];

    /**
     ***返回农历 y 年的总天数**
     * lYearDays()
     * @param {Number} y 年份
     * @return {Number} y 年的总天数
     */
    function lYearDays(y) {
        var days = 348 + (lunarInfo[y - 1900] >> 4).toString(2).replace(/0/g, '').length;
        return days + leapDays(y);
    }

    /**
     ***返回农历 y 年闰月的天数**
     * leapDays()
     * @param {Number} y 年份
     * @return {Number} 闰月的天数（大月30，小月29，无闰月0）
     */
    function leapDays(y) {
        return leapMonth(y) ? (lunarInfo[y - 1899] & 0xf) === 0xf ? 30 : 29 : 0;
    }

    /**
     ***返回农历 y 年闰哪个月 1-12 , 没闰返回 0**
     * leapMonth()
     * @param {Number} y 年份
     * @return {Number} 闰月月份，0为不闰
     */
    function leapMonth(y) {
        var lm = lunarInfo[y - 1900] & 0xf;
        return lm === 0xf ? 0 : lm;
    }
    /**
     ***返回农历 y 年 m 月的总天数**
     * monthDays()
     * @param {Number} y 年份
     * @param {Number} m 月份
     * @return {Number} 农历 y 年 m 月的天数（大月30，小月29）
     */
    function monthDays(y, m) {
        return (lunarInfo[y - 1900] & (0x10000 >> m)) ? 30 : 29;
    }

    /**
     ***获取阳历对应的农历信息**
     * getLunarInfo()
     * @param {Date} date 阳历日期对象
     * @return {Object} 农历信息
     */
    function getLunarInfo(date) {
        var i, leap = 0, temp = 0;
        var offset = (
            Date.UTC(date.getFullYear(), date.getMonth(), date.getDate())
            - Date.UTC(1900, 0, 31)
            ) / 86400000;

        for (i = 1900; i < 2100 && offset > 0; i++) {
            temp = lYearDays(i);
            offset -= temp;
        }

        if (offset < 0) {
            offset += temp;
            i--;
        }

        var year = i;

        leap = leapMonth(i); //闰哪个月
        var isLeap = false;

        for (i = 1; i < 13 && offset > 0; i++) {
            //闰月
            if (leap > 0 && i === leap + 1 && !isLeap) {
                --i;
                isLeap = true;
                temp = leapDays(year);
            }
            else {
                temp = monthDays(year, i);
            }

            //解除闰月
            if (isLeap && i === leap + 1) {
                isLeap = false;
            }

            offset -= temp;
        }

        if (offset === 0 && leap > 0 && i === leap + 1) {
            if (isLeap) {
                isLeap = false;
            }
            else {
                isLeap = true;
                --i;
            }
        }

        if (offset < 0) {
            offset += temp;
            --i;
        }

        return {
            year: year,
            month: i - 1,
            day: offset + 1,
            leap: isLeap,
            solar: date
        };
    }

    //农历节日
    var lFtv = {
        '0100': '除夕',
        '0101': '春节',
        '0505': '端午',
        '0815': '中秋'
    };

    /**
     ***获取农历节日**
     * getLunarFestival()
     * @param {Object} lunar 农历日期信息
     * @return {Object} 农历节日
     */
    function getLunarFestival(lunar) {
        // 处理除夕
        if (lunar.month === 11 && lunar.day > 28) {
            var next =  new Date(lunar.solar.getTime() + 1000 * 60 * 60 * 24);
            next = getLunarInfo(next);
            if (next.day === 1) {
                lunar.month = 0;
                lunar.day = 0;
            }
        }
        var text = lunar.leap
            ? ''
            : lFtv[getNumTwoBit(lunar.month + 1) + getNumTwoBit(lunar.day)] || '';

        return text ? text : null;
    }

    // 阳历节日
    var sFtv = {
        '0101': '元旦',
        '0405': '清明',
        '0501': '五一',
        '1001': '国庆'
    };

    /**
     ***获取阳历节日**
     * getSolarFestival()
     * @param {Date} date 阳历日期对象
     * @return {Object} 阳历节日
     */
    function getSolarFestival(date) {
        var text = sFtv[getNumTwoBit(date.getMonth() + 1) + getNumTwoBit(date.getDate())];
        return text ? text : null;
    }

    /**
     ***获取节日或节气信息**
     * getFestival()
     * @param {Date} date 阳历日期对象
     * @return {String} 节日或节气信息
     */
    function getFestival(date) {
        var lunar = getLunarInfo(date);
        return getLunarFestival(lunar) || getSolarFestival(date);
    }

    /**
     * 获取节日或节气信息
     * @method Date.getFestival
     * @param {Date} date 阳历日期对象
     * @return {String} 节日或节气信息
     */
    Date.prototype.getFestival=function(){
        return getFestival(this);
    }

    function isBlank ( value ) {
        /**function(v, v2, v3, ...) or compare*/
        var args = arguments;
        if ( args.length > 1 ) {
            for ( var i = 0, len = args.length; i < len; i++) {
                if ( isBlank( args[ i ] ) ) return true;
            }
            return false;
        }
        return String(value) === 'undefined' || String( value ) === 'null' || (typeof (value) === 'string' ? $.trim( value ) === '' : false );
    }

    //calendar组件定义
    $.ui.define('calendar', {
        options:options,
        init:function(){
            var me = this;
            var opts = me.options;
            // if( me.options.readonly ) {
            //     me.el.attr('readonly','readonly');
            // }
            //开始时间对象，私有不公开,数据来源options.beginDate
            me.beginDate = null;
            //结束时间对象，私有不公开,数据来源options.endDate
            me.endDate = null;

            //设置初始限制日期
            if ( !isBlank(opts.beginDate) ) {
                me.beginDate = new Date().parseDate(opts.beginDate, opts.format, null);
            }

            if ( !isBlank(opts.endDate) ) {
                me.endDate = new Date().parseDate(opts.endDate, opts.format, null);
            }


            //当前选中的日期
            me.currentDate = new Date();

            var defaultValue = '';
            if ( !isBlank(me.el.val()) ) {
                defaultValue = $.trim(me.el.val());
            } else
            if ( !isBlank(opts.defaultDate) ) {
                var _defaultDate = new Date().parseDate(opts.defaultDate, opts.format, null);
                if ( _defaultDate ) {
                    defaultValue = _defaultDate.format(opts.format);
                    me.el.val(defaultValue);
                }
            }else
            if ( !isBlank(opts.beginDate) ) {
                var _beginDate = new Date().parseDate(opts.beginDate, opts.format, null);
                if ( _beginDate ) {
                    me.setBeginDate(_beginDate);
                    //setBeginDate 已经把处理好的日期赋值给 currentDate了，所以下面的currentDate赋值就不再执行！！
                    defaultValue = null;
                }
            }

            if ( !isBlank(defaultValue) ) {
                me.currentDate = me.currentDate.parseDate(defaultValue, me.options.format);
            }

            me.dateInit(me.currentDate);

            me.create();

            me.rander(true, function(){
                me.bind();
                me.setStyle();
                if(me.options.onReady) me.options.onReady(me.el);
            });
        },
        dateInit:function (dateObj){
            var _this = this;
            var opts = _this.options;
            //单日历 克隆操作对象，不影响已选择的对象
            _this.date = dateObj.clone();
            //双日历
            //var h = dateObj.format('hh');
            //var m = dateObj.format('min');
            //var s = dateObj.format('ss');
            //_this.dateDouble = new Date(dateObj.getNextMonth() +'/01' +' '+h +':'+m +':'+s);
            _this.dateDouble = dateObj.clone();
            _this.dateDouble.setDate(1);
            _this.dateDouble.setMonth(_this.dateDouble.getMonth()+1);
            var endDate = _this.endDate;

            if ( endDate ) {
                if ( opts.isDouble && _this.dateDouble.format('yyyymm') > endDate.format('yyyymm') ) {
                    _this.date.setMonth(_this.date.getMonth() - 1);
                    _this.dateDouble.setMonth(_this.dateDouble.getMonth() - 1);
                }
            }

        },
        //日历模板
        tpl:function(){
            var opts = this.options;
            var isDouble = this.options.isDouble;
            var getTableTpl = function(index, total){
                var index = typeof(index) == 'undefined' ? 1 : index;

                var hide = 'style="display:none"';
                var prevClass = 'prev';
                var nextClass = 'next';
                var placeClass = '';
                if (isDouble){
                    if ( index == 1 ){
                        nextClass = '';
                        placeClass = ' ui-calendar-left';
                    }else if( index == 2 ){
                        prevClass = '';
                        placeClass = ' ui-calendar-right';
                    }
                }

                return tableTpl = '<table class="ui-calendar-table'+placeClass+'" data-index="'+index+'">\
					<thead>\
						<tr>\
							<th class="'+prevClass+'"><i></i></th>\
							<th class="switch" colspan="5"></th>\
							<th class="'+nextClass+'"><i></i></th>\
						</tr>\
					</thead>\
					<tbody></tbody>\
					<tfoot><tr><th colspan="7" class="today"></th></tr></tfoot>\
				</table>';
            }

            var tableTpl = getTableTpl(1);
            var doubleClass = '';

            if (isDouble) {
                tableTpl += getTableTpl(2);
                doubleClass = ' ui-calendar-double';
            }

            var footerTpl = '';
            //小时 分钟 选择
            var time = '';
            if (opts.changeHour || opts.changeMinute || opts.changeSecond) {
                footerTpl = '<tr><td class="'+opts.footerClass+'"><div class="time"></div></td></tr>';
                //$(footerTpl).html('<tr><td class="time"></th></tr>').prop('outerHTML');
            }

            return '<div class="ui-calendar'+doubleClass+' ui-calendar'+this.guid+'" style="display:none"><table class="ui-calendar-warp"><tr><td class="ui-calendar-body">'+tableTpl+'</td></tr>'+footerTpl+'</table></div>';
        },
        create:function(){
            $(this.tpl()).appendTo('body');
            this.calendar = $('.ui-calendar'+this.guid);

            var headHtml = '<tr>';
            var dayNamaeArray = Date.abbrDayNames;
            for (var i = 0; i < dayNamaeArray.length; i++) {
                headHtml += '<th>' + dayNamaeArray[i] + '</th>';
            }
            headHtml += "</tr>";
            this.calendar.find('thead').append(headHtml);

            if (this.options.hasToday) {
                this.calendar.find('.today').html(this.options.today);
            }
        },
        setStyle:function(){
            var opts = this.options;
            if (opts.type == 'stick' ) {
                var top = this.el.offset().top + this.el.outerHeight();
                var left = this.el.offset().left;
                this.calendar.css({
                    top: top,
                    left: left,
                    zIndex: opts.zIndex+this.guid
                });
            }else if (opts.type == 'center' ) {
                this.middle(this.calendar);
            }
        },
        //设置垂直距中
        middle:function(el){
            var ow = el.outerWidth();
            var oh = el.outerHeight();
            var scrollbarWidth = $.page.docHeight() != $.page.clientHeight() ? 16 :0;
            var top = ( $.page.clientHeight() - oh ) / 2;
            var left = ( $.page.clientWidth() - scrollbarWidth - (ow) ) / 2;
            el.css({
                left: left,
                top: top
            });
        },
        //取当前月份要显示的数据
        //数据格式 {"date":"2014-05","info":[23, 283]}
        getMonthData:function(dateObj, callback){
            var opts = this.options;
            var date = dateObj.format("yyyy-mm");

            if (opts.monthAjaxParam) {
                var successFn = function(data){
                    if (data.date == date  && typeof(data.info) =='object' ) {
                        if(callback) callback(data.info);
                    }
                };
                var param = $.extend({
                    type:'GET',
                    dataType:'json',
                    data:'date='+date,
                    success:successFn
                }, opts.monthAjaxParam);
                $.ajax(param);
            }else {
                callback(null);
            }
        },
        /**
         * 更新主体内容
         * @method rander
         * @param {Boolse} isInit 是否取系统时间更新
         * @param {Function} callback 回调
         * @param {String} type 来源prev||next
         */
        rander:function(isInit,callback, type){
            var me = this;
            var dateObj = me.date;
            var opts = me.options;
            me.getMonthData(dateObj, function(monthData){
                if ( !opts.isDouble ){
                    //单日历
                    me.randerMain(isInit, monthData, dateObj);
                    me.rangeSet();
                    if(callback) callback();
                }else{
                    //双日历
                    var dateObjDouble = me.dateDouble;
                    me.getMonthData(dateObjDouble, function(monthDataDouble){
                        me.randerMain(isInit, monthData, dateObj);
                        me.randerMain(null, monthDataDouble, dateObjDouble, 2);
                        me.rangeSet();
                        if(callback) callback();
                    });
                }
            });
        },
        getFestival:function (day, j){
            if (this.options.hasFestival && day){
                var festival = getFestival(new Date(day));
                if(festival){
                    return '<span class="festival">'+festival+'</span>';
                }
            }
            return j;
        },
        randerMain:function(isInit, monthData, dateObj, index){
            var index = typeof(index) == 'undefined' ? 1 : index;
            var data_index = 'data-index="'+index+'"';
            var _this = this;
            var opts = _this.options;
            var bodyHtml = '<tr>';
            var currentMonth = dateObj.getMonthArray(true);

            var beginDate = _this.beginDate;
            var endDate = _this.endDate;
            var beginDateY = '';
            var beginDateM = '';
            var endDateYMD = '';
            var endDateY = '';
            var endDateM = '';

            var hmsstr = ' ' + _this.getHMS().join(':');

            if ( beginDate ) {
                beginDateY = beginDate.format("yyyy");
                beginDateM = beginDate.format("mm");
            }

            if ( endDate ) {
                endDateY = endDate.format("yyyy");
                endDateM = endDate.format("mm");
            }

            //当前当天才标识为蓝色
            var isCurrentMonth = _this.currentDate.format('yyyymm') == dateObj.format('yyyymm');
            var dateObjDay = _this.currentDate.getDate();

            for (var i = 0; i < currentMonth.length; i++) {
                var tr = '';
                if ( (i+1)%7 == 0) {
                    tr = '</tr><tr>';
                }

                var td = '';
                //上一月 old
                if (/old/.test(currentMonth[i])) {
                    var prevMonthDay = currentMonth[i].replace('old','');
                    var prevMonthStr = dateObj.getPrevMonth() +'/'+ _this.getNumTwoBit(prevMonthDay);
                    var prevMonthRange = _this.getDayRangeClass(prevMonthStr + hmsstr, beginDate, endDate);
                    day = _this.getFestival(prevMonthStr, prevMonthDay);
                    var prevMonthTpl = prevMonthRange ? $.tpl(opts.dayRangeTpl,{day:day})  :  $.tpl(opts.siteMonthDayTpl,{day:day});
                    td = opts.isCompleteMode
                        ? '<td class="old' + prevMonthRange + '" data-day="'+prevMonthDay+'" '+data_index+'>'+prevMonthTpl
                        : '<td>' ;
                }

                //当前月 day
                if (/now/.test(currentMonth[i])) {
                    var j = currentMonth[i].replace('now','');
                    var active = '';
                    if ( isCurrentMonth && j == dateObjDay ) {
                        active = ' active';
                    }

                    //getFestival
                    var dayTpl = j;
                    var currentMonthStr = dateObj.getCurrentMonth() +'/'+ _this.getNumTwoBit(j);
                    var currentMonthRange = _this.getDayRangeClass(currentMonthStr + hmsstr, beginDate, endDate);
                    var day = _this.getFestival(currentMonthStr, j);
                    //是否超过限定的日期
                    if (!currentMonthRange) {
                        var infoObj = monthData ? monthData[j-1] : null;
                        dayTpl =  $.tpl(opts.dayTpl,{date:currentMonthStr, day:day, info:infoObj});
                    }else {
                        dayTpl =  $.tpl(opts.dayRangeTpl,{day:day});
                    }
                    var specialDateClass = _this.getSpecialDateClass(currentMonthStr);
                    td = '<td class="day' + active + currentMonthRange + specialDateClass+'" data-day="'+j+'" '+data_index+'>'+dayTpl;
                }

                //下一月 new
                if (/new/.test(currentMonth[i])) {
                    var nextMonthDay = currentMonth[i].replace('new','');
                    var nextMonthStr = dateObj.getNextMonth() +'/'+ _this.getNumTwoBit(nextMonthDay);
                    var nextMonthRange = _this.getDayRangeClass(nextMonthStr + hmsstr, beginDate, endDate);
                    day = _this.getFestival(nextMonthStr, nextMonthDay);

                    var nextMonthTpl = nextMonthRange ? $.tpl(opts.dayRangeTpl,{day:day})  :  $.tpl(opts.siteMonthDayTpl,{day:day});
                    td = opts.isCompleteMode
                        ? '<td class="new' + nextMonthRange + '"  data-day="'+nextMonthDay+'"  '+data_index+'>' + nextMonthTpl
                        : '<td>' ;
                }

                bodyHtml += td +'</td>'+ tr;
            }

            bodyHtml += '</tr>';

            //天
            var calendarTable = _this.calendar.find('table[data-index='+index+']');
            calendarTable.find('tbody').html(bodyHtml);

            //年份
            var currentYear = dateObj.getFullYear();
            if ( opts.changeYear ){
                var optionYear = '';
                var beginYear = currentYear-5;
                var endYear = currentYear+5;
                if (!isBlank(beginDate, beginDateY) && beginYear<=beginDateY) {
                    beginYear = beginDateY;
                }

                if (!isBlank(endDate,endDateY)) {
                    endYear=endDateY;
                }

                for (var i=beginYear; i<=endYear; i++){
                    var select = i==currentYear ? 'selected="true"' : '';
                    optionYear += '<option '+select+'>'+i+'年</option>';
                }
                optionYear = '<select data-year="'+currentYear+'" '+data_index+' class="year">'+optionYear+'</select>';
                currentYear = optionYear;
            }else{
                currentYear = currentYear+'年';
            }

            //月份
            var currentMonth = parseFloat(dateObj.format('mm'));

            if ( opts.changeMonth ){
                var optionMonth = '';
                var beginMonth = 1;
                var endMonth = 13;

                //加上了去限制日期的逻辑
                if (!isBlank(beginDate,beginDateY) && dateObj.getFullYear() == beginDateY ) {
                    if (beginMonth<=beginDateM) beginMonth=parseFloat(beginDateM);
                    if ( opts.isDouble ) {
                        if ( index == 2 ) {
                            beginMonth += 1;
                        }
                    }
                }
                //加上了去限制日期的逻辑
                if (!isBlank(endDate,endDateY) && dateObj.getFullYear() == endDateY ) {
                    if (endMonth>endDateM) endMonth=parseFloat(endDateM)+1;
                    if ( opts.isDouble ) {
                        if ( index == 1 ) {
                            endMonth -= 1;
                        }
                    }
                }

                for (var i= beginMonth; i<endMonth; i++){
                    var select = i==currentMonth  ? 'selected="true"' : '';
                    optionMonth += '<option '+select+'>'+i+'月</option>';
                }
                optionMonth = '&nbsp;&nbsp;<select data-month="'+currentMonth+'" '+data_index+' class="month">'+optionMonth+'</select>';
                currentMonth = optionMonth;
            }else{
                currentMonth = currentMonth+'月';
            }

            var switchHtml = currentYear+ currentMonth ;
            calendarTable.find('thead .switch').html(switchHtml);

            if (index == 1) {
                //时分秒只绘制一次
                _this.drawCalendarTime(dateObj, index);
            } else {
                calendarTable.find('.today').hide();
            }
        },
        filterAllowDay : function(dateObj, index){
            index = index === undefined ? 1 : index;
            var _this = this;
            var opts = _this.options;
            var beginDate = _this.beginDate;
            var endDate = _this.endDate;
            var data_index = 'data-index="'+index+'"';
            var calendarTable = _this.calendar.find('table['+data_index+']');
            var currentDateYMD = _this.currentDate.format('yyyymmdd');
            if ( beginDate || endDate ) {
                //过滤不能选择的日期
                var _crdate = dateObj.parseDate(dateObj.format(opts.format), opts.format, null);
                if ( _crdate ) {
                    calendarTable.find('.day[' + data_index + ']').each(function () {
                        _this.filterAllowAction(this, _crdate, beginDate, endDate, opts.dayTpl, currentDateYMD);
                    });
                }

                _crdate = dateObj.parseDate(dateObj.format(opts.format), opts.format, null);
                if ( _crdate ) {
                    _crdate.setMonth(_crdate.getMonth()+1);
                    calendarTable.find('.new[' + data_index + ']').each(function () {
                        _this.filterAllowAction(this, _crdate, beginDate, endDate, opts.siteMonthDayTpl);
                    });
                }

                _crdate = dateObj.parseDate(dateObj.format(opts.format), opts.format, null);
                if ( _crdate ) {
                    _crdate.setMonth(_crdate.getMonth()-1);
                    calendarTable.find('.old[' + data_index + ']').each(function () {
                        _this.filterAllowAction(this, _crdate, beginDate, endDate, opts.siteMonthDayTpl);
                    });
                }

                //过虑确定按钮
                _this.filterAllowConfirmBut();
            }
        },
        filterAllowAction:function(dthis, crdate, beginDate, endDate, tpl, currentDateYMD){
            var _this = this;
            var opts = _this.options;
            var _d = $(dthis);
            var _dday = _d.data('day');
            var dayTpl = '';
            crdate.setDate(_dday);
            if (beginDate) {
                //是否超过限定的日期,去除毫秒，保留到秒来比较
                if ( _this.dateCompare(beginDate, crdate, true) ) {
                    dayTpl = $.tpl(opts.dayRangeTpl,{day:_dday});
                    _d.addClass('range').removeClass('active');
                }else {
                    dayTpl = $.tpl(tpl,{day:_dday});
                    _d.removeClass('range');
                    if ( currentDateYMD && crdate.format('yyyymmdd') == currentDateYMD ) {
                        _d.addClass('active');
                    }
                }

                _d.html(dayTpl);
            }

            if (endDate) {
                //是否超过限定的日期,去除毫秒，保留到秒来比较
//                if ( parseInt(crdate.getTime()/ 1000) >= endDateTime) {
                if ( _this.dateCompare(crdate, endDate, true) ) {
                    dayTpl = $.tpl(opts.dayRangeTpl,{day:_dday});
                    _d.addClass('range').removeClass('active');
                } else {
                    dayTpl = $.tpl(tpl,{day:_dday});
                    _d.removeClass('range');
                    if ( currentDateYMD && crdate.format('yyyymmdd') == currentDateYMD ) {
                        _d.addClass('active');
                    }
                }

                _d.html(dayTpl);
            }
        },
        filterAllowConfirmBut:function(){//过虑确定按钮
            var _this = this;
            var beginDate = _this.beginDate;
            var endDate = _this.endDate;
            var confirmBut = _this.calendar.find('.confirm-but');
            if ( confirmBut ) {
                var _ncdate = _this.getTime(_this.currentDate);
                if ( ( beginDate && _this.dateCompare(beginDate, _ncdate, true )) || ( endDate && _this.dateCompare(_ncdate, endDate, true) )) {
                    confirmBut.hide();
                } else {
                    confirmBut.show();
                }
            }
        },
        drawCalendarTime: function( dateObj, index ){
            index = index === undefined ? 1 : index;
            var _this = this;
            var data_index = 'data-index="'+index+'"';
            var opts = _this.options;
            var footer = _this.calendar.find('.'+opts.footerClass);
            //小时 分钟 选择
            var time = '';
            if (opts.changeHour || opts.changeMinute || opts.changeSecond) {
                var optionHour = '',  optionNooninute = '',optionSecond = '',  optionNoon = '';

                var d = dateObj;

                if (opts.changeHour) {
                    var h_scope = [0,24];
                    var currentHour = currentHourDefalut = d.format('hh');
                    for (var i = h_scope[0]; i < h_scope[1]; i++) {
                        var select = i==currentHour  ? 'selected="true"' : '';
                        optionHour += '<option '+select+'>'+i+'</option>';
                    }
                    optionHour = '<select data-hour="'+currentHourDefalut+'" '+data_index+' class="hour">'+optionHour+'</select>';
                }

                if (opts.changeMinute) {
                    var m_scope = [0,60];
                    var currentMinute = d.format('min');
                    for (var i = m_scope[0]; i< m_scope[1]; i++){
                        var select = i==currentMinute  ? 'selected="true"' : '';
                        optionNooninute += '<option '+select+'>'+i+'</option>';
                    }
                    optionNooninute = '&nbsp; : &nbsp; <select data-minute="'+currentMinute+'" '+data_index+' class="minute">'+optionNooninute+'</select>';
                }

                if (opts.changeSecond) {
                    var s_scope = [0,60];
                    var currentSecond = d.format('ss');
                    for (var i = s_scope[0]; i < s_scope[1]; i++){
                        var select = i==currentSecond  ? 'selected="true"' : '';
                        optionSecond += '<option '+select+'>'+i+'</option>';
                    }
                    optionSecond = '&nbsp; : &nbsp; <select data-second="'+currentSecond+'" '+data_index+' class="second">'+optionSecond+'</select>';
                }

                time = optionHour+optionNooninute+optionSecond;

                _this.filterAllowDay(_this.date, 1);
                if ( opts.isDouble ) {
                    _this.filterAllowDay(_this.dateDouble, 2);
                }

                var timeBox = footer.find('.time').html(time);

                if ( (opts.changeHour || opts.changeMinute || opts.changeSecond ) && !isBlank(opts.confirmButTpl)) {
                    timeBox.append($(opts.confirmButTpl).addClass('confirm-but'));
                }

            }
        },
        bind:function(){
            var me = this;
            var opts = this.options;

            //bind click
            this.el.bind('click',function (event){
                me.show(event);
                me.setStyle();
            });

            //切换至上一月
            this.calendar.on('click','.prev',function() {
                if(me.hasRange($(this))) return;
                me.onPrevMonth();
            });

            //切换至下一月
            this.calendar.on('click','.next',function() {
                if(me.hasRange($(this))) return;
                me.onNextMonth();
            });

            //select date
            var selectDateFn = function (_this, from){

                if(me.hasRange(_this)) return false;
                var day = _this.attr('data-day');
                var index = _this.attr('data-index');
                var date = (index== 2 ? me.dateDouble : me.date).format('yyyy/mm/dd');
                var newDate = new Date(date);
                if ( from==1 ){
                    newDate.addMonths(1);
                }else if( from==-1 ){
                    newDate.reduceMonths(1);
                }
                newDate.setDate(parseFloat(day));
                if ( opts.isCloseOnSelectDay ) {
                    me.setSelectDate(newDate, index);
                } else {
                    me.currentDate = me.getTime(newDate);
                    me.filterAllowConfirmBut();
                }
                return true;
            };

            //当前月 日期点击
            this.calendar.on('click','.day',function() {
                var _this = $(this);
                if(selectDateFn(_this, 0)){
                    me.calendar.find('.day').removeClass('active');
                    _this.addClass('active');
                }
            });

            //上月 日期点击
            this.calendar.on('click','.old',function() {
                var _this = $(this);
                if(selectDateFn(_this, -1)){
                    me.onPrevMonth();
                }
            });

            //下月 日期点击
            this.calendar.on('click','.new',function() {
                var _this = $(this);
                if(selectDateFn(_this, 1)){
                    me.onNextMonth();
                }
            });

            //返回今日
            if (this.options.hasToday) {
                this.calendar.on('click','.today', function(event) {
                    var date = new Date();
                    me.setSelectDate(date, 1);
                    /**注释以下代码原因：
                     * 因为在选择时分秒后，点击今日，整个HTML会重绘，造成选择的时分秒丢失。
                     * --by wuyaoheng 2014-10-15
                     * */
                    //me.rander(true,function(){
                    //	me.setSelectDate(date, 1);
                    //});
                });
            }

            //select year
            this.calendar.on('change','.year',function(event) {
                /**FIX 修复ie6-8在下拉框选择内容时，窗口会关闭; to-addDocumentHandler()*/
                event.stopPropagation();
                var _this = $(this);
                var beginDate = me.beginDate;
                var endDate = me.endDate;
                var num = _this.find('option').eq(_this[0].selectedIndex).val();
                num = parseFloat(num);
                var index = _this.attr('data-index');
                if ( index == 1 ){
                    me.date.setFullYear(num);
                    if(opts.isDouble){
                        me.dateDouble = me.date.clone();
                        me.dateDouble.setMonth(me.dateDouble.getMonth()+1);
                    }
                }else if ( index == 2 ){
                    if(opts.isDouble) {
                        me.dateDouble.setFullYear(num);
                        me.date = me.dateDouble.clone();
                        me.date.setMonth(me.date.getMonth()-1);
                    }
                }

                if ( beginDate ) {
                    if ( beginDate.format('yyyymm') > me.date.format('yyyymm') ) {
                        //如果选择的日期不在限制范围，则重新刷新(因为初始化的时候，去限制范围作了处理，固不再重复处理)
                        me.show(event);
                        return false;
                    }
                }
                if ( endDate ) {
                    if ( endDate.format('yyyymm') < me.date.format('yyyymm') ) {
                        //如果选择的日期不在限制范围，则重新刷新(因为初始化的时候，去限制范围作了处理，固不再重复处理)
                        me.show(event);
                        return false;
                    }
                }

                me.rander(null);
            });

            //select month
            this.calendar.on('change','.month',function(event) {
                var _this = $(this);
                var num = _this.find('option').eq(_this[0].selectedIndex).val();
                var index = _this.attr('data-index');
                num = parseFloat(num)-1;
                /**FIX 修复ie6-8在下拉框选择内容时，窗口会关闭;to-addDocumentHandler()*/
                event.stopPropagation();

                if ( index ==1 ){
                    me.date.setMonth(num);
                    if(opts.isDouble){
                        me.dateDouble = me.date.clone();
                        me.dateDouble.setMonth(num+1);
                    }
                }else if ( index ==2 ){
                    if(opts.isDouble) {
                        me.dateDouble.setMonth(num);
                        me.date = me.dateDouble.clone();
                        me.date.setMonth(num-1);
                    }
                }
                me.rander(null);
            });

            //select month
            this.calendar.on('change','.hour',function(event) {
                var _this = $(this);
                var num = _this.find('option').eq(_this[0].selectedIndex).val();
//                var index = _this.attr('data-index');
                /**FIX 修复ie6-8在下拉框选择内容时，窗口会关闭;to-addDocumentHandler()*/
                event.stopPropagation();

                me.date.setHours(num);

                if ( opts.isDouble ) {
                    me.dateDouble.setHours(num);
                }

                me.filterAllowDay(me.date, 1);
                if ( opts.isDouble ) {
                    me.filterAllowDay(me.dateDouble, 2);
                }
            });

            //select month
            this.calendar.on('change','.minute',function(event) {
                var _this = $(this);
                var num = _this.find('option').eq(_this[0].selectedIndex).val();
                var index = _this.attr('data-index');
                /**FIX 修复ie6-8在下拉框选择内容时，窗口会关闭;to-addDocumentHandler()*/
                event.stopPropagation();

                me.date.setMinutes(num);

                if ( opts.isDouble ) {
                    me.dateDouble.setMinutes(num);
                }

                me.filterAllowDay(me.date, 1);
                if ( opts.isDouble ) {
                    me.filterAllowDay(me.dateDouble, 2);
                }
            });

            //select month
            this.calendar.on('change','.second',function(event) {
                var _this = $(this);
                var num = _this.find('option').eq(_this[0].selectedIndex).val();
                var index = _this.attr('data-index');
                /**FIX 修复ie6-8在下拉框选择内容时，窗口会关闭;to-addDocumentHandler()*/
                event.stopPropagation();

                me.date.setSeconds(num);

                if ( opts.isDouble ) {
                    me.dateDouble.setSeconds(num);
                }

                me.filterAllowDay(me.date, 1);
                if ( opts.isDouble ) {
                    me.filterAllowDay(me.dateDouble, 2);
                }
            });

            this.calendar.on("click",".confirm-but",function(){
                me.setSelectDate(me.currentDate);
            });


            /***注释以下代码原因:
             * 1、此判断条件在点击其它（除event.target=body,html之外）元素时，条件逻辑会有识差
             * 2、性能问题，优化后改成显示时才绑定document.click事件，隐藏后解绑
             * 详情看：addDocumentHandler()
             * --by wuyaoheng 2014-10-15
             * */
            ////点空白隐藏
            //$(document).click(function(event){
            //	if ($.contains(event.target,me.calendar[0])){
            //		me.hide();
            //	}
            //})
        },
        //时间范围设置 超出时间范围对其加class range
        rangeSet:function(){
            var _this = this;
            var opts = _this.options;
            var beginDate = _this.beginDate;
            var endDate = _this.endDate;
            var hasNext = false;
            var hasPrev = false;

            if (beginDate) {
                var prev = _this.calendar.find('thead .prev');
                //if (dateObj.getPrevMonth() < new Date(opts.beginDate).format("yyyy/mm") ) {

                var prevMonth = _this.date.getPrevMonth();
                var beginDateym = beginDate.format("yyyy/mm");

                if ( prevMonth >= beginDateym ) {
                    hasPrev = true;
                }

                if ( !hasPrev ) {
                    prev.addClass('range');
                    prev.find('i').addClass('range');
                }else {
                    prev.removeClass('range');
                    prev.find('i').removeClass('range');
                }
            }

            if (endDate) {
                var next = _this.calendar.find('thead .next');
                //if (dateObj.getNextMonth() > new Date(opts.endDate).format("yyyy/mm") ) {
                var nextMonth = _this.date.getNextMonth();
                var endDateym = endDate.format("yyyy/mm");
                //小于限制月，或等于当月(需要限制时分秒还有选择空间)
                if ( nextMonth < endDateym ) {
                    hasNext = true;
                } else
                if ( nextMonth == endDateym && !opts.isDouble ) {
                    hasNext = true;
                }
                if ( !hasNext ) {
                    next.addClass('range');
                    next.find('i').addClass('range');
                }else {
                    next.removeClass('range');
                    next.find('i').removeClass('range');
                }
            }
        },
        //是否有range class
        hasRange:function(o){
            if(o.hasClass('range')){
                return true;
            }else {
                return false;
            }
        },
        //检测day是否超出设置的起始时间
        getDayRangeClass:function(day, beginDate, endDate){
            //if ( (this.options.beginDate && day < this.options.beginDate) ||
            //    (this.options.endDate && day > this.options.endDate)
            //    ) {
            //    return ' range';
            //}
//            if ( (!isBlank(beginDate, day) && day < beginDate) ||  (!isBlank(endDate) && day > endDate)) {
//                return ' range';
//            }

            day = new Date(day);
            if ( (!isBlank(beginDate, day) && this.dateCompare(beginDate, day, true)) ||  (!isBlank(endDate) && this.dateCompare(day, endDate, true))) {
                return ' range';
            }

            if(this.isInDisabledDate(day.format('yyyy/mm/dd'))){
                return ' range';
            }

            if(!this.isInEnabledDate(day.format('yyyy/mm/dd'))){
                return ' range';
            }

            return '';
        },
        //日期比较
        dateCompare: function(date, date2, equal){
            var opts = this.options;
            var dateTime = null;
            var date2Time = null;
            var on = false;
            if ( isBlank(date, date2) ) return false;
            //去除毫秒，保留到秒

            if (opts.changeSecond) {
                dateTime = parseInt(date.getTime() / 1000);
                date2Time = parseInt(date2.getTime() / 1000);
            } else if (opts.changeMinute) {
                dateTime = date.format('yyyymmddhhmin');
                date2Time = date2.format('yyyymmddhhmin');
            } else if (opts.changeHour) {
                dateTime = date.format('yyyymmddhh');
                date2Time = date2.format('yyyymmddhh');
            } else {
                dateTime = date.format('yyyymmdd');
                date2Time = date2.format('yyyymmdd');
            }

            if ( equal ) {
                on = dateTime == date2Time;
            }
            if ( !on ) {
                on = dateTime > date2Time;
            }
            return on;

        },
        //指定日期加高亮样式
        getSpecialDateClass:function (date){
            var opts = this.options;
            var specialDate = opts.specialDate;
            if(specialDate && $.isArray(specialDate)){
                return $.inArray(date, specialDate) > -1 ? ' '+opts.specialDateClass : '';
            }
            return '';
        },
        //指定不可选择的日期
        isInDisabledDate: function(date){
            var disabledDate = this.options.disabledDate;

            return ($.inArray(date, disabledDate) != -1) ? true : false;
        },
        isInEnabledDate: function(date){
            var enabledDate = this.options.enabledDate;
            if(enabledDate.length <= 0){
                return true;
            }
            return ($.inArray(date, enabledDate) != -1) ? true : false;
        },
        //返回一个字符两位数字模式
        getNumTwoBit:function(str){
            if (str) {
                var str = parseFloat(str);
                return str < 10 ? '0'+str : str;
            }
        },
        //切换至上个月
        onPrevMonth:function(){
            var me = this;
            this.date.reduceMonths(1);
            if(this.options.isDouble) this.dateDouble.reduceMonths(1);
            me.rander(null, function (){
                if(me.options.onPrevMonth) me.options.onPrevMonth(me.el);
            },'prev');
        },
        //切换至下个月
        onNextMonth:function(){
            var me = this;
            this.date.addMonths(1);
            if(this.options.isDouble) this.dateDouble.addMonths(1);
            me.rander(null, function (){
                if(me.options.onNextMonth) me.options.onNextMonth(me.el);
            },'next');
        },
        getHMS: function(){
            var opts = this.options;
            var newDate = new Date();
            var h = newDate.format('hh');
            var m = newDate.format('min');
            var s = newDate.format('ss');
            var footer = this.calendar.find('.'+opts.footerClass);

            if ( !isBlank(footer) ) {
                if (opts.changeHour) {
                    h  = footer.find('.hour').val();
                }

                if (opts.changeMinute) {
                    m  = footer.find('.minute').val();
                }
                if (opts.changeSecond) {
                    s  = footer.find('.second').val();
                }
            }
            return [h,m,s];
        },
        //设置日期的分/时/秒为当前时间的分/时/秒
        getTime:function(dataObj, index){
            //var dateFormat = "yyyy/mm/dd" +' '+h +':'+m +':'+s+noon;
            var dateFormat = "yyyy/mm/dd" +' '+ this.getHMS().join(':');
            return new Date(dataObj.format(dateFormat));
        },
        /**
         * 设置选中的日期输出
         */
        setSelectDate:function(dateObj, index){
            var _this = this;
            var opts = _this.options;
            //dateObj = _this.getTime(dateObj, index);
            //克隆操作对象，不影响已选择的对象
            _this.currentDate = _this.getTime(dateObj);
            var date = _this.currentDate.format(opts.format);
            _this.el.val(date);
            _this.hide();
            if(opts.onSelectDate) opts.onSelectDate(date, _this.el);
        },
        /**
         * 显示日历框
         * @method show
         */
        show:function( event ){
            var me = this;
            $('.ui-calendar').hide();
            var data = me.currentDate;
            /**
             * 如果可以编辑，则从输出框中获取日期作为当前日期
             * 注：此日期与初始化日期不一样。 暂时不开放
             */
                //if ( !me.options.readonly ) {
                //    data = data.parseDate($.trim(me.el.val()), me.options.format) || data;
                //}
            this.dateInit(data);
            this.rander(true, function(){
                me.calendar.show();
                if(me.options.onShow) me.options.onShow(me.el);
            });

            //添加点空白隐藏事件
            me.addDocumentHandler(event);
        },
        /**
         *  隐藏日历框
         * @method hide
         */
        hide:function(){
            this.calendar.hide();
            if(this.options.onHide) this.options.onHide(this.el);
            //删除点空白隐藏事件
            this.removeDocumentHandler();
        },
        documentHandler:null,
        /**
         * 添加点空白隐藏事件
         * @param event
         */
        addDocumentHandler:function( event ){
            var me = this;
            if ( !me.documentHandler ) {
                me.documentHandler = function(event){
                    /**FIX 修复ie6-8在下拉框选择内容时，窗口会关闭; form.select.live('change')*/
                    if (  !event.isPropagationStopped() && !$.contains( me.calendar[0], event.target ) && $(me.calendar[0]).get(0) != $(event.target).get(0) ) {
                        me.hide();
                    }
                };
            } else {
                me.removeDocumentHandler();
            }
            /**避免事件冒泡*/
            if ( event ) {
                event.stopPropagation();
            }
            $(document).bind('click', me.documentHandler);
        },
        removeDocumentHandler:function(){
            $(document).unbind('click', this.documentHandler);
        },
        setBeginDate:function(newDate){
            var me = this;
            var opts = me.options;
            var date = null;
            var beginDate = null;
            if ( !isBlank(newDate) ) {
                //显示用的
                date = new Date().parseDate(newDate, opts.format, null);
                //限制用的，不要和date共用一个！
                beginDate = date.clone();

                //设置为大于限制日期时间
                if (opts.changeSecond) {
                    date.setSeconds(date.getSeconds() + 1);
                } else if (opts.changeMinute) {
                    date.setMinutes(date.getMinutes() + 1);
                } else if (opts.changeHour) {
                    date.setHours(date.getHours() + 1);
                } else {
                    //只选择到天时（yyyy-mm-dd ），设置为小于限制日期，同时差开一天
                    date.setDate(date.getDate() + 1);
                }
            }
            if ( date) {
                me.beginDate = beginDate;
                opts.beginDate = beginDate.format(opts.format);
                if ( isBlank(me.el.val()) ) {
                    me.setValue(date);
                }
            }
        },
        setEndDate:function(newDate){
            var me = this;
            var opts = me.options;
            var date = null;
            var endDate = null;
            if ( !isBlank(newDate) ) {
                //显示用的
                date = new Date().parseDate(newDate, opts.format, null);
                endDate = date.clone();

                //只选择到天时（yyyy-mm-dd ），设置为小于限制日期，同时差开一天
                if (opts.changeSecond) {
                    date.setSeconds(date.getSeconds() - 1);
                } else if (opts.changeMinute) {
                    date.setMinutes(date.getMinutes() - 1);
                } else if (opts.changeHour) {
                    date.setHours(date.getHours() - 1);
                } else {
                    //只选择到天时（yyyy-mm-dd ），设置为小于限制日期，同时差开一天
                    date.setDate(date.getDate() - 1);
                }
            }

            if ( date ) {
                me.endDate = endDate;
                opts.endDate = endDate.format(opts.format);
                if ( isBlank(me.el.val()) ) {
                    me.setValue(date);
                }
            }
        },
        setValue:function(newDate, format){
            var me = this;
            var date = null;
            if ( !isBlank(newDate) ) {
                date = new Date().parseDate(newDate, format || me.options.format, null);
            }
            if ( date ) {
                me.currentDate = date;
            }
        }
    });
})(jQuery);