/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.paraview.oauth.netty;

import com.paraview.oauth.bean.OAuthReq;
import com.paraview.oauth.bean.Token;
import com.paraview.oauth.utils.StringUtil;
import com.paraview.oauth.web.OAuthContorller;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.CookieEncoder;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;

import java.util.Map;

/**
 * @author liujun
 * @since 2021/6/5
 */
public class HttpServerHandler extends SimpleChannelInboundHandler {

    private static final String OAUTH_TOKEN_URL = "/oauth/token";

    private static final String OAUTH_AUTHORIZE_URL = "/oauth/authorize";

    private static ThreadLocal<DefaultHttpRequest> requestThreadLocal = new ThreadLocal<>();


    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (msg instanceof HttpRequest) {
            DefaultHttpRequest request = (DefaultHttpRequest) msg;
            requestThreadLocal.set(request);
        } else if (msg instanceof DefaultLastHttpContent) {
            DefaultLastHttpContent httpContent = (DefaultLastHttpContent) msg;
            doHandle(ctx, httpContent);
        }
    }

    private void doHandle(ChannelHandlerContext ctx, DefaultLastHttpContent httpContent) {
        String authorization = requestThreadLocal.get().headers().get("Authorization");
        String url = requestThreadLocal.get().uri();
        requestThreadLocal.remove();
        if (OAUTH_TOKEN_URL.equals(url)) {
            Map<String,String> param = StringUtil.parseBody(httpContent.content().toString(CharsetUtil.UTF_8));
            OAuthReq req = new OAuthReq();
            req.setAuthorization(authorization);
            req.setGrant_type(param.get("grant_type"));
            req.setPassword(param.get("password"));
            req.setUsername(param.get("username"));
            Token token = OAuthContorller.getInstance().token(req);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(token.toString()
                    .getBytes()));
            Cookie cookie1 = new DefaultCookie("oauth-token", token.getAccess_token());
            ClientCookieEncoder clientCookieEncoder = ClientCookieEncoder.STRICT;
            response.headers().set(HttpHeaders.Names.SET_COOKIE, clientCookieEncoder.encode(cookie1));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                    response.content().readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.write(response);
            ctx.flush();
        } else if (OAUTH_AUTHORIZE_URL.equals(url)) {

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }

}
