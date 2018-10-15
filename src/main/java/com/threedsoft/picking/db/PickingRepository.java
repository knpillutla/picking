package com.threedsoft.picking.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PickingRepository extends JpaRepository<Pick, Long>, PickingRepositoryCustom{
	@Query("select s from Pick s where s.busName=:busName and s.locnNbr=:locnNbr and s.orderId=:orderId")
	public List<Pick> findByBusNameAndLocnNbrAndOrderId(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("orderId") Long orderId);

	@Query("select s from Pick s where s.busName=:busName and s.locnNbr=:locnNbr and s.orderNbr=:orderNbr")
	public List<Pick> findByBusNameAndLocnNbrAndOrderNbr(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("orderNbr") String orderNbr);

	@Query("select s from Pick s where s.busName=:busName and s.locnNbr=:locnNbr and s.batchNbr=:batchNbr")
	public List<Pick> findByBusNameAndLocnNbrAndBatchNbr(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("batchNbr") String batchNbr);

	@Query("select s from Pick s where s.busName=:busName and s.locnNbr=:locnNbr and s.id=:pickId")
	public Pick findByPickId(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("pickId") Long pickId);
}
