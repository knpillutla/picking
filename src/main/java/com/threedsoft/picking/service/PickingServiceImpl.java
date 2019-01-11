package com.threedsoft.picking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.threedsoft.picking.db.Pick;
import com.threedsoft.picking.db.PickingRepository;
import com.threedsoft.picking.dto.converter.PickDTOConverter;
import com.threedsoft.picking.dto.events.PickConfirmationEvent;
import com.threedsoft.picking.dto.events.PickCreatedEvent;
import com.threedsoft.picking.dto.requests.PickConfirmRequestDTO;
import com.threedsoft.picking.dto.requests.PickCreationRequestDTO;
import com.threedsoft.picking.dto.requests.PickSearchRequestDTO;
import com.threedsoft.picking.dto.responses.PickResourceDTO;
import com.threedsoft.picking.util.PickStatus;
import com.threedsoft.picking.util.PickingConstants;
import com.threedsoft.util.service.EventPublisher;

@Service
public class PickingServiceImpl implements PickingService {
	private static final Logger logger = LoggerFactory.getLogger(PickingServiceImpl.class);

	@Autowired
	PickingRepository pickDAO;

	@Autowired
	EventPublisher eventPublisher;

	@Autowired
	PickDTOConverter pickDTOConverter;

	@Override
	@Transactional
	public PickResourceDTO assignNextPick(String busName, Integer locnNbr, String userId) throws Exception {
		// find the next pick which is in status READY order by pickId(to start with),
		// batchNbr, priority, createdDttm
		List<Pick> pickEntityList = pickDAO.findAssignedPickForUser(busName, locnNbr, userId);
		Pick pickEntity = null;
		if(pickEntityList.size()>=1) {
			pickEntity = pickEntityList.get(0);
		}
		else {
			pickEntity = pickDAO.findNextPickId(busName, locnNbr, PickStatus.RELEASED.getStatus());
			pickEntity.setStatus(PickStatus.ASSIGNED.getStatus());
			pickEntity.setUserId(userId);
			pickEntity = pickDAO.save(pickEntity);
		}
		return PickDTOConverter.getPickDTO(pickEntity);
	}

	@Override
	@Transactional
	public PickResourceDTO assignNextPick(String busName, Integer locnNbr, String batchNbr, String userId)
			throws Exception {
		// find the next pick which is in status READY order by pickId(to start with),
		// batchNbr, priority, createdDttm
		Pick pickEntity = pickDAO.findNextPickIdByBatchNbr(busName, locnNbr, batchNbr, PickStatus.RELEASED.getStatus());
		pickEntity.setStatus(PickStatus.ASSIGNED.getStatus());
		pickEntity.setUserId(userId);
		Pick savedPickEntity = pickDAO.save(pickEntity);
		return PickDTOConverter.getPickDTO(pickEntity);
	}

	@Override
	@Transactional
	public PickResourceDTO confirmPick(PickConfirmRequestDTO pickConfirmRequest) throws Exception {
		logger.info("confirmPick Start, :" + pickConfirmRequest);
		PickResourceDTO pickDTO = null;
		Optional<Pick> pickDtl = pickDAO.findById(pickConfirmRequest.getId());
		if (pickDtl.isPresent()) {
			Pick pickEntity = pickDtl.get();
			pickEntity.setPickedQty(pickEntity.getPickedQty() + pickConfirmRequest.getQtyPicked());
			pickEntity.setUserId(pickConfirmRequest.getUserId());
			pickEntity.setStatus(PickStatus.PICKED.getStatus());
			pickEntity.setToContainer(pickConfirmRequest.getToContainer());
			Pick updatedPickObj = pickDAO.save(pickEntity);
			pickDTO = PickDTOConverter.getPickDTO(updatedPickObj);
			PickConfirmationEvent pickConfirmEvent = new PickConfirmationEvent(pickDTO,
					PickingConstants.PICKING_SERVICE_NAME);
			eventPublisher.publish(pickConfirmEvent);
			logger.info("confirmPick End, updated pick obj:" + pickDTO);
		}
		return pickDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.demo.PickingService#createNew(com.example.AvroPickTask)
	 */
	@Override
	@Transactional
	public PickResourceDTO createPick(PickCreationRequestDTO pickCreationReq) throws Exception {
		Pick newPickEntity = PickDTOConverter.getPickEntity(pickCreationReq);
		newPickEntity.setStatus(PickStatus.RELEASED.getStatus());
		PickResourceDTO pickDTO = PickDTOConverter.getPickDTO(pickDAO.save(newPickEntity));
		PickCreatedEvent pickCreatedEvent = new PickCreatedEvent(pickDTO, PickingConstants.PICKING_SERVICE_NAME);
		eventPublisher.publish(pickCreatedEvent);
		logger.info("createPick End, created new pick:" + pickDTO);
		return pickDTO;
	}

	@Override
	public List<PickResourceDTO> findByOrderId(String busName, Integer locnNbr, Long orderId) throws Exception {
		List<Pick> pickEntityList = pickDAO.findByBusNameAndLocnNbrAndOrderId(busName, locnNbr, orderId);
		List<PickResourceDTO> pickDTOList = new ArrayList();
		if (pickEntityList != null) {
			for (Pick pickEntity : pickEntityList) {
				pickDTOList.add(PickDTOConverter.getPickDTO(pickEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	@Transactional
	public List<PickResourceDTO> releasePicksforOrder(String busName, Integer locnNbr, String orderNbr)
			throws Exception {
		List<Pick> pickEntityList = pickDAO.findByBusNameAndLocnNbrAndOrderNbr(busName, locnNbr, orderNbr);
		List<PickResourceDTO> pickDTOList = new ArrayList();
		if (pickEntityList != null) {
			for (Pick pickEntity : pickEntityList) {
				pickEntity.setStatus(PickStatus.RELEASED.getStatus());
				pickDTOList.add(PickDTOConverter.getPickDTO(pickDAO.save(pickEntity)));
			}
		}
		return pickDTOList;
	}

	@Override
	@Transactional
	public List<PickResourceDTO> releasePicksforBatch(String busName, Integer locnNbr, String batchNbr)
			throws Exception {
		List<Pick> pickEntityList = pickDAO.findByBusNameAndLocnNbrAndBatchNbr(busName, locnNbr, batchNbr);
		List<PickResourceDTO> pickDTOList = new ArrayList();
		if (pickEntityList != null) {
			for (Pick pickEntity : pickEntityList) {
				pickEntity.setStatus(PickStatus.RELEASED.getStatus());
				pickDTOList.add(PickDTOConverter.getPickDTO(pickDAO.save(pickEntity)));
			}
		}
		return pickDTOList;
	}

	@Override
	public PickResourceDTO findByPickId(String busName, Integer locnNbr, Long pickId) throws Exception {
		Pick pickEntity = pickDAO.findByPickId(busName, locnNbr, pickId);
		return PickDTOConverter.getPickDTO(pickEntity);
	}

	@Override
	public List<PickResourceDTO> findByOrderNbr(String busName, Integer locnNbr, String orderNbr) throws Exception {
		List<Pick> pickEntityList = pickDAO.findByBusNameAndLocnNbrAndOrderNbr(busName, locnNbr, orderNbr);
		List<PickResourceDTO> pickDTOList = new ArrayList();
		if (pickEntityList != null) {
			for (Pick pickEntity : pickEntityList) {
				pickDTOList.add(PickDTOConverter.getPickDTO(pickEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PickResourceDTO> findByBusNameAndLocnNbr(String busName, Integer locnNbr) throws Exception {
		PageRequest pageRequest = new PageRequest(0, 20);
		List<Pick> pickEntityList = pickDAO.findByBusNameAndLocnNbr(busName, locnNbr, pageRequest);
		List<PickResourceDTO> pickDTOList = new ArrayList();
		if (pickEntityList != null) {
			for (Pick pickEntity : pickEntityList) {
				pickDTOList.add(PickDTOConverter.getPickDTO(pickEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	public List<PickResourceDTO> searchPicks(String busName, Integer locnNbr, PickSearchRequestDTO pickSearchReq)
			throws Exception {
		PageRequest pageRequest = new PageRequest(0, 50);
		Pick searchPicks = pickDTOConverter.getPickEntityForSearch(pickSearchReq);
		Page<Pick> pickEntityPage = pickDAO.findAll(Example.of(searchPicks), pageRequest);
		List<PickResourceDTO> pickDTOList = new ArrayList();
		for (Pick invnEntity : pickEntityPage) {
			pickDTOList.add(pickDTOConverter.getPickDTO(invnEntity));
		}
		return pickDTOList;
	}

}
