package com.shursulei.springbase.aware;

import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class AwareService implements BeanNameAware, ResourceLoaderAware {
    private String beanName;
    private ResourceLoader loader;

    @Override
    public void setBeanName(String name) {
            this.beanName=name;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
            this.loader=resourceLoader;
    }

    public void outputResult(){
        System.out.println("Bean的名称:"+beanName);
        Resource resource=loader.getResource("classpath:com/shursulei/springbase/aware/test.txt");
        try {
            System.out.println("ResourceLoader加载的内容文件为"+IOUtils.toString(resource.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
