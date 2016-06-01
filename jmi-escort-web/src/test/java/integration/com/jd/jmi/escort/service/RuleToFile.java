package integration.com.jd.jmi.escort.service;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.escort.common.constant.JmiOrderTypeEnum;
import com.jd.jmi.escort.common.model.risk.RiskRuleVO;
import com.jd.jmi.escort.common.service.JmiRiskRuleJsfService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/2/24
 * Version:1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath*:spring-config.xml"})
public class RuleToFile {
    private final Logger logger = LoggerFactory.getLogger(RuleToFile.class);
    @Resource
    private JmiRiskRuleJsfService riskRuleJsfService;

    @Test
    public void rule2File() throws Exception {
        try {
            for (JmiOrderTypeEnum orderType : JmiOrderTypeEnum.values()) {
                String filePath = System.getProperty("user.home") + "/escort_rule_data_" + orderType.getOrderType() + ".txt";
                logger.error("写文件的Path：{}", filePath);
                File file = new File(filePath);
                FileWriter fw = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(fw);
                List<RiskRuleVO> list = riskRuleJsfService.getRisksByOrderType(orderType.getOrderType(), null);
                for (RiskRuleVO ruleVO : list) {
                    writer.write(JSON.toJSONString(ruleVO));
                    writer.newLine();
                }
                writer.flush();
                writer.close();
                fw.close();
            }
        } catch (IOException e) {
            logger.error("读写文件异常：", e);
        }

        logger.error("同步数据完成！");
    }
}
