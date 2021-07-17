package com.cgf.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description //TODO email验证码工具类
 * @Author cgf
 * @Date 2021/1/17 16:41
 */
@Slf4j
@Component
public class EmailCodeUtils {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    private static String EMAIL_KEY = "email:";

    /**
     * 发送人
     */
    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 验证码
     */
    private String code;

    /**
     * 过期时间,默认五分钟
     */
    private Long timeout = 5*60*1000L;

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * Math.random生成的是一般随机数，采用的是类似于统计学的随机数生成规则，其输出结果很容易预测，因此可能导致被攻击者击中。
     * 而SecureRandom是真随机数，采用的是类似于密码学的随机数生成规则，其输出结果较难预测，若想要预防被攻击者攻击，最好做到使攻击者根本无法，或不可能鉴别生成的随机值和真正的随机值。
     */
    private static final Random RANDOM = new SecureRandom();

    public String generateVerCode() {
        char[] nonceChars = new char[6];
        for (int i = 0; i < nonceChars.length; i++) {
            nonceChars[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * @Author cgf
     * @Description //TODO 发送验证码，过期时间为60s
     * @Date 21:12 2021/5/10
     * @Param [receiver] 发送人
     */
    @Async
    public  void sendEmailCode(String receiver){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("验证码");//设置邮件标题
        code = generateVerCode();
        //存储到redis
        redisTemplate.opsForValue().set(EMAIL_KEY+receiver, code, timeout, TimeUnit.MILLISECONDS);
        log.warn("验证码为"+code);
        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + code + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");    //设置邮件正文
        message.setFrom(sender);//发件人
        message.setTo(receiver);//收件人
        mailSender.send(message);//发送邮件
    }

    public boolean isExpired(String receiver) {
        return Objects.isNull(redisTemplate.opsForValue().get(EMAIL_KEY+receiver));
    }

    public String getCode(String receiver) {
        return (String) redisTemplate.opsForValue().get(EMAIL_KEY+receiver);
    }
}