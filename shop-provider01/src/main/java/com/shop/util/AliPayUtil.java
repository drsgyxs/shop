package com.shop.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AliPayUtil {
    @Value("${aliPay.appId}")
    public String APP_ID;
    @Value("${aliPay.appPrivateKey}")
    public String APP_PRIVATE_KEY;
    @Value("${aliPay.aliPayPublicKey}")
    public String ALI_PAY_PUBLIC_KEY;
    @Value("${aliPay.gateway}")
    public String GATEWAY;
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    @Value("${aliPay.returnURL}")
    private String RETURN_URL;
    @Value("${aliPay.notifyURL}")
    private String NOTIFY_URL;
    public static final String CHARSET = "UTF-8";
    public static final String FORMAT = "json";
    //签名方式
    public static final String SIGN_TYPE = "RSA2";

    public String pagePay(String uid, String amount, String subject) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALI_PAY_PUBLIC_KEY, SIGN_TYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setSubject(subject);
        model.setOutTradeNo(uid);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(amount);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        request.setBizModel(model);
        request.setReturnUrl(RETURN_URL);
        request.setNotifyUrl(NOTIFY_URL);
        String body = null;
        try {
            body = alipayClient.sdkExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return body;
    }
}
