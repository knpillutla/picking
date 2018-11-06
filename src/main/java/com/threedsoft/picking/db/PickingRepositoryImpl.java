package com.threedsoft.picking.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class PickingRepositoryImpl implements PickingRepositoryCustom {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Pick findNextPickId(String busName, Integer locnNbr, String status) {
		Query query = entityManager.createQuery(
				"select s from Pick s where s.busName=:busName and s.locnNbr=:locnNbr and s.status=:status order by s.id desc",
				Pick.class);
		query.setParameter("busName", busName);
		query.setParameter("locnNbr", locnNbr);
		query.setParameter("status", status);
		query.setMaxResults(1);
		return (Pick) query.getSingleResult();
	}

	@Override
	public Pick findNextPickIdByBatchNbr(String busName, Integer locnNbr, String batchNbr, String status) {
		Query query = entityManager.createQuery(
				"select s from Pick s where s.busName=:busName and s.locnNbr=:locnNbr and s.batchNbr=:batchNbr and s.status=:status order by s.id desc",
				Pick.class);
		query.setParameter("busName", busName);
		query.setParameter("locnNbr", locnNbr);
		query.setParameter("batchNbr", batchNbr);
		query.setParameter("status", status);
		query.setMaxResults(1);
		return (Pick) query.getSingleResult();
	}

	public <T> List<T> findAllEntitiesOrderedBy(Class<T> entityClass, String orderByColumn, boolean ascending) {
	    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

	    CriteriaQuery<T> criteria = builder.createQuery(entityClass);
	    Root<T> entityRoot = criteria.from(entityClass);
	    criteria.select(entityRoot);
	    javax.persistence.criteria.Order order = ascending ? builder.asc(entityRoot.get(orderByColumn))
	        : builder.desc(entityRoot.get(orderByColumn));
	    criteria.orderBy(order);
	    return entityManager.createQuery(criteria).getResultList();
	}
	
}
