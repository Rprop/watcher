package com.jd.watcher.receiver;

import com.jd.watcher.common.Initialization;
import com.jd.watcher.exception.NoRetryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("messageAnalyzerManager")
public class DefaultMessageAnalyzerManager implements MessageAnalyzerManager, Initialization {
    private static Logger logger = LoggerFactory.getLogger(DefaultMessageAnalyzerManager.class);
    private static final long MINUTE = 60 * 1000L;
    private long m_duration = 60 * MINUTE;
    private long m_extraTime = 3 * MINUTE;
    private List<String> m_analyzerNames;
    private Map<Long, Map<String, List<MessageAnalyzer>>> m_analyzers = new HashMap<Long, Map<String, List<MessageAnalyzer>>>();

    @Override
    public List<MessageAnalyzer> getAnalyzer(String name, long startTime) {
        try {
            Map<String, List<MessageAnalyzer>> temp = m_analyzers.remove(startTime - m_duration * 2);
            if (temp != null) {
                for (List<MessageAnalyzer> anlyzers : temp.values()) {
                    for (MessageAnalyzer analyzer : anlyzers) {
                        analyzer.destroy();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取分析器异常：", e);
        }

        Map<String, List<MessageAnalyzer>> map = m_analyzers.get(startTime);
        if (map == null) {
            synchronized (m_analyzers) {
                map = m_analyzers.get(startTime);

                if (map == null) {
                    map = new HashMap<String, List<MessageAnalyzer>>();
                    m_analyzers.put(startTime, map);
                }
            }
        }

        List<MessageAnalyzer> analyzers = map.get(name);

        if (analyzers == null) {
            synchronized (map) {
                analyzers = map.get(name);

                if (analyzers == null) {
                    analyzers = new ArrayList<MessageAnalyzer>();

                    MessageAnalyzer analyzer = lookup(MessageAnalyzer.class, name);

                    analyzer.setIndex(0);
                    analyzer.initialize(startTime, m_duration, m_extraTime);
                    analyzers.add(analyzer);

                    int count = analyzer.getAnanlyzerCount();

                    for (int i = 1; i < count; i++) {
                        MessageAnalyzer tempAnalyzer = lookup(MessageAnalyzer.class, name);

                        tempAnalyzer.setIndex(i);
                        tempAnalyzer.initialize(startTime, m_duration, m_extraTime);
                        analyzers.add(tempAnalyzer);
                    }
                    map.put(name, analyzers);
                }
            }
        }

        return analyzers;
    }

//    @Override
//    public List<MessageAnalyzer> getLastAnalyzer(String name) {
//        return null;
//    }
//
//    @Override
//    public List<MessageAnalyzer> getCurrentAnalyzer(String name) {
//        return null;
//    }

    @Override
    public List<String> getAnalyzerNames() {
        return m_analyzerNames;
    }

    @Override
    @PostConstruct
    public void initialize() throws NoRetryException {
        Map<String, MessageAnalyzer> map = lookupMap(MessageAnalyzer.class);

        for (MessageAnalyzer analyzer : map.values()) {
            analyzer.destroy();
        }

        m_analyzerNames = new ArrayList<String>(map.keySet());

        Collections.sort(m_analyzerNames, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                String state = "state";
                String top = "top";

                if (state.equals(str1)) {
                    return 1;
                } else if (state.equals(str2)) {
                    return -1;
                }
                if (top.equals(str1)) {
                    return -1;
                } else if (top.equals(str2)) {
                    return 1;
                }
                return str1.compareTo(str2);
            }
        });
    }

    /**
     * 获取某个实现
     *
     * @param classes
     * @param name
     * @param <T>
     * @return
     */
    private <T> T lookup(Class<T> classes, String name) {
        Map<String, T> map = lookupMap(classes);
        return map.get(name);
    }

    private <T> Map<String, T> lookupMap(Class<T> role) {
        HashMap result = new HashMap<String, T>();
        result.put("tp",new TopAnalyzer());
        //todo 配置各个实现
        return result;

    }
}
