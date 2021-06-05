package com.paraview.oauth.service.invoker;

import com.paraview.oauth.bean.OAuthReq;
import com.paraview.oauth.bean.Token;
import com.paraview.oauth.bean.User;
import com.paraview.oauth.client.ClientApp;
import com.paraview.oauth.context.CacheContext;
import com.paraview.oauth.context.ClientContext;
import com.paraview.oauth.enums.AuthType;
import com.paraview.oauth.enums.AuthorizeType;
import com.paraview.oauth.exception.AuthException;

public class AuthorizationCodeInvoker implements AuthInvoker {

    private ClientContext clientContext;

    public AuthorizationCodeInvoker(ClientContext context) {
        this.clientContext = context;
    }

    @Override
    public String grantType() {
        return AuthorizeType.AUTHORIZATION_CODE.value();
    }

    @Override
    public Token doInvoker(OAuthReq req, ClientApp clientApp) {
        User user = CacheContext.getCodeCache().get(req.getCode());
        if(user == null){
            throw new AuthException("code error.");
        }
        Token token = new Token(AuthType.BEARER.value(), 3600);
        CacheContext.getTokenCache().put(token.getAccess_token(), user);
        CacheContext.getCodeCache().remove(req.getCode());
        return token;
    }

    @Override
    public boolean checkAuth(ClientApp clientApp) {
        return clientContext.checkAuth(clientApp, grantType());
    }
}
