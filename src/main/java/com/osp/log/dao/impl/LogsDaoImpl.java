package com.osp.log.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.osp.log.dao.LogsDao;
import com.osp.log.model.DateInterval;
import com.osp.log.model.LoginLogModel;
import com.osp.log.model.OperationLogModel;
import com.osp.log.model.Page;

/**
 * 登录日志 2017-11-07
 * 
 * @author zhangmingcheng
 */
@Repository
public class LogsDaoImpl implements LogsDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<LoginLogModel> selectLoginLogList(Page page, String systemId, String username,
			DateInterval dateInterval) {
		String sql = "select ll.F_ZGBH,bu.F_NAME,ll.F_XTBH,ll.F_STIME,ll.F_ETIME,ll.F_IP,(select count(1) from SYS_LOGINLOG log where log.F_ZGBH = ll.F_ZGBH and log.F_STIME BETWEEN ? AND ?) F_NUM from SYS_LOGINLOG ll,BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
		String loginlog_count = "select count(ll.LOG_ID) from SYS_LOGINLOG ll,BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ?";
		Object[] selectParams = new Object[] { dateInterval.getStartDate(), dateInterval.getEndDate(), dateInterval.getStartDate(), dateInterval.getEndDate(), 
				page.getStart(),page.getLength() };
		Object[] countParams = new Object[] { dateInterval.getStartDate(), dateInterval.getEndDate() };

		if (systemId.isEmpty() == false && username.isEmpty() == false) {
			sql = "select ll.F_ZGBH,bu.F_NAME,ll.F_XTBH,ll.F_STIME,ll.F_ETIME,ll.F_IP,(select count(1) from SYS_LOGINLOG log where log.F_ZGBH = ll.F_ZGBH and log.F_STIME BETWEEN ? AND ?) F_NUM from SYS_LOGINLOG ll,BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and ll.F_ZGBH = ? and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
			loginlog_count = "select count(ll.LOG_ID) from SYS_LOGINLOG ll,BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and ll.F_ZGBH = ? and ll.F_STIME BETWEEN ? AND ?";
			selectParams = new Object[] { dateInterval.getStartDate(), dateInterval.getEndDate(), systemId, username,dateInterval.getStartDate(), dateInterval.getEndDate(), 
					page.getStart(), page.getLength() };
			countParams = new Object[] { systemId, username, dateInterval.getStartDate(), dateInterval.getEndDate() };
		} else if (systemId.isEmpty() == false) {
			sql = "select ll.F_ZGBH,bu.F_NAME,ll.F_XTBH,ll.F_STIME,ll.F_ETIME,ll.F_IP,(select count(1) from SYS_LOGINLOG log where log.F_ZGBH = ll.F_ZGBH and log.F_STIME BETWEEN ? AND ?) F_NUM from SYS_LOGINLOG ll,BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
			loginlog_count = "select count(ll.LOG_ID) from SYS_LOGINLOG ll,BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ?";
			selectParams = new Object[] { dateInterval.getStartDate(), dateInterval.getEndDate(), systemId,dateInterval.getStartDate(), dateInterval.getEndDate(), 
					page.getStart(), page.getLength() };
			countParams = new Object[] { systemId, dateInterval.getStartDate(), dateInterval.getEndDate() };
		} else if (username.isEmpty() == false) {
			sql = "select ll.F_ZGBH,bu.F_NAME,ll.F_XTBH,ll.F_STIME,ll.F_ETIME,ll.F_IP,(select count(1) from SYS_LOGINLOG log where log.F_ZGBH = ll.F_ZGBH and log.F_STIME BETWEEN ? AND ?) F_NUM from SYS_LOGINLOG ll,BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and ll.F_ZGBH = ? and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
			loginlog_count = "select count(ll.LOG_ID) from SYS_LOGINLOG ll,BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and ll.F_ZGBH = ? and ll.F_STIME BETWEEN ? AND ?";
			selectParams = new Object[] { dateInterval.getStartDate(), dateInterval.getEndDate(), username,dateInterval.getStartDate(), dateInterval.getEndDate(), 
					page.getStart(), page.getLength() };
			countParams = new Object[] { username, dateInterval.getStartDate(), dateInterval.getEndDate() };
		}
		List<LoginLogModel> list = jdbcTemplate.query(sql, selectParams,
				new BeanPropertyRowMapper(LoginLogModel.class));
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setOrderNumber(i + 1);
		}
		Long counts = jdbcTemplate.queryForObject(loginlog_count, countParams, Long.class);
		page.setRecordsTotal(counts);
		page.setRecordsFiltered(counts);
		return list;
	}

	/**
	 * 获取当前页操作日志
	 */
	@Override
	public List<OperationLogModel> selectOperationList(Page page, String systemId, DateInterval dateInterval) {
		String sql = "select ll.F_STIME,bu.F_NAME,ll.F_GNBH,ll.F_XTBH,ll.F_ZGBH,ll.F_IP from SYS_OPLOG ll, BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
		String log_count = "select count(ll.LOG_ID) from SYS_OPLOG ll,BSUSER bu where ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ?";
		Object[] selectParams = new Object[] { dateInterval.getStartDate(), dateInterval.getEndDate(), page.getStart(),
				page.getLength() };
		Object[] countParams = new Object[] { dateInterval.getStartDate(), dateInterval.getEndDate() };
		if (systemId.isEmpty() == false) {
			sql = "select ll.F_STIME,bu.F_NAME,ll.F_GNBH,ll.F_XTBH,ll.F_ZGBH,ll.F_IP from SYS_OPLOG ll, BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ? ORDER BY ll.F_STIME DESC limit ?,?";
			log_count = "select count(ll.LOG_ID) from SYS_OPLOG ll,BSUSER bu where ll.F_XTBH = ? and ll.F_ZGBH = bu.F_ZGBH and ll.F_STIME BETWEEN ? AND ?";
			selectParams = new Object[] { systemId, dateInterval.getStartDate(), dateInterval.getEndDate(),
					page.getStart(), page.getLength() };
			countParams = new Object[] { systemId, dateInterval.getStartDate(), dateInterval.getEndDate() };
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<OperationLogModel> list = jdbcTemplate.query(sql, selectParams,
				new BeanPropertyRowMapper(OperationLogModel.class));
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setOrderNumber(i + 1);
		}
		Long counts = jdbcTemplate.queryForObject(log_count, countParams, Long.class);
		page.setRecordsTotal(counts);
		page.setRecordsFiltered(counts);
		return list;
	}

}
