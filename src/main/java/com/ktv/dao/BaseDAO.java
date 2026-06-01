package com.ktv.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.PostConstruct;
import java.util.List;

public abstract class BaseDAO {

    protected static final Logger logger = LoggerFactory.getLogger(BaseDAO.class);

    @Autowired
    protected JdbcTemplate jdbcTemplate;   // Spring 已经配好连接池

    /* 通用更新 */
    public int executeUpdate(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    /* 查询单个对象 */
    protected <T> T queryForObject(String sql, RowMapper<T> rm, Object... args) {
        List<T> list = jdbcTemplate.query(sql, rm, args);
        return list.isEmpty() ? null : list.get(0);
    }

    /* 查询列表 */
    protected <T> List<T> queryForList(String sql, RowMapper<T> rm, Object... args) {
        return jdbcTemplate.query(sql, rm, args);
    }

    /* 统计 */
    protected int queryCount(String sql, Object... args) {
        Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, args);
        return cnt == null ? 0 : cnt;
    }
}