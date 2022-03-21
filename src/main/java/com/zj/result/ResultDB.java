package com.zj.result;

/**
 * @Classname ResultDB
 * @Description
 * @Date 2022/3/21 12:05
 * @Created by lxf
 */
public class ResultDB {

    //db总量
    private long dbCount;

    //db为0的数量
    private long dbZero;

    //db较低的数量
    private long dbLow;

    //db中段的数量
    private long dbMid;

    //db较高的数量
    private long dbHigh;

    //有声音的占有比例
    private double proportion;

    public long getDbCount() {
        return dbCount;
    }

    public void setDbCount(long dbCount) {
        this.dbCount = dbCount;
    }

    public long getDbZero() {
        return dbZero;
    }

    public void setDbZero(long dbZero) {
        this.dbZero = dbZero;
    }

    public long getDbLow() {
        return dbLow;
    }

    public void setDbLow(long dbLow) {
        this.dbLow = dbLow;
    }

    public long getDbMid() {
        return dbMid;
    }

    public void setDbMid(long dbMid) {
        this.dbMid = dbMid;
    }

    public long getDbHigh() {
        return dbHigh;
    }

    public void setDbHigh(long dbHigh) {
        this.dbHigh = dbHigh;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public ResultDB() {
    }

    public ResultDB(long dbCount, long dbZero, long dbLow, long dbMid, long dbHigh, double proportion) {
        this.dbCount = dbCount;
        this.dbZero = dbZero;
        this.dbLow = dbLow;
        this.dbMid = dbMid;
        this.dbHigh = dbHigh;
        this.proportion = proportion;
    }





    @Override
    public String toString() {
        return "ResultDB{" +
                "dbCount=" + dbCount +
                ", dbZero=" + dbZero +
                ", dbLow=" + dbLow +
                ", dbMid=" + dbMid +
                ", dbHigh=" + dbHigh +
                ", proportion=" + proportion +
                '}';
    }


}
