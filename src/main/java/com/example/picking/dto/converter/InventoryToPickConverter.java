package com.example.picking.dto.converter;

import org.springframework.stereotype.Component;

import com.example.inventory.dto.events.InventoryAllocatedEvent;
import com.example.inventory.dto.responses.InventoryDTO;
import com.example.picking.dto.requests.PickCreationRequestDTO;

@Component
public class InventoryToPickConverter {
	public static PickCreationRequestDTO createPickCreationRequest(InventoryAllocatedEvent invnAllocatedEvent) {
		InventoryDTO inventoryDTO = invnAllocatedEvent.getInventoryDTO();
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
