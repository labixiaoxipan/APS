package com.benewake.system.entity.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 * @TableName aps_attendance
 */
@TableName(value ="aps_attendance")
@Data
public class ApsAttendanceVo implements Serializable {
    /**
     * 唯一标识
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工姓名
     */
    @ExcelProperty("员工姓名")
    @TableField(value = "employee_name")
    private String employeeName;

    /**
     * 日期
     */
    @ExcelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "date")
    private Date date;

    /**
     * 星期
     */
    @ExcelProperty("星期")
    @TableField(value = "day_of_week")
    private String dayOfWeek;

    /**
     * 是否为工作日
     */
    @ExcelProperty("是否为工作日")
    @TableField(value = "is_workday")
    private String isWorkday;

    /**
     * 出勤时间范围
     */
    @ExcelProperty("出勤时间范围")
    @TableField(value = "attendance_time_range")
    private String attendanceTimeRange;

    /**
     * 午休时间范围
     */
    @ExcelProperty("午休时间范围")
    @TableField(value = "lunch_break_time_range")
    private String lunchBreakTimeRange;

    /**
     * 晚饭时间范围
     */
    @ExcelProperty("晚饭时间范围")
    @TableField(value = "dinner_time_range")
    private String dinnerTimeRange;

    /**
     * 早会时间范围
     */
    @ExcelProperty("早会时间范围")
    @TableField(value = "morning_meeting_time_range")
    private String morningMeetingTimeRange;

    /**
     * 请假时间范围
     */
    @ExcelProperty("请假时间范围")
    @TableField(value = "leave_time_range")
    private String leaveTimeRange;

    /**
     * 有效出勤时间范围
     */
    @ExcelProperty("有效出勤时间范围")
    @TableField(value = "effective_attendance_time_range")
    private String effectiveAttendanceTimeRange;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}