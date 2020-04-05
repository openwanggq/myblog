package org.sang.task;

import org.assertj.core.internal.cglib.asm.$ClassWriter;
import org.sang.bean.Article;
import org.sang.bean.ArticleReptile;
import org.sang.mapper.ArticleReptileMapper;
import org.sang.reptile.JianShuNewsSpider;
import org.sang.reptile.MysqlPipeline;
import org.sang.service.ArticleReptileService;
import org.sang.service.ArticleService;
import org.sang.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

import java.sql.Timestamp;
import java.util.Date;
/***
 * 基于spring boot+WebMagic+MyBatis的爬虫框架
 * https://www.jianshu.com/p/cfead4b3e34e
 */

/**
 * //    0 0 3 * * ?     每天3点执行
 * //    0 5 3 * * ?     每天3点5分执行
 * //    0 5 3 ? * *     每天3点5分执行，与上面作用相同
 * //    0 5/10 3 * * ?  每天3点的 5分，15分，25分，35分，45分，55分这几个时间点执行
 * //    0 10 3 ? * 1    每周星期天，3点10分 执行，注：1表示星期天
 * //    0 10 3 ? * 1#3  每个月的第三个星期，星期天 执行，#号只能出现在星期的位置
 * <p>
 * //    * 第一位， 表示秒，取值0-59
 * //            * 第二位，表示分   ，取值0-59
 * //            * 第三位，表示小时 ，取值0-23
 * //            * 第四位，日期天/日，取值1-31
 * //            * 第五位，日期月份 ，取值1-12
 * //            * 第六位，星期，取值1-7，星期一，星期二...，注：不是第1周，第二周的意思
 * //    另外：1表示星期天，2表示星期一。
 * //            * 第7为，年份，可以留空，取值1970-2099
 * <p>
 * 天气预报： http://blog.csdn.net/gebitan505/article/details/77897344
 */
@Component
public class Jobs {

    @Autowired
    private JianShuNewsSpider jianShuNewsSpider;
    @Autowired
    private MysqlPipeline mysqlPipeline;

    public final static long ONE_Minute = 60 * 1000;
    public final static long ONE_HOUR = 60 * 60 * 1000;
    public final static long TWO_HOUR = 2 * 60 * 60 * 1000;


    //    @Scheduled(initialDelay = 2000, fixedDelay = 10 * 60000)
    @Scheduled(initialDelay = 2000, fixedDelay = TWO_HOUR)
    @Transactional
    public void fixedDelayJob() {
        String curDate = Util.format_yyyyMMddHHmmss(new Date());
        System.out.println(curDate + " >>fixedDelay执行....");
        Spider.create(jianShuNewsSpider).addUrl("https://www.jianshu.com/").addPipeline(mysqlPipeline).runAsync();
    }

    @Scheduled(fixedRate = ONE_HOUR)
    public void fixedRateJob() {
//        System.out.println(Util.format_yyyyMMddHHmmss(new Date()) + " >>fixedRate执行....");
//        try {
//            emailServiceUtil.sendSimpleMail("932884749@qq.com", "祝福--from 王高强", "今天三七女生节。世界因你的诞生，显得分外美丽；人间因你的存在，显得异常奇妙。一个小小的问候，一份浓浓的真意，愿你越来越美丽。接下来小编带大家了解一下2020最新朋友间女生节的祝福语，仅供大家阅读借鉴！");
//        } catch (Exception e) {
//            System.out.println(e.toString() + " >>sendSimpleMail....");
//        }
    }

    //     每周星期天，9点0分 执行，注：1表示星期天
    @Scheduled(cron = "0 30 8 ? * 2,5 ")
    public void crashSend() {
//        String curDate = Util.format_yyyyMMddHHmmss(new Date());
//        String title = "崩溃日志";
//        String content = "推送时间: " + curDate
//                + " \n\n崩溃个数： " + logRepository.count()
//                + " \n\n日志链接： http://51yxt.cn:9999/#/MainLog";
//        for (String sendTo : crashMailArray) {
//            emailServiceUtil.sendSimpleMail(sendTo, title, content);
//        }
//        System.out.println(curDate + " >>cron执行....");
    }

    //            @Scheduled(fixedDelay = 2 * 60 * 1000)
//    @Scheduled(cron = "0 0 8,21 * * ?")
    @Scheduled(cron = "0 0 7 * * ?")
    public void weatherSend() {
        new Thread() {
            @Override
            public void run() {
                super.run();
//                sendWeatherByCity("苏州", weather_suzhou_MailArray, weather_suzhou_phones);
//                sendWeatherByCity("信阳", weather_xinyang_MailArray, weather_xinyang_phones);
            }
        }.start();
    }


}
