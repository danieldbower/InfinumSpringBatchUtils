package com.infinum.utils.springbatch

import groovy.transform.CompileStatic

import org.springframework.batch.item.adapter.ItemProcessorAdapter


@CompileStatic
/**
 * Adds a additional parameters to the underlying "process" call
 */
class ExtendedProcessorAdapter<I,O> extends ItemProcessorAdapter<I, O> {
	
	List<Object> extensions
	
	@Override
	public O process(I item) throws Exception {
		//original contents of ItemProcessor:
		//return invokeDelegateMethodWithArgument(item)
		
		Object[] args = ([item] + extensions)  as Object[]
		
		return invokeDelegateMethodWithArguments(args)
	}
	
}
