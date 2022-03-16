package com.fzclub.justauth.ext.request;

import com.alibaba.fastjson.JSONObject;
import com.fzclub.justauth.ext.unum.MyAuthSource;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.enums.AuthUserGender;
import me.zhyd.oauth.enums.scope.AuthGitlabScope;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.utils.AuthScopeUtils;
import me.zhyd.oauth.utils.UrlBuilder;

/**
 * Desc: 自建Gitlab登录
 *
 * @author vincent109
 */
public class MyAuthGitlabRequest extends AuthDefaultRequest {

    public MyAuthGitlabRequest(AuthConfig config) {
        super(config, MyAuthSource.GITLABX);
    }

    public MyAuthGitlabRequest(AuthConfig config, AuthSource source, AuthStateCache authStateCache) {
        super(config, MyAuthSource.GITLABX, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        JSONObject object = JSONObject.parseObject(response);

        this.checkResponse(object);

        return AuthToken.builder()
                .accessToken(object.getString("access_token"))
                .refreshToken(object.getString("refresh_token"))
                .idToken(object.getString("id_token"))
                .tokenType(object.getString("token_type"))
                .scope(object.getString("scope"))
                .build();
    }

    private void checkResponse(JSONObject object) {
        // oauth/token 验证异常
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error_description"));
        }
        // user 验证异常
        if (object.containsKey("message")) {
            throw new AuthException(object.getString("message"));
        }
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String response = doGetUserInfo(authToken);
        JSONObject object = JSONObject.parseObject(response);

        this.checkResponse(object);

        return AuthUser.builder()
                .rawUserInfo(object)
                .uuid(object.getString("id"))
                .username(object.getString("username"))
                .nickname(object.getString("name"))
                .avatar(object.getString("avatar_url"))
                .blog(object.getString("web_url"))
                .company(object.getString("organization"))
                .location(object.getString("location"))
                .email(object.getString("email"))
                .remark(object.getString("bio"))
                .gender(AuthUserGender.UNKNOWN)
                .token(authToken)
                .source(source.toString())
                .build();
    }

    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(super.authorize(state))
                .queryParam("scope", this.getScopes("+", false, AuthScopeUtils.getDefaultScopes(AuthGitlabScope.values())))
                .build();
    }
}
