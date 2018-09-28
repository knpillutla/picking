package com.example.picking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.picking.streams.PickingStreams;
import com.example.util.service.EventPublisher;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableAutoConfiguration
@EnableBinding(PickingStreams.class)
@EnableScheduling
@Slf4j
public class PickingApplication {
	@Autowired
	PickingStreams pickingStreams;
	
	public static void main(String[] args) {
		SpringApplication.run(PickingApplication.class, args);
	}
	@Bean
	public EventPublisher eventPublisher() {
		return new EventPublisher(pickingStreams.outboundPick());
	}	
	
/*	@Bean
	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "1"))
	public MessageSource<String> timerMessageSource() {
		return () -> MessageBuilder.withPayload("hello").build();
	}	
*/	
}
