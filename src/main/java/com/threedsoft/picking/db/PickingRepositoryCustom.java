package com.threedsoft.picking.db;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.repository.query.Param;

public interface PickingRepositoryCustom {
	public Pick findNextPickId(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr,
			@Param("status") String status);

	public Pick findAssignedPickForUser(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr,
			@Param("userId") String userId);
	
	public Pick findNextPickIdByBatchNbr(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr,
			@Param("batchNbr") String batchNbr, @Param("status") String status);

	public <T> List<T> findAllEntitiesOrderedBy(Class<T> entityClass, String orderByColumn, boolean ascending);
}
