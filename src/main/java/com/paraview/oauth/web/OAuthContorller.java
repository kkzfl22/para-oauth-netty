package com.paraview.oauth.web;

import com.paraview.oauth.bean.OAuthReq;
import com.paraview.oauth.bean.Token;
import com.paraview.oauth.service.OAuthService;

import java.net.URI;


public class OAuthContorller {

    private static OAuthContorller oAuthContorller = new OAuthContorller();

    private OAuthService oAuthService = new OAuthService();

    public static OAuthContorller getInstance(){
        return oAuthContorller;
    }

    public Token token(OAuthReq req) {
        return oAuthService.doAuth(req);
    }

    public void authorize(OAuthReq req) {
        String code = oAuthService.makeCode(req);
        URI.create(req.getRedirect_uri() + "&code=" + code + "&state=" + req.getState());
    }

}
