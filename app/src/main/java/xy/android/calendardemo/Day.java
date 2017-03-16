/*
 * Copyright (c) 2017.
 * You can access the web site to find information
 * httpyipai.love
 *
 * 上海约拍网络科技有限公司 版权所有.
 */

package xy.android.calendardemo;

import java.io.Serializable;

public class Day implements Serializable {

    private int year;
    private int month;
    private boolean enable; //是否可选
    private boolean isSingleChosen; //是否单选
    private boolean isOrder; //是否被选
    private boolean isFirst; //是否是起始时间
    private boolean isLast; //是否是终止时间
    private int day; //天数名称

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isSingleChosen() {
        return isSingleChosen;
    }

    public void setSingleChosen(boolean singleChosen) {
        isSingleChosen = singleChosen;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
