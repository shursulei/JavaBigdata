package com.shursulei.streaming;

public enum TaxgTest {
    A(1,5000,30),b(1,5000,30);
    private final double start;
    private final double end;
    private final double precent;
    private TaxgTest(double start,double end,double precent) {
        this.start = start;
        this.end = end;
        this.precent = precent;
    }
}
