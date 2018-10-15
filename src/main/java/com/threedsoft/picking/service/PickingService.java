package com.threedsoft.picking.service;

import java.util.List;

import com.threedsoft.picking.dto.requests.PickConfirmRequestDTO;
import com.threedsoft.picking.dto.requests.PickCreationRequestDTO;
import com.threedsoft.picking.dto.requests.PicklistCreationRequestDTO;
import com.threedsoft.picking.dto.responses.PickResourceDTO;
import com.threedsoft.picking.dto.responses.PicklistResourceDTO;

public interface PickingService {
	public PickResourceDTO createPick(PickCreationRequestDTO pickCreationRequest) throws Exception;
	
	public PickResourceDTO confirmPick(PickConfirmRequestDTO pickConfirmRequest) throws Exception;

	public List<PickResourceDTO> findByOrderId(String busName, Integer locnNbr, Long orderId) throws Exception;

	public List<PickResourceDTO> findByOrderNbr(String busName, Integer locnNbr, String orderNbr) throws Exception;

	PickResourceDTO findByPickId(String busName, Integer locnNbr, Long pickDtlId) throws Exception;

	List<PickResourceDTO> releasePicksforOrder(String busName, Integer locnNbr, String orderNbr) throws Exception;

	List<PickResourceDTO> releasePicksforBatch(String busName, Integer locnNbr, String batchNbr) throws Exception;

	PickResourceDTO assignNextPick(String busName, Integer locnNbr, String userId) throws Exception;

	PickResourceDTO assignNextPick(String busName, Integer locnNbr, String batchNbr, String userId) throws Exception;
}