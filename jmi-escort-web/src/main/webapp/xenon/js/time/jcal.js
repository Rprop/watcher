/**
 * 日历组件，两个jQuery插件Jcal和Cal，分别双月和单月显示
 *
 * **参数**
 *    
 *  startDate    {String|Date} 起始日期，此前的日期不可点
 *  endDate      {String|Date} 结束日期，此后的日期不可点
 *  choseDate    {String|Date} 当前高亮显示的日期
 *  currCls      {String}   指定当前li的className
 *  showDay      {Boolean}  是否在输入框显示星期几，如“周四”
 *  showFestival {Boolean}  是否显示节假日
 *  fillInputVal {Boolean}  是否自动回填input的值
 *  diffX        {Number}   距离输入框X轴的位置调整
 *  diffY        {Number}   距离输入框Y轴的位置调整
 *  fixScroll    {String}   (undefined|fix|hide) 滚动条滚动时处理方式，可选。默认不处理，fix自动调整位置，hide则隐藏
 * 
 * **方法**
 *    showCal    手动调用该方法来显示日历（用在非点击输入框INPUT时弹出日历）

 * **事件**
 *  choose  选中后触发，包括点击选中和enter
 *
 * **更新日志**
 * 2015.5.6 单日历闰年判断bug，1972年出现的天都是29天
 *
 */
~function($) {

var now = new Date()
// 一周，从周日开始
var week = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
// 月，闰年2月29天
var months = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
// 2014-11-09 
var reDate = /^\d{4}\-\d{1,2}\-\d{1,2}/
// 阳历节日
var solarFestival = {
    '0101': '元旦',
    '0405': '清明',
    '0501': '五一',
    '1001': '国庆'
}
//农历节日
var lunarFestival = {
    '0100': '除夕',
    '0101': '春节',
    '0505': '端午',
    '0815': '中秋'
}
/*
 * 农历数据表
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
]
/*
 * 补齐数字位数
 * @param {number|string} n 需要补齐的数字
 * @return {string} 补齐两位后的字符
 */
function getTwoBit(n) {
    return (n > 9 ? '' : '0') + n
}
/*
 * 日期字符串转成Date对象
 * @param {String} str
 *   "2014-12-31" 
 *   "2014/12/31"
 * @return {Date} 
 */
function str2Date(str) {
    if (reDate.test(str)) {
        str = str.replace(/-/g, '/')
    }
    return new Date(str)
}
/*
 * 日期对象转成字符串
 * @param {Date} new Date()
 * @return {string} "2014-12-31" 
 */
function date2Str(date, split) {
    if (typeof date == 'string') return date
    split = split || '-'
    var y = date.getFullYear()
    var m = getTwoBit(date.getMonth() + 1)
    var d = getTwoBit(date.getDate())
    return [y, m, d].join(split)    
}
/*
 * 判断是否闰年
 * @param {number} year 年
 * @return {boolean}
 */
function isLeapYear(year) {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}
/*
 * 获取节日或节气信息
 * @param {Date} date 阳历日期对象
 * @return {String} 节日或节气信息
 */
function getFestival(date) {
    var lunar = getLunarInfo(date)
    return getLunarFestival(lunar) || getSolarFestival(date)
}
/*
 * 获取阳历节日
 * @param {Date} date 阳历日期对象
 * @return {Object} 阳历节日
 */
function getSolarFestival(date) {
    var text = solarFestival[getTwoBit(date.getMonth() + 1) + getTwoBit(date.getDate())]
    return text ? text : null
}
/*
 * 获取农历节日
 * @param {Object} lunar 农历日期信息
 * @return {Object} 农历节日
 */
function getLunarFestival(lunar) {
    // 处理除夕
    if (lunar.month == 11 && lunar.day > 28) {
        var next =  new Date(lunar.solar.getTime() + 1000 * 60 * 60 * 24)
        next = getLunarInfo(next)
        if (next.day === 1) {
            lunar.month = 0
            lunar.day = 0
        }
    }
    var text = lunar.leap ? ''
        : lunarFestival[getTwoBit(lunar.month + 1) + getTwoBit(lunar.day)] || ''

    return text ? text : null
}
/*
 * 获取阳历对应的农历信息
 * @param {Date} date 阳历日期对象
 * @return {Object} 农历信息
 */
function getLunarInfo(date) {
    var i    = 0
    var leap = 0
    var temp = 0
    var offset = (
        Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()) - Date.UTC(1900, 0, 31)
    ) / 86400000

    for (i = 1900; i < 2100 && offset > 0; i++) {
        temp = lYearDays(i)
        offset -= temp
    }
    if (offset < 0) {
        offset += temp
        i--
    }

    var year = i
    leap = leapMonth(i) //闰哪个月
    var isLeap = false
    for (i = 1; i < 13 && offset > 0; i++) {
        // 闰月
        if (leap > 0 && i === leap + 1 && !isLeap) {
            --i
            isLeap = true
            temp = leapDays(year)
        } else { 
            temp = monthDays(year, i)
        }
        // 解除闰月
        if (isLeap && i === leap + 1) {
            isLeap = false
        }
        offset -= temp
    }
    if (offset === 0 && leap > 0 && i === leap + 1) {
        if (isLeap) {
            isLeap = false
        } else {
            isLeap = true
            --i
        }
    }
    if (offset < 0) {
        offset += temp
        --i
    }
    return {
        year: year, 
        month: i - 1, 
        day: offset + 1, 
        leap: isLeap, 
        solar: date
    }
}
/*
 * 返回农历 y 年的总天数
 * @param {Number} y 年份
 * @return {Number} y 年的总天数
 */
function lYearDays(y) {
    var days = 348 + (lunarInfo[y - 1900] >> 4).toString(2).replace(/0/g, '').length
    return days + leapDays(y)
}
/*
 * 返回农历 y 年闰月的天数
 * @param {Number} y 年份
 * @return {Number} 闰月的天数（大月30，小月29，无闰月0）
 */
function leapDays(y) {
    return leapMonth(y) ? (lunarInfo[y - 1899] & 0xf) === 0xf ? 30 : 29 : 0
}
/*
 * 返回农历 y 年闰哪个月 1-12 , 没闰返回 0**
 * @param {Number} y 年份
 * @return {Number} 闰月月份，0为不闰
 */
function leapMonth(y) {
    var lm = lunarInfo[y - 1900] & 0xf
    return lm === 0xf ? 0 : lm
}
/*
 * 返回农历 y 年 m 月的总天数
 * @param {Number} y 年份
 * @param {Number} m 月份
 * @return {Number} 农历 y 年 m 月的天数（大月30，小月29）
 */
function monthDays(y, m) {
    return (lunarInfo[y - 1900] & (0x10000 >> m)) ? 30 : 29
}
/*
 * 格式化日期对象为字符串类型
 * @param {Date} 
 * @param {Boolean} 是否带上 “星期几”
 * @return {String} "2014-11-25" or "2014-11-25 星期二"
 */
function format(date, hasDay) {
    var arr, m, d, day
    if (typeof date === 'string') {
        date = str2Date(date)
    }
    var mo = getTwoBit(date.getMonth()+1)
    var da = getTwoBit(date.getDate())
    var str = date.getFullYear() + '-' + mo + '-' + da
    if (hasDay) {
        day = week[date.getDay()]
        str += ' ' + day
    }
    return str
}
/*
 * 设置日历的位置
 */
function setPosition($div, $input, diffX, diffY) {
    var posi = $input.offset()
    var outerHeight = $input.outerHeight()
    var left = (diffX ? diffX : 0) + posi.left
    var top  = (diffY ? diffY : 0) + posi.top + outerHeight
    $div.css({
        position: 'absolute',
        left: left,
        top: top
    })
}
/*
 * 获取当前被选择的日期
 */
function getChoseDate($input, setting) {
    var val = $input.val()
    var res = reDate.exec(val)
    // 点击时弹出日历
    var choseDate = setting.choseDate
    // 输入框有值时以该值为选择值，否则以当天改默认选择值
    if (res) {
        choseDate = res[0]
    }
    // 初始化日历被选定天
    if (reDate.test(choseDate)) {
        choseDate = str2Date(choseDate)
    }
    return choseDate
}
// common dom
var $win  = $(window)
var $doc  = $(document)
var $body = $('body')

/*
 * Jcal 双月日历组件
 */
$.fn.Jcal = function(option, callback) {
    var setting = $.extend({}, $.fn.Jcal.defaults, option)

    // alias
    var diffX     = setting.diffX
    var diffY     = setting.diffY
    var showDay   = setting.showDay
    var fixScroll = setting.fixScroll
    var showFestival = setting.showFestival
    var fillInputVal = setting.fillInputVal

    /*
     * 生成日历的HTML模板
     */
    function template() {
        var tableHTML = '<table cellpadding="0" cellpadding="0" class="datepicker">'
        var theadHTML = '<thead>'
        var tbodyHTML = '<tbody>'
        var tfootHTML = '<tfoot>'
        var trHTML    = '<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>'
        var table1 = $(tableHTML)
        var table2 = $(tableHTML)
        table1.append(theadHTML).append(tfootHTML).append(tbodyHTML)
        table2.append(theadHTML).append(tfootHTML).append(tbodyHTML)
        $('thead', table1).append('<tr class="controls"><th colspan="7"><span class="prevMonth"><s></s></span><span class="currDate"><span class="currYs"></span>年<span class="currMo"></span>月</span></th></tr>')
        $('thead', table1).append('<tr class="days"><th class="org sun">日</th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th class="org sat">六</th></tr>')
        $('tfoot', table1).append('<tr><td colspan="7"><span class="today">今天</span></td></tr>')
        for (var o = 0; o < 6; o++) {
            $('tbody', table1).append(trHTML)
        }
        $('thead', table2).append('<tr class="controls"><th colspan="7"><span class="nextMonth"><s></s></span><span class="currDate"><span class="currYs"></span>年<span class="currMo"></span>月</span></span></th></tr>')
        $('thead', table2).append('<tr class="days"><th class="org sun">日</th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th class="org sat">六</th></tr>')
        $('tfoot', table2).append('<tr><td colspan="7"><span class="close">关闭</span></td></tr>')
        for (var i = 0; i < 6; i++) {
            $('tbody', table2).append(trHTML)
        }
        return $('<div class="o-datepicker">').append(table1).append(table2)
    }
    /*
     * 设置年月
     */
    function setYearMonth($div, date) {
        var $tables = $div.find('table')
        // 左边的日历
        var $table1 = $tables.eq(0)
        var $year1  = $table1.find('.currYs')
        var $month1 = $table1.find('.currMo')
        // 右边的日历
        var $table2 = $tables.eq(1)
        var $year2  = $table2.find('.currYs')
        var $month2 = $table2.find('.currMo')
        // 获取当前的月年
        var month  = date.getMonth()
        var year   = date.getFullYear()
        if (month == 11) {
            $year1.text(year)
            $month1.text(month + 1)
            $year2.text(year + 1)
            $month2.text(1)
        } else {
            $year1.text(year)
            $month1.text(month + 1)
            $year2.text(year)
            $month2.text(month + 2)
        }
    }
    /*
     * 上一个/下一个 月
     */
    function prevNextMonth($div, $input, isPrev) {
        var $table = $div.find('table').first()
        var $year  = $table.find('.currYs')
        var $month = $table.find('.currMo')
        var year   = $year.text() - 0
        var month  = $month.text() - (isPrev ? 2 : 0)
        var date = new Date(year, month, 1)
        setYearMonth($div, date)
        fillDate($div, $input)
    }
    /*
     * 绘制日历每天
     */
    function fillDate($div, $input) {
        var startDate = setting.startDate
        var endDate   = setting.endDate
        var currDate = $input.data('currDate')
        $div.find('table').each(function(i, table) {
            var $table  = $(table)
            var $tds    = $table.find('tbody td').unbind().empty().removeClass()
            var cYear   = $table.find('.currYs').text() - 0
            var cMonth  = $table.find('.currMo').text() - 1
            var cDate   = new Date(cYear, cMonth, 1)
            var week    = cDate.getDay()
            var start   = 0
            var hasDate = true
            var days1   = months[cMonth]
            var days2   = months[cMonth]
            // 闰年的2月
            if (cMonth == 1 && isLeapYear(cYear)) {
                days2 = 29
            }
            // 高亮当前
            for (var i = 0; i < days2; i++) {
                var $td = $tds.eq(i + week)
                var rday = i + 1
                var festival = getFestival(new Date(cYear, cMonth, rday))
                if (showFestival && festival) {
                    $td.html('<span class="festival">' + festival + '</span>')
                } else {
                    $td.text(rday)
                }
                $td.attr('day', rday)
                if (i+1 == currDate.getDate() && cMonth == currDate.getMonth() && cYear == currDate.getFullYear()) {
                    $td.addClass('chosen')
                }
            }
            if (startDate && reDate.test(startDate)) {
                var arr   = startDate.split('-')
                var year  = arr[0] - 0
                var month = arr[1] - 1
                var day   = arr[2] - 1
                if (cMonth == month && cYear == year) {
                    start = day
                }
                if (cYear < year || cMonth < month && cYear <= year) {
                    hasDate = false
                }                
            }
            if (endDate && reDate.test(endDate)) {
                var arr   = endDate.split('-')
                var year  = arr[0] - 0
                var month = arr[1] - 1
                if (cMonth == month && cYear == year) {
                    days1 = arr[2]
                }
                if (cYear > year || cMonth > month && cYear == year) {
                    hasDate = false
                }
            }
            if (hasDate) {
                for (var u = start; u < days2; u++) {
                    var $td = $tds.eq(u + week)
                    $td.addClass('date')
                }
            }
        })
    }
    /*
     *  日历事件
     */
    function addEvent($div, $input) {
        $div.delegate('.date', 'click', function(ev) {
            var $td     = $(this)
            var $table  = $td.closest('table')
            var year    = $table.find('.currYs').text()
            var month   = $table.find('.currMo').text() - 1
            var day     = $td.attr('day')
            var dateObj = new Date(year, month, day)
            $input.data('currDate', dateObj)
            $input.val(format(dateObj, showDay))
            $div.hide()
            $div.trigger('choose', [format(dateObj), $input])
        })
        .delegate('.date', 'mouseover', function() {
            $(this).addClass('over')
        })
        .delegate('.date', 'mouseout', function() {
            $(this).removeClass('over')
        })
        .delegate('.today', 'click', function() {
            var date = new Date()
            $input.data('currDate', date)
            $input.val(format(date, showDay))
            $div.hide()
            $div.trigger('choose', [format(date), $input])
        })
        .delegate('.close', 'click', function() {
            $div.hide()
        })
        .delegate('.prevMonth', 'click', function(ev) {
            var $span = $(this)
            if ($span.hasClass('disabled')) {
                return false
            }
            prevNextMonth($div, $input, true)
        })
        .delegate('.nextMonth', 'click', function(ev) {
            var $span = $(this)
            if ($span.hasClass('disabled')) {
                return false
            }
            prevNextMonth($div, $input, false)
        })
        $win.bind('resize', {div: $div, input: $input}, onResize)
        $doc.bind('click', {div: $div, input: $input}, onBody)
        switch (fixScroll) {
            case 'fix': 
                $win.bind('scroll', {div: $div, input: $input}, onResize)
                break
            case 'hide':
                $win.bind('scroll', {div: $div, input: $input}, function(ev) {
                    var $div = ev.data.div
                    var $input = ev.data.input
                    $div.hide()       
                })
                break
            default: ;
        }
    }
    function onBody(ev) {
        var $div = ev.data.div
        var $input = ev.data.input
        var $target = $(ev.target)
        if (!$target.parents('.datepicker').length && $target[0] != $input[0]) {
            $div.hide()
        }
    }
    function onResize(ev) {
        var $div = ev.data.div
        var $input = ev.data.input
        setPosition($div, $input, diffX, diffY)
    }
    function bootstrap($input) {
        var $div = template()
        var val = $input.val()
        var choseDate = getChoseDate($input, setting)
        // $div存在popui
        $input.data('Jcal', $div)
        // 是否回填输入框
        if (fillInputVal) {
            $input.val(format(choseDate, showDay))
        }        
        // method
        $div.setOption = function(option) {
            for (var a in option) {
                setting[a] = option[a]
            }
        }
        $div.setRange = function(startDate, endDate) {
            setting.startDate = startDate
            setting.endDate   = endDate
        }        
        $div.showCal = showCal
        function init() {
            var choseDate = getChoseDate($input, setting)
            // 设置年月
            setYearMonth($div, choseDate)
            // 缓存当前日期
            $input.data('currDate', choseDate)
            // 设置日期弹层位置
            setPosition($div, $input, diffX, diffY)
            // 第一次渲染
            fillDate($div, $input)
            $div.show()
        }
        function showCal() {
            if ($input.data('hasCal')) {
                init()
            } else {
                init()
                addEvent($div, $input)
                $body.prepend($div)
                $input.data('hasCal', 1)
            }
        }
        $input.click(function() {
            showCal()
        })
    }
    return this.each(function() {
        var $input = $(this)
        bootstrap($input)
        if ($.isFunction(callback)) callback($input)
    })
}

$.fn.Jcal.format = format
$.fn.Jcal.defaults = {
    startDate: null,  
    endDate: null,      
    choseDate: now,
    showDay: false,
    showFestival: true,
    fillInputVal: true,    
    diffX: 0,
    diffY: 0,
    fixScroll: ''
}

/**
 * Cal 单月日历组件
 */
$.fn.Cal = function(option, callback) {
    var setting = $.extend({}, $.fn.Cal.defaults, option)

    // alias
    var diffX     = setting.diffX
    var diffY     = setting.diffY
    var showDay   = setting.showDay
    var showToday = setting.showToday
    var fixScroll = setting.fixScroll
    var showFestival = setting.showFestival
    var fillInputVal = setting.fillInputVal

    // date对象转成字符串格式
    setting.startDate = date2Str(setting.startDate)
    setting.endDate   = date2Str(setting.endDate)
    /*
     * 生成日历的HTML模板
     */
    function template() {
        var tableHTML = '<table cellspacing="0" cellpadding="0">'
        var theadHTML = ''
                +'<thead>'
                +   '<tr class="controls"><th colspan="7">'
                +       '<a href="javascript:;" class="prevYear ctrl-prev">«</a>'
                +       '<a href="javascript:;" class="prevMonth ctrl-prev">‹</a>'
                +       '<span class="selectMonth">'
                +           '<select class="month"></select>'
                +       '</span>'
                +       '<span class="slecteYear">'
                +           '<select class="year"></select>'
                +       '</span>'
                +       '<a href="javascript:;" class="nextYear ctrl-next">»</a>'
                +       '<a href="javascript:;" class="nextMonth ctrl-next">›</a>'
                +   '</th></tr>'
                +   '<tr class="days">'
                +       '<th class="J-sun">日</th>'
                +       '<th class="J-mon">一</th>'
                +       '<th class="J-tue">二</th>'
                +       '<th class="J-wed">三</th>'
                +       '<th class="J-thu">四</th>'
                +       '<th class="J-fri">五</th>'
                +       '<th class="J-sat">六</th>'
                +   '</tr>'
                +'</thead>' 
        var tbodyHTML = '<tbody>'
        var trHTML    = '<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>'
        var tfootHTML = showToday ? '<tfoot><tr><td colspan="7"><span class="today">今天</span></td></tr></tfoot>' : ''
        var table = $(tableHTML)
        table.append(theadHTML).append(tbodyHTML).append(tfootHTML)
        for (var o = 0; o < 6; o++) {
            $('tbody', table).append(trHTML)
        }
        return $('<div class="Cal">').append(table)
    }
    /*
     * 设置年月
     */
    function setYearMonth($div, date) {
        var $table = $div.find('table')
        var $year  = $table.find('.year')
        var $month = $table.find('.month')
        var month  = date.getMonth()
        var year   = date.getFullYear()
        $year[0].selectedIndex = $year.find('[value="' + year + '"]').attr('idx')
        // change年时保持月不变
        var $option  = $month.find('[value="' + month + '"]')
        var index    = $option.index()
        if ($option.length) {
            $month[0].selectedIndex = index
        } else {
            $month[0].selectedIndex = 0    
        }
        $month.attr('data-val', month)
    }
    /*
     * 绘制日历
     * 1. 插入天
     * 2. 当前选择的会给一个高亮样式
     * 3. 不在起始和结束日期范围的点击无效
     */
    function fillDate($div, $input) {
        var startDate = setting.startDate
        var endDate   = setting.endDate
        var curDate   = $input.data('currDate')
        var $table    = $div.find('table')
        var $tds      = $table.find('tbody td').unbind().empty().removeClass()
        var cYear     = $table.find('.year').val()  - 0
        var cMonth    = $table.find('.month').val() - 0
        var cDate     = new Date(cYear, cMonth, 1)
        var week      = cDate.getDay()
        var start     = 0
        var hasDate   = true
        var days      = months[cMonth]
        // 闰年的2月
        if (cMonth == 1 && isLeapYear(cYear)) {
            days = 29
        }
        // 填入日期并给当前的日期添加高亮样式
        for (var i = 0; i < days; i++) {
            var $td = $tds.eq(i + week)
            var rday = i + 1
            var festival = getFestival(new Date(cYear, cMonth, rday))
            if (showFestival && festival) {
                $td.html('<span class="festival">' + festival + '</span>')
            } else {
                $td.text(rday)
            }
            $td.attr('day', rday)
            if (i+1 == curDate.getDate() && cMonth == curDate.getMonth() && cYear == curDate.getFullYear()) {
                $td.addClass('chosen')
            }
        }
        if (startDate && reDate.test(startDate)) {
            var arr   = startDate.split('-')
            var year  = arr[0] - 0
            var month = arr[1] - 1
            var day   = arr[2] - 1
            if (cMonth == month && cYear == year) {
                start = day
            }
            if (cYear < year || cMonth < month && cYear <= year) {
                hasDate = false
            }
        }
        if (endDate && reDate.test(endDate)) {
            var arr   = endDate.split('-')
            var year  = arr[0] - 0
            var month = arr[1] - 1
            if (cMonth == month && cYear == year) {
                days = arr[2]
            }
            if (cYear > year || cMonth > month && cYear == year) {
                hasDate = false
            }
        }
        if (hasDate) {
            for (var u = start; u < days; u++) {
                var $td = $tds.eq(u + week)
                $td.addClass('date')
            }
        }

        setButtonStatus($div)
    }
    /*
     * 获取起始和结束年，默认从配置参数取，如果没有配置则以当前时间向前向后推5年
     */
    function getStartEndYear() {
        var start, end, startYear, endYear
        var startStr = setting.startDate
        var endStr   = setting.endDate
        if (startStr) {
            start     = str2Date(startStr)
            startYear = start.getFullYear()
        } else {
            startYear = now.getFullYear() - 5
            start     = new Date(startYear, 1, 1)
        }
        if (endStr) {
            end       = str2Date(endStr)
            endYear   = end.getFullYear()
        } else {
            endYear   = now.getFullYear() + 5
            end       = new Date(endYear, 12, 31)
        }
        return {
            start: start,
            end: end,
            startYear: startYear,
            endYear: endYear
        }
    }
    /*
     * 填入年Select
     */
    function fillYearSelect($div) {
        var obj = getStartEndYear()
        var year, i = 0, str = ''
        for (year = obj.startYear; year <= obj.endYear; year++) {
            str += '<option value="' + year + '" idx="' + (i++) + '">' + year + '</option>'
        }
        $div.find('select.year').html(str)
    }
    /*
     * 填入月Select
     */
    function fillMonthSelect($div, year) {
        var month, options = ''
        var obj = getStartEndYear()
        var startMonth = obj.start.getMonth() + 1
        var endMonth   = obj.end.getMonth() + 1
        var $month     = $div.find('select.month')
        var val        = $month.attr('data-val')
        var concat = function(m) {
            options += '<option value="' + (m-1) +'">' + m + '月</option>'            
        }
        if (year == obj.startYear && year == obj.endYear) {
            for (month = startMonth; month <= endMonth; month++) {
                concat(month)
            }
            $month.html(options)
        } else if (year == obj.startYear) {
            for (month = startMonth; month <= 12; month++) {
                concat(month)
            }
            $month.html(options)
        } else if (year == obj.endYear) {
            for (month = 1; month <= endMonth; month++) {
                concat(month)
            }
            $month.html(options)
            return
        } else {
            for (month = 1; month <= 12; month++) {
                concat(month)
            }
            $month.html(options)            
        }
        var $option  = $month.find('[value="' + val + '"]')
        var index    = $option.index()
        if ($option.length) {
            $month[0].selectedIndex = index
        } else {
            $month[0].selectedIndex = 0    
        }
        $month.attr('data-val', val)        
    }
    /*
     * 激活或禁用按钮状态
     */
    function setButtonStatus($div) {
        var $prevYear  = $div.find('.prevYear')        
        var $prevMonth = $div.find('.prevMonth')
        var $nextYear  = $div.find('.nextYear')        
        var $nextMonth = $div.find('.nextMonth')
        var elYear     = $div.find('select.year')[0]
        var elMonth    = $div.find('select.month')[0]
        var year       = elYear.value
        var month      = elMonth.value
        var start      = setting.startDate.split('-')
        var end        = setting.endDate.split('-')
        var startYear  = start[0]        
        var startMonth = start[1] - 1
        var endYear    = end[0]       
        var endMonth   = end[1] - 1    
        // 第1年
        if (year == startYear) {
            $prevYear.addClass('disable')
            month == startMonth
                ? $prevMonth.addClass('disable')
                : $prevMonth.removeClass('disable')
        } else {
            $prevYear.removeClass('disable')
            $prevMonth.removeClass('disable')
        }
        // 最后1年
        if (year == endYear) {
            $nextYear.addClass('disable')
            month == endMonth
                ? $nextMonth.addClass('disable')
                : $nextMonth.removeClass('disable')
        } else {
            $nextYear.removeClass('disable')
            $nextMonth.removeClass('disable')            
        }
    }    
    /*
     * 日历上所有的事件，多数事件采用事件代码方式
     */
    function addEvent($div, $input) {
        var $prevMonthBtn = $div.find('.prevMonth')
        var $prevYearBtn  = $div.find('.prevYear')
        var $nextMonthBtn = $div.find('.nextMonth')
        var $nextYearBtn  = $div.find('.nextYear')
        $div.find('select.month').change(function() {
            var $select = $(this)
            var val = $select.val()
            $select.attr('data-val', val)
            fillDate($div, $input)
        })
        $div.find('select.year').change(function() {
            var year = $(this).val() - 0
            fillMonthSelect($div, year)
            fillDate($div, $input)
        })
        // 上一年
        $div.delegate('.prevYear', 'click', function() {
            var start     = setting.startDate.split('-')
            var startYear = start[0]
            var elYear    = $div.find('select.year')[0]
            var yIdx      = elYear.selectedIndex
            var year      = elYear.value
            if (year == startYear) {
                return
            }
            elYear.selectedIndex = --yIdx
            fillMonthSelect($div, elYear.value)
            fillDate($div, $input)
        })
        $div.delegate('.nextYear', 'click', function() {
            var end      = setting.endDate.split('-')
            var endYear  = end[0]
            var elYear   = $div.find('select.year')[0]
            var yIdx     = elYear.selectedIndex  
            var year     = elYear.value   
            if (year == endYear) {
                return
            }
            elYear.selectedIndex = ++yIdx
            fillMonthSelect($div, elYear.value)
            fillDate($div, $input)
        })
        // 上一个月，到达其实月时置灰
        $div.delegate('.prevMonth', 'click', function() {
            var start      = setting.startDate.split('-')
            var startYear  = start[0] - 0
            var startMonth = start[1] - 1
            var elYear     = $div.find('select.year')[0]
            var elMonth    = $div.find('select.month')[0]
            var yIdx       = elYear.selectedIndex
            var mIdx       = elMonth.selectedIndex            
            var year       = elYear.value
            var month      = elMonth.value
            if (year == startYear && month == startMonth) {
                return
            }
            if (month == 0) {
                fillMonthSelect($div, --year)
            }
            if (month == 0) {
                // 显示末位option
                elMonth.selectedIndex = elMonth.children.length-1
                elYear.selectedIndex  = --yIdx
            } else {
                elMonth.selectedIndex = --mIdx
            }
            fillDate($div, $input)
        })
        // 下一个月，到达结束月时置灰
        $div.delegate('.nextMonth', 'click', function() {
            var end        = setting.endDate.split('-')
            var endYear    = end[0] - 0
            var endMonth   = end[1] - 1            
            var elYear     = $div.find('select.year')[0]
            var elMonth    = $div.find('select.month')[0]
            var yIdx       = elYear.selectedIndex
            var mIdx       = elMonth.selectedIndex            
            var year       = elYear.value
            var month      = elMonth.value            
            if (year == endYear && month == endMonth) {
                return
            }
            if (month == 11) {
                fillMonthSelect($div, ++year)
            }
            if (month == 11) {
                // 显示首位option
                elMonth.selectedIndex = 0
                elYear.selectedIndex  = ++yIdx
            } else {
                elMonth.selectedIndex = ++mIdx
            }
            fillDate($div, $input)
        })
        // 选定日期回填到输入框
        $div.delegate('td.date', 'click', function() {
            var $td     = $(this)
            var $table  = $td.closest('table')
            var year    = $table.find('select.year').val()
            var month   = $table.find('select.month').val()
            var day     = $td.attr('day')
            var dateObj = new Date(year, month, day)
            // $input.data('currDate', new Date(year, month, day))
            $div.hide()
            $input.val(format(dateObj, showDay))
            $div.trigger('choose', [format(dateObj), {
                input: $input
            }])
        })
        $div.delegate('td.date', 'mouseover', function() {
            $(this).addClass('over')
        }).delegate('td.date', 'mouseout', function() {
            $(this).removeClass('over')
        })
        // 今天
        $div.delegate('.today', 'click', function() {
            $input.val(format(new Date(), showDay))
            $div.hide()
        })
        $win.bind('resize', {div: $div, input: $input}, onResize)
        $doc.bind('click', {div: $div, input: $input}, onBody)
    }
    function onBody(ev) {
        var $div = ev.data.div
        var $input = ev.data.input
        var $target = $(ev.target)
        if (!$target.parents('.Cal').length && $target[0] != $input[0]) {
            $div.hide()
        }
    }
    function onResize(ev) {
        var $div = ev.data.div
        var $input = ev.data.input
        setPosition($div, $input, diffX, diffY)
    }    
    // 入口函数
    function bootstrap($input) {
        var $div = template()
        // 是否回填输入框
        var choseDate = getChoseDate($input, setting)
        if (fillInputVal) {
            $input.val(format(choseDate, showDay))
        }
        // 注册到PopUI
        $input.data('Cal', $div)
        // 初始化
        function init() {
            var val       = $input.val()
            var res       = reDate.exec(val)
            var choseDate = getChoseDate($input, setting)
            var endDate   = setting.endDate
            // 当前日期如果大于结束日期，那么以结束日期为当前日期
            if (endDate) {
                endDate = str2Date(endDate)
            }
            if (choseDate > endDate) {
                choseDate = endDate
            }
            // 填充年的select
            fillYearSelect($div)
            fillMonthSelect($div, choseDate.getFullYear())
            // 设置年月
            setYearMonth($div, choseDate)
            // 缓存当前日期
            $input.data('currDate', choseDate)
            // 设置日期弹层位置
            setPosition($div, $input, diffX, diffY)
            // 第一次渲染
            fillDate($div, $input)
            $div.show()
        }
        function showCal() {
            if ($input.data('hasCal')) {
                init()
            } else {
                init()
                addEvent($div, $input)
                $body.prepend($div)
                $input.data('hasCal', 1)
            }
        }
        // method
        $div.showCal = showCal 
        $div.setOption = function(option) {
            for (var a in option) {
                setting[a] = option[a]
            }
        }
        $div.setRange = function(startDate, endDate) {
            setting.startDate = startDate
            setting.endDate   = endDate
        }
        $input.click(function() {
            showCal()
        })
    }
    return this.each(function() {
        var $input = $(this)
        bootstrap($input)
        if ($.isFunction(callback)) callback($input)
    })
}

$.fn.Cal.format = format
$.fn.Cal.defaults = {
    startDate: null,
    endDate: null,
    choseDate: date2Str(now),
    showDay: false,
    showToday: false,
    showFestival: true,
    fillInputVal: false,
    diffX: 0,
    diffY: 0,
    fixScroll: ''
}

}(jQuery);