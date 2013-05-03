package com.infinum.utils.springbatch

import java.util.List;

import javax.sql.DataSource

import org.springframework.batch.core.ItemReadListener
import org.springframework.batch.core.ItemWriteListener
import org.springframework.jdbc.core.JdbcTemplate

/**
 * Sets a single column in a row to newValForColumn by listening for Item
 */
class ProcessIndicatorSettingItemReadListener implements ItemWriteListener{

	private JdbcTemplate jdbcTemplate
	private String sqlUpdateStatement
	
	/**
	 * The property in the write record that contains the original id
	 */
	String writeRecordIdPropertyName = 'id'
	
	/**
	 * Table to apply the update the statement
	 */
	String tableName
	
	/**
	 * Column to be updated
	 */
	String columnName
	
	/**
	 * The new value for the column to be updated
	 */
	String newValForColumn
	
	/**
	 * If the new value for the column is calculated by a sql formula, we
	 * shouldn't quote it.
	 */
	Boolean newValForColumnIsFormula = false
	
	/**
	 * To be more strict about what is allowed to be updated
	 */
	List<String> oldValsForColumn
	
	/**
	 * If you want to be more strict about what is allowed to be updated, set 
	 * strictUpdate to true and specify oldValsForColumn that are acceptable.
	 */
	Boolean strictUpdate = false
	
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new JdbcTemplate(dataSource)
	}
	
	@Override
	public void afterWrite(List writes) {
		buildSql()
		
		writes.each{row ->
			int numUpdates = jdbcTemplate.update(sqlUpdateStatement, row[writeRecordIdPropertyName])
		}
	}

	private void buildSql(){
		if(!sqlUpdateStatement){
			StringBuilder sqlStatement= new StringBuilder()
			sqlStatement.append "update $tableName set $columnName="
			
			if(newValForColumnIsFormula){
				sqlStatement.append newValForColumn
			}else{
				sqlStatement.append "'$newValForColumn'"
			}
			
			sqlStatement.append " where id=? "
			
			if(strictUpdate){
				sqlStatement.append "and $columnName"
				
				if(oldValsForColumn==null || oldValsForColumn.isEmpty()){
					sqlStatement.append(" is null")
				}else if(oldValsForColumn.size()==1){
					sqlStatement.append(" = ${oldValsForColumn[0]}")
				}else{
					sqlStatement.append(" in (")
					oldValsForColumn.eachWithIndex{ oldValForColumn, index ->
						sqlStatement.append(" '$oldValForColumn'")
						if((index+1)!=oldValsForColumn.size()){
							sqlStatement.append(",")
						}
					}
					sqlStatement.append(")")
				}
			}
			
			sqlUpdateStatement = sqlStatement.toString()
		}
	}

	@Override
	public void beforeWrite(List arg0) {
		// no-op
	}

	@Override
	public void onWriteError(Exception arg0, List arg1) {
		// no-op
	}

}
