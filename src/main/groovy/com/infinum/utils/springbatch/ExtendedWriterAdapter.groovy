package com.infinum.utils.springbatch

import java.util.List;

import org.springframework.batch.item.adapter.ItemWriterAdapter

class ExtendedWriterAdapter<T> extends ItemWriterAdapter {
	
	List<Object> extensions
	
	@Override
	public void write(List items) throws Exception {
		
		Object[] args
		
		for (T item : items) {
			args = ([item] + extensions)  as Object[]
			invokeDelegateMethodWithArguments(args)
		}
	}

}
