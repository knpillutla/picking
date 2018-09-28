package com.example.picking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.picking.db.Pick;
import com.example.picking.db.PickingRepository;
import com.example.picking.dto.converter.EntityDTOConverter;
import com.example.picking.dto.events.PickConfirmationEvent;
import com.example.picking.dto.events.PickCreatedEvent;
import com.example.picking.dto.requests.PickConfirmRequestDTO;
import com.example.picking.dto.requests.PickCreationRequestDTO;
import com.example.picking.dto.responses.PickDTO;
import com.example.util.service.EventPublisher;

@Service
public class PickingServiceImpl implements PickingService {
	private static final Logger logger = LoggerFactory.getLogger(PickingServiceImpl.class);
	
	@Autowired
	PickingRepository pickDAO;
	
	@Autowired
	EventPublisher eventPublisher;
	
	public enum PickStatus {
		CREATED(100), LOCKED(105), RELEASED(110), ASSIGNED(115), PICKED(120), SHORTED(140), CANCELLED(199);
		PickStatus(Integer statCode) {
			this.statCode = statCode;
		}

		private Integer statCode;

		public Integer getStatCode() {
			return statCode;
		}
	}
	
	@Override
	@Transactional
	public PickDTO assignNextPick(String busName, Integer locnNbr, String userId) throws Exception {
		// find the next pick which is in status READY order by pickId(to start with), batchNbr, priority, createdDttm
		Pick pickEntity = pickDAO.findNextPickId(busName, locnNbr, PickStatus.RELEASED.getStatCode());
		pickEntity.setStatCode(PickStatus.ASSIGNED.getStatCode());
		pickEntity.setUserId(userId);
		Pick savedPickEntity = pickDAO.save(pickEntity);
		return EntityDTOConverter.getPickDTO(pickEntity);
	}

	@Override
	@Transactional
	public PickDTO assignNextPick(String busName, Integer locnNbr, String batchNbr, String userId) throws Exception {
		// find the next pick which is in status READY order by pickId(to start with), batchNbr, priority, createdDttm
		Pick pickEntity = pickDAO.findNextPickIdByBatchNbr(busName, locnNbr, batchNbr, PickStatus.RELEASED.getStatCode());
		pickEntity.setStatCode(PickStatus.ASSIGNED.getStatCode());
		pickEntity.setUserId(userId);
		Pick savedPickEntity = pickDAO.save(pickEntity);
		return EntityDTOConverter.getPickDTO(pickEntity);
	}

	@Override
	@Transactional
	public PickDTO confirmPick(PickConfirmRequestDTO pickConfirmRequest) throws Exception{
		logger.info("confirmPick Start, :" + pickConfirmRequest);
		PickDTO pickDTO = null;
		Optional<Pick> pickDtl = pickDAO.findById(pickConfirmRequest.getId());
		if(pickDtl.isPresent()) {
			Pick pickEntity = pickDtl.get();
			pickEntity.setPickedQty(pickEntity.getPickedQty() + pickConfirmRequest.getQtyPicked());
			pickEntity.setUserId(pickConfirmRequest.getUserId());
			pickEntity.setStatCode(PickStatus.PICKED.getStatCode());
			pickEntity.setToContainer(pickConfirmRequest.getToContainer());
			Pick updatedPickObj = pickDAO.save(pickEntity);
			pickDTO = EntityDTOConverter.getPickDTO(updatedPickObj);
			PickConfirmationEvent pickConfirmEvent = new PickConfirmationEvent(pickDTO);
			eventPublisher.publish(pickConfirmEvent);
			logger.info("confirmPick End, updated pick obj:" + pickDTO);
		}
		return pickDTO;
	}

	/* (non-Javadoc)
	 * @see com.example.demo.PickingService#createNew(com.example.AvroPickTask)
	 */
	@Override
	@Transactional
	public PickDTO createPick(PickCreationRequestDTO pickCreationReq) throws Exception {
		Pick newPickEntity = EntityDTOConverter.getPickEntity(pickCreationReq);
		newPickEntity.setStatCode(PickStatus.RELEASED.getStatCode());
		PickDTO pickDTO = EntityDTOConverter.getPickDTO(pickDAO.save(newPickEntity));
		PickCreatedEvent pickCreatedEvent = new PickCreatedEvent(pickDTO);
		eventPublisher.publish(pickCreatedEvent);
		logger.info("createPick End, created new pick:" + pickDTO);
		return pickDTO;
	}

	@Override
	public List<PickDTO> findByOrderId(String busName, Integer locnNbr, Long orderId) throws Exception {
		List<Pick> pickEntityList = pickDAO.findByBusNameAndLocnNbrAndOrderId(busName, locnNbr, orderId);
		List<PickDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pick pickEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPickDTO(pickEntity));
			}
		}
		return pickDTOList;
	}

	@Override
	@Transactional
	public List<PickDTO> releasePicksforOrder(String busName, Integer locnNbr, String orderNbr) throws Exception {
		List<Pick> pickEntityList = pickDAO.findByBusNameAndLocnNbrAndOrderNbr(busName, locnNbr, orderNbr);
		List<PickDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pick pickEntity : pickEntityList) {
				pickEntity.setStatCode(PickStatus.RELEASED.getStatCode());
				pickDTOList.add(EntityDTOConverter.getPickDTO(pickDAO.save(pickEntity)));
			}
		}
		return pickDTOList;
	}

	@Override
	@Transactional
	public List<PickDTO> releasePicksforBatch(String busName, Integer locnNbr, String batchNbr) throws Exception {
		List<Pick> pickEntityList = pickDAO.findByBusNameAndLocnNbrAndBatchNbr(busName, locnNbr, batchNbr);
		List<PickDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pick pickEntity : pickEntityList) {
				pickEntity.setStatCode(PickStatus.RELEASED.getStatCode());
				pickDTOList.add(EntityDTOConverter.getPickDTO(pickDAO.save(pickEntity)));
			}
		}
		return pickDTOList;
	}

	@Override
	public PickDTO findByPickId(String busName, Integer locnNbr, Long pickId) throws Exception {
		Pick pickEntity = pickDAO.findByPickId(busName, locnNbr, pickId);
		return EntityDTOConverter.getPickDTO(pickEntity);
	}

	@Override
	public List<PickDTO> findByOrderNbr(String busName, Integer locnNbr, String orderNbr) throws Exception {
		List<Pick> pickEntityList = pickDAO.findByBusNameAndLocnNbrAndOrderNbr(busName, locnNbr, orderNbr);
		List<PickDTO> pickDTOList = new ArrayList();
		if(pickEntityList!=null) {
			for(Pick pickEntity : pickEntityList) {
				pickDTOList.add(EntityDTOConverter.getPickDTO(pickEntity));
			}
		}
		return pickDTOList;
	}

}
