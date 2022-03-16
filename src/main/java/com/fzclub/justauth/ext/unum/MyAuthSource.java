package com.fzclub.justauth.ext.unum;

import com.fzclub.justauth.ext.request.MyAuthGitlabRequest;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthDefaultRequest;

/**
 * Desc: 扩展内置应用所需要的url
 *
 * @author vincent109
 */
public enum MyAuthSource implements AuthSource {
    /***
     *  自建Gitlab
     *  http://git.xxxx.com 为gitlab地址
     */
    GITLABX {
        @Override
        public String authorize() {
            return "http://git.xxxx.com/oauth/authorize";
        }
        @Override
        public String accessToken() {
            return "http://git.xxxx.com/oauth/token";
        }
        @Override
        public String userInfo() {
            return "http://git.xxxx.com/api/v4/user";
        }
        @Override
        public Class<? extends AuthDefaultRequest> getTargetClass() {
            return MyAuthGitlabRequest.class;
        }
    };
    private MyAuthSource() {
    }
}
