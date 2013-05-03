package com.infinum.utils.springbatch

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper

class GroovyRowResultMapper implements RowMapper{

	@Override
	public Object mapRow(ResultSet rs, int arg1) throws SQLException {
		return rs.toRowResult()
	}

}
