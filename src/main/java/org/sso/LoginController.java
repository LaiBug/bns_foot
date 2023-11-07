package org.sso;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@RestController
public class LoginController {
    Log log=   LogFactory.getLog(LoginController.class);
    //    private static final String AUTH_SERVER_URL ="https://test.zhongshu.tech/pbc/usercenter/auth";//""<认证服务器URL>";
    @Value("${signleLogin.serverUrl}")
    private String AUTH_SERVER_URL;//<认证服务器URL>";

    @Value("${signleLogin.isHlw}")
    private Boolean IS_HLW;//是否HLW环境;
    @Value("${signleLogin.clientId}")
    private String CLIENT_ID;//"<客户端ID>";
    @Value("${signleLogin.callBackUrl}")
    private String CALLBACK_URL;//"<回调URL>";
    @Value("${signleLogin.clientSecret}")
    private String CLIENT_SECRET;//"<客户端密钥>";
    @Autowired
    private HttpServletResponse response;
    @Value("${token.ixmLoginExpireTime}")
    private int expireTime;


    @GetMapping(value ="/singleLogin")
    public void login(HttpServletResponse res) throws IOException {
        // 生成单点登录链接
        String ssoUrl = AUTH_SERVER_URL +"/auth/oauth/authorize"+
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + CALLBACK_URL+
                "&response_type=code&scope=basic name&state=UjGtVS";
        // 将用户重定向到单点登录链接

        log.info(ssoUrl);

        res.sendRedirect(ssoUrl);
    }

    @GetMapping("/callback")
    public void handleCallback(String code) throws IOException {
        // 使用授权码调用认证服务器的 API，获取访问令牌
        UserInfoResponse userInfoResponse=null;
        if(IS_HLW){//互联网环境
            RestTemplate restTemplate = new RestTemplate();

            String tokenUrl = AUTH_SERVER_URL +"/oauth/token"+
                    "?grant_type=authorization_code" +
                    "&scope=basic name"  +
                    "&client_id=" + CLIENT_ID +
                    "&client_secret=" + CLIENT_SECRET+
                    "&code=" + code +
                    "&redirect_uri=" + CALLBACK_URL
                    ;
            String accessToken ="";
            try {
                TokenResponse tokenResponse = restTemplate.postForObject(tokenUrl, null, TokenResponse.class);
                accessToken = tokenResponse.getAccess_token();
            }catch (HttpClientErrorException.Unauthorized e){
                HttpStatus statusCode=e.getStatusCode();
                log.error("单点登录401報錯："+e.getMessage());
            }catch (HttpClientErrorException e){
                String responseBody =e.getResponseBodyAsString();
                log.error("请求报错："+responseBody);
            }

            // 使用访问令牌调用认证服务器的 API，获取用户信息
            String userInfoUrl = AUTH_SERVER_URL + "/server/public/user/get";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            try {
                ResponseEntity<UserInfoResponse> responseEntity = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, UserInfoResponse.class);
                userInfoResponse = responseEntity.getBody();
            }catch (HttpClientErrorException.Unauthorized e){
                HttpStatus statusCode=e.getStatusCode();
                log.error("获取服务端token401報錯："+e.getMessage());
            }catch (HttpClientErrorException e){
                String responseBody =e.getResponseBodyAsString();
                log.error("获取服务端token请求报错："+responseBody);
            }
        }else {//测试环境
            userInfoResponse =new UserInfoResponse();
            if(userInfoResponse!=null){
                userInfoResponse.setOpenId("666");
            }
        }
        log.info("处理用户信息，并进行相应的操作,openId:"+userInfoResponse.getOpenId());
        // 处理用户信息，并进行相应的操作
        // ...
        log.info(":"+CALLBACK_URL);
//        response.sendRedirect(CALLBACK_URL+"?token="+"token3333333333");
    }
}



