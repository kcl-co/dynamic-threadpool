package com.share.co.kcl.dtp.core.reporter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.share.co.kcl.dtp.common.constants.ResultCode;
import com.share.co.kcl.dtp.common.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DefaultServerHealthReporter extends AbstractServerHealthReporter {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultServerHealthReporter.class);

    private final String reportLink;

    public DefaultServerHealthReporter(String serverCode, String serverSecret, String reportLink) {
        super(serverCode, serverSecret);
        this.reportLink = reportLink;
    }

    @Override
    protected boolean sendReport(String serverCode, String serverSecret, String serverIp) {
        Map<String, String> headers = new HashMap<>();
        headers.put("secret", serverSecret);

        Map<String, String> params = new HashMap<>();
        params.put("serverCode", serverCode);
        params.put("serverIp", serverIp);

        String result = HttpUtils.doGet(reportLink, headers, params);
        JSONObject response = JSON.parseObject(result);
        Integer responseCode = response.getInteger("code");
        String responseMsg = response.getString("msg");
        if (!ResultCode.SUCCESS.getCode().equals(responseCode)) {
            LOG.error("executor reporter request remote server failure, error msg: {}", responseMsg);
            return false;
        }
        return true;
    }

    @Override
    protected long reportDelay() {
        return 0;
    }

    @Override
    protected long reportPeriod() {
        return 3000L;
    }
}
