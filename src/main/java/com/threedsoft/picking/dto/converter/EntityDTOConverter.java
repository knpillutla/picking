package com.threedsoft.picking.dto.converter;

import org.springframework.stereotype.Component;

import com.threedsoft.picking.db.Pick;
import com.threedsoft.picking.dto.requests.PickCreationRequestDTO;
import com.threedsoft.picking.dto.responses.PickResourceDTO;

@Component
public class EntityDTOConverter {

	public static PickResourceDTO getPickDTO(Pick pickEntity) {
		if (pickEntity == null)
			return null;
		PickResourceDTO pickDTO = new PickResourceDTO(pickEntity.getId(), pickEntity.getOrderLineId(), pickEntity.getBatchNbr(),
				pickEntity.getBusName(), pickEntity.getLocnNbr(), pickEntity.getBusUnit(), pickEntity.getCompany(),
				pickEntity.getDivision(), pickEntity.getLocnBrcd(), pickEntity.getItemBrcd(), pickEntity.getQty(),
				pickEntity.getPickedQty(), pickEntity.getFromContainer(), pickEntity.getToContainer(),
				pickEntity.getStatCode(), pickEntity.getOrderId(), pickEntity.getOrderNbr(), pickEntity.getPackageNbr(),
				pickEntity.getOrderLineNbr(), pickEntity.getTransName(), pickEntity.getSource(),
				pickEntity.getHostName(), pickEntity.getUserId());
		return pickDTO;
	}

	public static Pick getPickEntity(PickCreationRequestDTO pickCreationReqDTO) {
		Pick newPickEntity = new Pick();
		newPickEntity.setBusName(pickCreationReqDTO.getBusName());
		newPickEntity.setLocnNbr(pickCreationReqDTO.getLocnNbr());
		newPickEntity.setLocnBrcd(pickCreationReqDTO.getLocnBrcd());
		newPickEntity.setItemBrcd(pickCreationReqDTO.getItemBrcd());
		newPickEntity.setQty(pickCreationReqDTO.getQty());
		newPickEntity.setOrderId(pickCreationReqDTO.getOrderId());
		newPickEntity.setOrderNbr(pickCreationReqDTO.getOrderNbr());
		newPickEntity.setOrderLineNbr(pickCreationReqDTO.getOrderLineNbr());
		newPickEntity.setBusUnit(pickCreationReqDTO.getBusUnit());
		newPickEntity.setBatchNbr(pickCreationReqDTO.getBatchNbr());
		newPickEntity.setCompany(pickCreationReqDTO.getCompany());
		newPickEntity.setDivision(pickCreationReqDTO.getDivision());
		newPickEntity.setFromContainer(pickCreationReqDTO.getFromContainer());
		newPickEntity.setToContainer(pickCreationReqDTO.getToContainer());
		newPickEntity.setPickedQty(0);
		newPickEntity.setOrderLineId(pickCreationReqDTO.getOrderLineId());
		return newPickEntity;
	}
}
