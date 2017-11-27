package com.zhj.config;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhj on 2017/11/28.
 */
@Service
public class GeneratorConfig {

    private String packageHead;

    private List<String> validatedPackageHeads;

    public String getPackageHead() {
        return packageHead;
    }

    public void setPackageHead(String packageHead) {
        this.packageHead = packageHead;
    }

    public List<String> getValidatedPackageHeads() {
        return validatedPackageHeads;
    }

    public void setValidatedPackageHeads(List<String> validatedPackageHeads) {
        this.validatedPackageHeads = validatedPackageHeads;
    }
}
