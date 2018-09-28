package com.example.picking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.example.picking.dto.events.BaseEvent;
import com.example.picking.streams.PickingStreams;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableBinding(PickingStreams.class)
@Slf4j
public class EventPublisher {
	@Autowired
	private PickingStreams pickingStreams;

	public void publish(BaseEvent event) {
		log.info("Sending event {}", event);
		MessageChannel messageChannel = pickingStreams.outboundPick();
		MessageHeaderAccessor msgHdrAccessor = new MessageHeaderAccessor();
		msgHdrAccessor.copyHeadersIfAbsent(event.getHeaderMap());
		messageChannel.send(MessageBuilder.withPayload(event)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
				.setHeaders(msgHdrAccessor)
				.build());
	}
}
