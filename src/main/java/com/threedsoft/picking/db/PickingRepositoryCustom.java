package com.threedsoft.picking.db;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface PickingRepositoryCustom {
	public Pick findNextPickId(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("statCode") Integer statCode);

	public Pick findNextPickIdByBatchNbr(@Param("busName") String busName, @Param("locnNbr") Integer locnNbr, @Param("batchNbr") String batchNbr,@Param("statCode") Integer statCode);

}
