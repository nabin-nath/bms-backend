package com.example.wfbank.util;

import java.io.Serializable;
import java.util.Random;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomIdGenerator implements IdentifierGenerator{
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) {
//		int length  =10;
		long max = 9999999999L;
		long min = 1000000000L;
		Random random = new Random();
		
		long id;
		id = min + (long)(random.nextDouble()*(max-min));
		
		LOGGER.info("ID generated: "+id);
		return id;
	}
}
