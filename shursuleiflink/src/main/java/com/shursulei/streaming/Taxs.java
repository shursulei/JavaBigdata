package com.shursulei.streaming;

public class Taxs {
    double start;
    double end;
    double before_taxs;
    double precent;

    public Taxs(){
    }

    public Taxs(double start, double end, double precent) {
        this.start = start;
        this.end = end;
        this.precent = precent;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public double getBefore_taxs() {
        return before_taxs;
    }

    public void setBefore_taxs(double before_taxs) {
        this.before_taxs = before_taxs;
    }

    public double getPrecent() {
        return precent;
    }

    public void setPrecent(double precent) {
        this.precent = precent;
    }
    public double calucate(double before_taxs,double start,double precent,double end){
        if(before_taxs<end){
            return (before_taxs-start)*(precent/100);
        }else{
            return (end-start)*(precent/100);
        }
    }


}
