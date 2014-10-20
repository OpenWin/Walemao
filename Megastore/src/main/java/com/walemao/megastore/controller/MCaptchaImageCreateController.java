package com.walemao.megastore.controller;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.Producer;

@Controller
public class MCaptchaImageCreateController {

	private Producer captchaProducer = null;
	
	@Autowired  
    public void setCaptchaProducer(Producer captchaProducer){  
        this.captchaProducer = captchaProducer;  
    }
	
	@RequestMapping(value = "captcha", method = RequestMethod.GET)
	public String getCheckPage()
	{
		System.out.println("get checkPage");
		return "captcha";
	}
	
	@RequestMapping(value = "/kaptcha.jpg")  
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{  
        // Set to expire far in the past.  
        response.setDateHeader("Expires", 0);  
        // Set standard HTTP/1.1 no-cache headers.  
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");  
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
        // Set standard HTTP/1.0 no-cache header.  
        response.setHeader("Pragma", "no-cache");  
  
        // return a jpeg  
        response.setContentType("image/jpeg");  
  
        // create the text for the image  
        String capText = captchaProducer.createText();  
  
        // store the text in the session  
        request.getSession().setAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY, capText);  
  
        // create the image with the text  
        BufferedImage bi = captchaProducer.createImage(capText);  
  
        ServletOutputStream out = response.getOutputStream();  
  
        // write the data out  
        ImageIO.write(bi, "jpg", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
        return null;  
    }  
	
	
	/** 
     * loginCheck:ajax异步校验登录请求. <br/> 
     * 
     * @author chenzhou1025@126.com 
     * @param request 
     * @param username 用户名 
     * @param password 密码 
     * @param kaptchaReceived 验证码 
     * @return 校验结果 
     * @since 2013-12-10 
     */  
    @RequestMapping(value = "check", method = RequestMethod.POST)  
    @ResponseBody  
    public String loginCheck(HttpServletRequest request,  
            /*@RequestParam(value = "username", required = true) String username,  
            @RequestParam(value = "password", required = true) String password, */ 
            @RequestParam(value = "kaptcha", required = true) String kaptchaReceived)
    {  
        //用户输入的验证码的值  
        String kaptchaExpected = (String) request.getSession().getAttribute(  
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);  
        //校验验证码是否正确 
        if (kaptchaReceived == null || !kaptchaReceived.equals(kaptchaExpected)) 
        {  
            return "kaptcha_error";//返回验证码错误  
        }
        
        //校验用户名密码  
        // ……  
        // ……  
        return "success"; //校验通过返回成功  
    }  
}
