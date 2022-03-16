package com.fzclub.justauth.module;

import com.fzclub.justauth.ext.request.MyAuthGitlabRequest;
import io.swagger.annotations.Api;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGitlabRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Desc:JustAuth实现第三方登录
 *
 * @author vincent109
 */
@Api("oauth")
@At("/oauth")
@IocBean
public class OauthMudule {

    @Inject
    protected PropertiesProxy conf;
    /**
     * Description: 获取授权链接并跳转到第三方授权页面
     * @param oauthType  登录类型
     */
    @At("/login/?")
    @Ok("re")
    public String login(String oauthType){
        AuthRequest authRequest = getAuthRequest(oauthType);
        return "redirect:" + authRequest.authorize(AuthStateUtils.createState());
    }
    /**
     * Description: 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
     * @date 2022/3/16 0016 上午 10:19
     * @param oauthType  登录类型
     * @param callback  第三方回调时的入参
     * @return 第三方平台的用户信息
     */
    @At("/callback/?")
    @Ok("json")
    public Object callback(String oauthType, @Param("..")AuthCallback callback){
        AuthRequest authRequest = getAuthRequest(oauthType);
        return authRequest.login(callback);
    }
    /***
     * Description: 获取Request
     * @author vincent109
     * @date 2022/3/16 0016 上午 10:34
     * @param oauthType
     */
    private AuthRequest getAuthRequest(String oauthType) {
        String authSource = oauthType.toUpperCase();
        switch (authSource){
            case "GITLAB":
                return getGitlabRequest();
            case "GITEE":
                return getGiteeRequest();
            case "GITLABX":
                return getMyGitlabRequest();
            default:
                throw new RuntimeException("暂不支持的第三方登录.");
        }
    }
    /**
     * Gitlab
     */
    private AuthRequest getGitlabRequest(){
        AuthConfig authConfig = AuthConfig.builder()
                .clientId(conf.get("gitlab.clientId"))
                .clientSecret(conf.get("gitlab.clientSecret"))
                .redirectUri(conf.get("gitlab.redirectUri"))
                .build();
        return new AuthGitlabRequest(authConfig);
    }
    /**
     * Gitee
     */
    private AuthRequest getGiteeRequest(){
        AuthConfig authConfig = AuthConfig.builder()
                .clientId(conf.get("gitee.clientId"))
                .clientSecret(conf.get("gitee.clientSecret"))
                .redirectUri(conf.get("gitee.redirectUri"))
                .build();
        return new AuthGiteeRequest(authConfig);
    }
    /**
     * MyGitlab
     */
    private AuthRequest getMyGitlabRequest(){
        AuthConfig authConfig = AuthConfig.builder()
                .clientId(conf.get("gitlabx.clientId"))
                .clientSecret(conf.get("gitlabx.clientSecret"))
                .redirectUri(conf.get("gitlabx.redirectUri"))
                .build();
        return new MyAuthGitlabRequest(authConfig);
    }


}
