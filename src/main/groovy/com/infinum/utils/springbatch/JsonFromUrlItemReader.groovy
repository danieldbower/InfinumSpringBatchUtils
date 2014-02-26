package com.infinum.utils.springbatch

import groovy.json.JsonSlurper
import groovy.util.logging.Log4j

import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.NonTransientResourceException
import org.springframework.batch.item.ParseException
import org.springframework.batch.item.UnexpectedInputException

@Log4j
class JsonFromUrlItemReader<T> implements ItemReader<T> {

	private String url
	private List items
	JsonSlurper slurper = new JsonSlurper()
	
	JsonFromUrlItemReader(String url){
		this.url = url
	}
	
	void fetchFromUrl(){
		items = null
		
		Reader urlReader = url.toURL()?.newReader()
		def json = slurper.parse(urlReader)
		
		log.debug("Json from $url:" + json.toString())
		
		if(json instanceof List){
			items = json
		}else if(json instanceof Map){
			if(json.data && json.data instanceof List){
				items = json.data
			}
		}
	}
	
	@Override
	T read() throws Exception, UnexpectedInputException, ParseException,
			NonTransientResourceException {
		
		if(items == null){
			fetchFromUrl()
		}
	
		if(items){
			return items.remove(0);
		}
		
		items = null
		return null;
	}

}
