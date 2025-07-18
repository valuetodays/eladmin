package me.zhengjie.modules.security.config;

import cn.vt.exception.CommonException;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.ChineseGifCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.awt.Font;

@ApplicationScoped
public class CaptchaFactory {

    @Inject
    LoginProperties loginProperties;

    public Captcha getCaptcha() {
        CaptchaConfigProperty config = loginProperties.captcha();
        Captcha captcha;
        int width = config.width();
        int height = config.height();
        int length = config.length();

        switch (config.codeType()) {
            case ARITHMETIC:
                captcha = new FixedArithmeticCaptcha(width, height);
                captcha.setLen(length);
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(width, height);
                captcha.setLen(length);
                break;
            case CHINESE_GIF:
                captcha = new ChineseGifCaptcha(width, height);
                captcha.setLen(length);
                break;
            case GIF:
                captcha = new GifCaptcha(width, height);
                captcha.setLen(length);
                break;
            case SPEC:
                captcha = new SpecCaptcha(width, height);
                captcha.setLen(length);
                break;
            default:
                throw new CommonException("验证码配置信息错误！正确配置查看 LoginCodeEnum");
        }

        if (config.fontName().isPresent()) {
            captcha.setFont(new Font(config.fontName().get(), Font.PLAIN, config.fontSize()));
        }

        return captcha;
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            int n1Temp = num(1, 10), n2Temp = num(1, 10);
            int n1 = Math.max(n1Temp, n2Temp);
            int n2 = Math.min(n1Temp, n2Temp);
            int opt = num(3);
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            char optChar = "+-x".charAt(opt);
            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);
            return chars.toCharArray();
        }
    }
}
