package org.clever.quartz.utils;

import org.clever.quartz.dto.response.JobDetailsRes;
import org.clever.quartz.entity.QrtzJobDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-10 11:46 <br/>
 */
public class ConvertUtils {

    private static boolean convert(String str) {
        if (str != null && str.length() > 0) {
            int c = Character.toLowerCase(str.charAt(0));
            return (c == 't' || c == 'y' || c == '1' || str.equals("-1"));
        }
        return false;
    }

    public static JobDetailsRes convert(QrtzJobDetails qrtzJobDetails) {
        JobDetailsRes jobDetailsRes = new JobDetailsRes();
        jobDetailsRes.setSchedName(qrtzJobDetails.getSchedName());
        jobDetailsRes.setJobName(qrtzJobDetails.getJobName());
        jobDetailsRes.setJobGroup(qrtzJobDetails.getJobGroup());
        jobDetailsRes.setDescription(qrtzJobDetails.getDescription());
        jobDetailsRes.setJobClassName(qrtzJobDetails.getJobClassName());
        jobDetailsRes.setDurable(convert(qrtzJobDetails.getIsDurable()));
        jobDetailsRes.setNonconcurrent(convert(qrtzJobDetails.getIsNonconcurrent()));
        jobDetailsRes.setUpdateData(convert(qrtzJobDetails.getIsUpdateData()));
        jobDetailsRes.setRequestsRecovery(convert(qrtzJobDetails.getRequestsRecovery()));
        return jobDetailsRes;
    }

    public static List<JobDetailsRes> convert(List<QrtzJobDetails> qrtzJobDetailsList) {
        if (qrtzJobDetailsList == null) {
            return null;
        }
        List<JobDetailsRes> jobDetailsResList = new ArrayList<>();
        for (QrtzJobDetails qrtzJobDetails : qrtzJobDetailsList) {
            jobDetailsResList.add(convert(qrtzJobDetails));
        }
        return jobDetailsResList;
    }
}
