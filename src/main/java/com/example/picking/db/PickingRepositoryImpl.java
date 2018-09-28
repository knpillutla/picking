package com.example.picking.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class PickingRepositoryImpl implements PickingRepositoryCustom {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Pick findNextPickId(String busName, Integer locnNbr, Integer statCode) {
		Query query = entityManager.createQuery(
				"select s from Pick s where s.busName=:busName and s.locnNbr=:locnNbr and s.statCode=:statCode order by s.id desc",
				Pick.class);
		query.setParameter("busName", busName);
		query.setParameter("locnNbr", locnNbr);
		query.setParameter("statCode", statCode);
		query.setMaxResults(1);
		return (Pick) query.getSingleResult();
	}

	@Override
	public Pick findNextPickIdByBatchNbr(String busName, Integer locnNbr, String batchNbr, Integer statCode) {
		Query query = entityManager.createQuery(
				"select s from Pick s where s.busName=:busName and s.locnNbr=:locnNbr and s.batchNbr=:batchNbr and s.statCode=:statCode order by s.id desc",
				Pick.class);
		query.setParameter("busName", busName);
		query.setParameter("locnNbr", locnNbr);
		query.setParameter("batchNbr", batchNbr);
		query.setParameter("statCode", statCode);
		query.setMaxResults(1);
		return (Pick) query.getSingleResult();
	}

}
