package com.fzclub.justauth;

import lombok.extern.slf4j.Slf4j;
import org.nutz.boot.NbApp;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
@Slf4j
@IocBean(create="init", depose="depose")
public class MainLauncher {

    @At("/")
    @Ok("->:/index.html")
    public void index() {}

    public void init() {
        // NB自身初始化完成后会调用这个方法
        log.info("---->> app init");
    }
    public void depose() {
        log.info("---->> app depose");
    }

    public static void main(String[] args) throws Exception {
        new NbApp().setArgs(args).setPrintProcDoc(false).run();
    }

}
