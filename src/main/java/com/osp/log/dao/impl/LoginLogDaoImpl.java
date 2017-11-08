package com.osp.log.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.osp.log.dao.LoginLogDao;
import com.osp.log.model.DateInterval;
import com.osp.log.model.LoginLog;
import com.osp.log.model.Page;
import com.osp.log.util.RegexUtil;

/**
 * 登录日志 2017-11-07
 * 
 * @author zhangmingcheng
 */
@Repository
public class LoginLogDaoImpl implements LoginLogDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<LoginLog> selectLoginLogList(Page page, String systemId, String username, String startDate,
			String endDate) {
		DateInterval dateInterval = RegexUtil.getDateInterval(new DateInterval(startDate, endDate), "yyyy-MM-dd");
		dateInterval.setStartDate(dateInterval.getStartDate() + " 00:00:00");
		dateInterval.setEndDate(dateInterval.getEndDate() + " 23:59:59");
		String sql = "select ll.F_ZGBH,bu.F_NAME,ll.F_XTBH,ll.F_STIME,ll.F_ETIME,ll.F_IP from SYS_LOGINLOG ll,BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
		String loginlog_count = "select count(ll.LOG_ID) from SYS_LOGINLOG ll,BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ?";
		Object[] selectParams = new Object[] { dateInterval.getStartDate(), dateInterval.getEndDate(), page.getStart(),
				page.getLength() };
		Object[] countParams = new Object[] { dateInterval.getStartDate(), dateInterval.getEndDate() };

		if (systemId.isEmpty() == false && username.isEmpty() == false) {
			sql = "select ll.F_ZGBH,bu.F_NAME,ll.F_XTBH,ll.F_STIME,ll.F_ETIME,ll.F_IP from SYS_LOGINLOG ll,BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and bu.F_NAME = ? and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
			loginlog_count = "select count(ll.LOG_ID) from SYS_LOGINLOG ll,BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and bu.F_NAME = ? and ll.F_STIME BETWEEN ? AND ?";
			selectParams = new Object[] { systemId, username, dateInterval.getStartDate(), dateInterval.getEndDate(),
					page.getStart(), page.getLength() };
			countParams = new Object[] { systemId, username, dateInterval.getStartDate(), dateInterval.getEndDate() };
		} else if (systemId.isEmpty() == false) {
			sql = "select ll.F_ZGBH,bu.F_NAME,ll.F_XTBH,ll.F_STIME,ll.F_ETIME,ll.F_IP from SYS_LOGINLOG ll,BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
			loginlog_count = "select count(ll.LOG_ID) from SYS_LOGINLOG ll,BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ?";
			selectParams = new Object[] { systemId, dateInterval.getStartDate(), dateInterval.getEndDate(),
					page.getStart(), page.getLength() };
			countParams = new Object[] { systemId, dateInterval.getStartDate(), dateInterval.getEndDate() };
		} else if (username.isEmpty() == false) {
			sql = "select ll.F_ZGBH,bu.F_NAME,ll.F_XTBH,ll.F_STIME,ll.F_ETIME,ll.F_IP from SYS_LOGINLOG ll,BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and bu.F_NAME = ? and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
			loginlog_count = "select count(ll.LOG_ID) from SYS_LOGINLOG ll,BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and bu.F_NAME = ? and ll.F_STIME BETWEEN ? AND ?";
			selectParams = new Object[] { username, dateInterval.getStartDate(), dateInterval.getEndDate(),
					page.getStart(), page.getLength() };
			countParams = new Object[] { username, dateInterval.getStartDate(), dateInterval.getEndDate() };
		}
		List<LoginLog> list = jdbcTemplate.query(sql, selectParams, new BeanPropertyRowMapper(LoginLog.class));
		Long counts = jdbcTemplate.queryForObject(loginlog_count, countParams, Long.class);
		page.setRecordsTotal(counts);
		page.setRecordsFiltered(counts);
		return list;
	}

}
