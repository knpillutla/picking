package com.threedsoft.picking.endpoint.listener;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import com.threedsoft.inventory.dto.events.InventoryAllocatedEvent;
import com.threedsoft.picking.dto.converter.InventoryToPickConverter;
import com.threedsoft.picking.service.PickingService;
import com.threedsoft.picking.streams.PickingStreams;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PickListener {
	@Autowired
	PickingService pickService;

	@StreamListener(target = PickingStreams.INVENTORY_OUTPUT, 
			condition = "headers['eventName']=='InventoryAllocatedEvent'")
	public void handleInventoryAllocatedEvent(InventoryAllocatedEvent inventoryAllocatedEvent) { 
		log.info("Received InventoryAllocatedEvent for: {}" + ": at :" + LocalDateTime.now(), inventoryAllocatedEvent);
		long startTime = System.currentTimeMillis();
		try {
			pickService.createPick(InventoryToPickConverter.createPickCreationRequest(inventoryAllocatedEvent));
			long endTime = System.currentTimeMillis();
			log.info("Completed Processing InventoryAllocatedEvent for: " + inventoryAllocatedEvent + ": at :"
					+ LocalDateTime.now() + " : total time:" + (endTime - startTime) / 1000.00 + " secs");
		} catch (Exception e) {
			e.printStackTrace();
			long endTime = System.currentTimeMillis();
			log.error("Error Completing InventoryAllocatedEvent for: " + inventoryAllocatedEvent + ": at :"
					+ LocalDateTime.now() + " : total time:" + (endTime - startTime) / 1000.00 + " secs", e);
		}
	}
}
