package com.infinum.utils.springbatch

import org.junit.Test

class JsonFromUrlItemReaderTest {

	@Test
	void fetchFromUrlTestPagedResponse(){
		JsonFromUrlItemReader reader = new JsonFromUrlItemReader('https://bowerstudios.com/testing/jsonMap.json')
		
		def obj1 = reader.read()
		assert obj1
		
		def obj2 = reader.read()
		assert obj2
		
		assert null == reader.read()
		
		def obj3 = reader.read()
		assert obj3
	}
	
	
	@Test
	void fetchFromUrlTestList(){
		JsonFromUrlItemReader reader = new JsonFromUrlItemReader('https://bowerstudios.com/testing/jsonList.json')
		
		def obj1 = reader.read()
		assert obj1
		
		def obj2 = reader.read()
		assert obj2
		
		assert null == reader.read()
	}
}
