/*
 * Symphony - A modern community (forum/BBS/SNS/blog) platform written in Java.
 * Copyright (C) 2012-2018, b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.symphony.service;

import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.model.Report;
import org.b3log.symphony.repository.ReportRepository;
import org.json.JSONObject;

/**
 * Report management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.0, Jun 26, 2018
 * @since 3.1.0
 */
@Service
public class ReportMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ReportMgmtService.class);

    /**
     * Report repository.
     */
    @Inject
    private ReportRepository reportRepository;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Makes the specified report as handled.
     *
     * @param reportId the specified report id
     */
    @Transactional
    public void makeReportHandled(final String reportId) {
        try {
            final JSONObject report = reportRepository.get(reportId);
            report.put(Report.REPORT_HANDLED, Report.REPORT_HANDLED_C_YES);

            reportRepository.update(reportId, report);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Makes report [id=" + reportId + "] as handled failed", e);
        }
    }

    /**
     * Adds a report.
     *
     * @param report the specified report, for example,
     *               {
     *               "reportUserId": "",
     *               "reportDataId": "",
     *               "reportDataType": int,
     *               "reportType": int,
     *               "reportMemo": ""
     *               }
     * @throws ServiceException service exception
     */
    @Transactional
    public void addReport(final JSONObject report) throws ServiceException {
        report.put(Report.REPORT_HANDLED, Report.REPORT_HANDLED_C_NOT);

        try {
            reportRepository.add(report);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Adds a report failed", e);

            throw new ServiceException(langPropsService.get("systemErrLabel"));
        }
    }
}
