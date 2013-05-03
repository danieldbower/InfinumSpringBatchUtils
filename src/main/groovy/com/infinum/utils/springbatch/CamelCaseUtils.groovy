package com.infinum.utils.springbatch

/**
 * Java/Groovy properties follow a camel casing standard.  This class provides
 * translation to/from that standard.
 */
class CamelCaseUtils {

	/**
	 * Where underscore means underscore separated word:
	 * 
	 * example:  abc_def_ghi
	 * gives:  abcDefGhi
	 */
	static String underscoreToCamelCase(String underscore){
		if(!underscore || underscore.isAllWhitespace()){
			return ''
		}
		return underscore.replaceAll(/_\w/){ it[1].toUpperCase() }
	}
	
	/**
	 * opposite of underscoreToCamelCase
	 * 
	 * example: abcDefGhi
	 * gives:  abc_def_ghi
	 */
	static String camelCaseToUnderscore(String camelCase) {
		return camelCase.replaceAll(/\B[A-Z]/) { '_' + it }.toLowerCase()
	}
}
