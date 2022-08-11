package com.kakao.socialLogin.practice.kakaoSocialLogin.controller;

import com.kakao.socialLogin.practice.kakaoSocialLogin.API.KakaoAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@Slf4j
public class HomeController {

    KakaoAPI kakaoAPI = new KakaoAPI();

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("code") String code,
                              HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String accessToken = kakaoAPI.getAccessToken(code);
        HashMap<String, Object> userInfo = kakaoAPI.getUserInfo(accessToken);

        log.debug("login info : {}", userInfo.toString());
        if(userInfo.get("email") != null){
            httpSession.setAttribute("userId", userInfo.get("email"));
            httpSession.setAttribute("accessToken", accessToken);
        }
        modelAndView.addObject("userId", userInfo.get("email"));
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value="/logout")
    public ModelAndView logout(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        kakaoAPI.kakaoLogout((String)session.getAttribute("accessToken"));
        session.removeAttribute("accessToken");
        session.removeAttribute("userId");
        modelAndView.setViewName("index");
        return modelAndView;
    }

}
