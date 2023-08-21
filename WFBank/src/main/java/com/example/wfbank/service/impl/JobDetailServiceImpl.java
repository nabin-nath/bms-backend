package com.example.wfbank.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.wfbank.model.JobDetail;
import com.example.wfbank.repository.JobDetailRepository;

@Service
@Validated
public class JobDetailServiceImpl {
	
	private JobDetailRepository jobDetailRepository;
	
	public JobDetailServiceImpl(JobDetailRepository jobDetailRepository) {
		this.jobDetailRepository = jobDetailRepository;
	}
	
	public JobDetail saveJobDetail(JobDetail jobDetail) {
		// TODO Auto-generated method stub		
		return jobDetailRepository.save(jobDetail);
	}

}