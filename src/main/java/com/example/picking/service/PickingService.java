package com.example.picking.service;

import java.util.List;

import com.example.picking.dto.requests.PickConfirmRequestDTO;
import com.example.picking.dto.requests.PickCreationRequestDTO;
import com.example.picking.dto.requests.PicklistCreationRequestDTO;
import com.example.picking.dto.responses.PickDTO;
import com.example.picking.dto.responses.PicklistDTO;

public interface PickingService {
	public PickDTO createPick(PickCreationRequestDTO pickCreationRequest) throws Exception;
	
	public PickDTO confirmPick(PickConfirmRequestDTO pickConfirmRequest) throws Exception;

	public List<PickDTO> findByOrderId(String busName, Integer locnNbr, Long orderId) throws Exception;

	public List<PickDTO> findByOrderNbr(String busName, Integer locnNbr, String orderNbr) throws Exception;

	PickDTO findByPickId(String busName, Integer locnNbr, Long pickDtlId) throws Exception;

	List<PickDTO> releasePicksforOrder(String busName, Integer locnNbr, String orderNbr) throws Exception;

	List<PickDTO> releasePicksforBatch(String busName, Integer locnNbr, String batchNbr) throws Exception;

	PickDTO assignNextPick(String busName, Integer locnNbr, String userId) throws Exception;

	PickDTO assignNextPick(String busName, Integer locnNbr, String batchNbr, String userId) throws Exception;
}