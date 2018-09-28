package com.example.picking.endpoint.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import com.example.inventory.dto.events.InventoryAllocatedEvent;
import com.example.picking.dto.converter.InventoryToPickConverter;
import com.example.picking.service.PickingService;
import com.example.picking.streams.PickingStreams;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PickListener {
	@Autowired
	PickingService pickService;

	@StreamListener(target = PickingStreams.INVENTORY_OUTPUT, 
			condition = "headers['eventName']=='InventoryAllocatedEvent'")
	public void handleInventoryAllocatedEvent(InventoryAllocatedEvent inventoryAllocatedEvent) { 
		log.info("Received InventoryAllocatedEvent for: {}" + ": at :" + new java.util.Date(), inventoryAllocatedEvent);
		long startTime = System.currentTimeMillis();
		try {
			pickService.createPick(InventoryToPickConverter.createPickCreationRequest(inventoryAllocatedEvent));
			long endTime = System.currentTimeMillis();
			log.info("Completed Processing InventoryAllocatedEvent for: " + inventoryAllocatedEvent + ": at :"
					+ new java.util.Date() + " : total time:" + (endTime - startTime) / 1000.00 + " secs");
		} catch (Exception e) {
			e.printStackTrace();
			long endTime = System.currentTimeMillis();
			log.error("Error Completing InventoryAllocatedEvent for: " + inventoryAllocatedEvent + ": at :"
					+ new java.util.Date() + " : total time:" + (endTime - startTime) / 1000.00 + " secs", e);
		}
	}
}
