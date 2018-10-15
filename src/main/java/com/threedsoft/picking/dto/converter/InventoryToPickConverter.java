package com.threedsoft.picking.dto.converter;

import org.springframework.stereotype.Component;

import com.threedsoft.inventory.dto.events.InventoryAllocatedEvent;
import com.threedsoft.inventory.dto.responses.InventoryResourceDTO;
import com.threedsoft.picking.dto.requests.PickCreationRequestDTO;
import com.threedsoft.util.dto.events.EventResourceConverter;

@Component
public class InventoryToPickConverter {
	public static PickCreationRequestDTO createPickCreationRequest(InventoryAllocatedEvent invnAllocatedEvent) {
		InventoryResourceDTO inventoryDTO = (InventoryResourceDTO) EventResourceConverter
				.getObject(invnAllocatedEvent.getEventResource(), invnAllocatedEvent.getEventResourceClassName());
		PickCreationRequestDTO pickReq = new PickCreationRequestDTO();
		pickReq.setBusName(inventoryDTO.getBusName());
		pickReq.setLocnNbr(inventoryDTO.getLocnNbr());
		pickReq.setBusUnit(inventoryDTO.getBusUnit());
		pickReq.setItemBrcd(inventoryDTO.getItemBrcd());
		pickReq.setLocnBrcd(inventoryDTO.getLocnBrcd());
		pickReq.setOrderNbr(inventoryDTO.getOrderNbr());
		pickReq.setOrderLineNbr(inventoryDTO.getOrderLineNbr());
		pickReq.setQty(inventoryDTO.getQty());
		pickReq.setPickedQty(0);
		pickReq.setBatchNbr(inventoryDTO.getBatchNbr());
		pickReq.setOrderId(inventoryDTO.getOrderId());
		pickReq.setOrderLineId(inventoryDTO.getOrderLineId());
		//pickReq.setCompany(invnAllocatedEvent);
		//pickReq.setDivision(division);
		pickReq.setUserId(inventoryDTO.getUserId());
		
		return pickReq;
	}
}
