package com.check.fruit.fruitcheck.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by yinwen on 11/5/2017.
 */

public class Record extends DataSupport {
    @Column(unique = true, defaultValue = "unknown")
    private String name; //record name

    private String type; //record type
    private float v0;
    private float v1;
    private float v2;
    private float v3;
    private float v4;
    private float v5;
    private float v6;
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public float getV0() {
        return v0;
    }

    public float getV1() {
        return v1;
    }

    public float getV2() {
        return v2;
    }

    public float getV3() {
        return v3;
    }

    public float getV4() {
        return v4;
    }

    public float getV5() {
        return v5;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setV0(float v0) {
        this.v0 = v0;
    }

    public void setV1(float v1) {
        this.v1 = v1;
    }

    public void setV2(float v2) {
        this.v2 = v2;
    }

    public void setV3(float v3) {
        this.v3 = v3;
    }

    public void setV4(float v4) {
        this.v4 = v4;
    }

    public void setV5(float v5) {
        this.v5 = v5;
    }

    public float getV6() {
        return v6;
    }

    public void setV6(float v6) {
        this.v6 = v6;
    }
}
