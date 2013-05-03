package com.infinum.utils.springbatch

import groovy.sql.GroovyRowResult

import java.sql.ResultSet
import java.sql.SQLException

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.RowMapper

/**
 * This class can be used instead of a more strongly typed RowMapper to
 * take advantage of Groovy's ability to pass a map in the constructor of
 * an object with all the properties
 *
 * Normally, we would just use Groovy's sql classes, but this class is geared
 * toward use with Spring Batch's steps (which relies on Spring JDBC)
 * 
 * To use: enter in your Batch DSL:
 * beanName(GroovyRowMapper, ClassToReturnFromMapRow)
 */
class GroovyRowMapper implements RowMapper {

	GroovyRowMapper(Class mappedClass){
		this.mappedClass = mappedClass
	}
	
	Logger log = LoggerFactory.getLogger(getClass())
	
	private Class mappedClass
	
	private Map columnNameToPropertyNameMapping = null

	public Object mapRow(ResultSet rs, int arg1) throws SQLException {
		
		GroovyRowResult gResult = rs.toRowResult()
		
		if(!columnNameToPropertyNameMapping){
			initColumnNameToPropertyNameMapping(gResult)
		}
		
		Map translatedProperties = translateColumnNames(gResult)
		
		try {
			return mappedClass.newInstance(translatedProperties);
		} catch (InstantiationException e) {
			log.error("unable to instantiate Transfer object in factory.");
		} catch (IllegalAccessException e) {
			log.error("unable to instantiate Transfer object in factory.");
		}
		return null
	}

	private void initColumnNameToPropertyNameMapping(GroovyRowResult gResult){
		Map c2pMapping = [:]
		gResult.keySet().each{
			c2pMapping.put(it, CamelCaseUtils.underscoreToCamelCase(it))
		}
		columnNameToPropertyNameMapping = c2pMapping
	}

	private Map translateColumnNames(GroovyRowResult gResult){
		Map translated = [:]
		gResult.each{ k, v ->
			translated.put(columnNameToPropertyNameMapping[k],v)
		}
		return translated
	}

}
