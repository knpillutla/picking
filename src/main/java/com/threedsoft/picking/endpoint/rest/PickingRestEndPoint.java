package com.threedsoft.picking.endpoint.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.threedsoft.picking.dto.requests.PickConfirmRequestDTO;
import com.threedsoft.picking.dto.requests.PickSearchRequestDTO;
import com.threedsoft.picking.service.PickingService;
import com.threedsoft.util.dto.ErrorResourceDTO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/picking/v1")
@Api(value = "Pick Service", description = "Operations pertaining to picking")
@RefreshScope
@Slf4j
public class PickingRestEndPoint {
	@Autowired
	PickingService pickingService;

	@Value("${wms.service.health.msg: Picking Service - Config Server is not working..please check}")
	private String healthMsg;

	@Value("${wms.service.ready.msg: Picking Service - Not ready yet}")
	private String readyMsg;

	@GetMapping("/ready")
	public ResponseEntity ready() throws Exception {
		return ResponseEntity.ok(readyMsg);
	}

	@GetMapping("/health")
	public ResponseEntity health() throws Exception {
		return ResponseEntity.ok(healthMsg);
	}

	@PostMapping("/{busName}/{locnNbr}/picks/next/{userId}")
	public ResponseEntity assignNextPick(@PathVariable("busName") String busName,
			@PathVariable("locnNbr") Integer locnNbr, @PathVariable("userId") String userId) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.assignNextPick(busName, locnNbr, userId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while getting next pick task"));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/picks/next/{batchNbr}/{userId}")
	public ResponseEntity assignNextPickByBatchNbr(@PathVariable("busName") String busName,
			@PathVariable("locnNbr") Integer locnNbr, @PathVariable("batchNbr") String batchNbr,
			@PathVariable("userId") String userId) throws IOException {
		try {
			log.info("Received assign next pick by batch nbr:" + busName + "," + locnNbr + "," + batchNbr + ","
					+ userId);
			return ResponseEntity.ok(pickingService.assignNextPick(busName, locnNbr, batchNbr, userId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while getting next pick task"));
		}
	}

	@GetMapping("/{busName}/{locnNbr}/picks/{id}")
	public ResponseEntity getByPickId(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr,
			@PathVariable("id") Long pickId) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.findByPickId(busName, locnNbr, pickId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while getting next pick task"));
		}
	}

	@GetMapping("/{busName}/{locnNbr}/picks")
	public ResponseEntity getPicks(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr)
			throws IOException {
		try {
			return ResponseEntity.ok(pickingService.findByBusNameAndLocnNbr(busName, locnNbr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while getting picks for busName:" + busName + ",locnNbr:" + locnNbr));
		}
	}

	@GetMapping("/{busName}/{locnNbr}/picks/order/{id}")
	public ResponseEntity getPicksByOrderId(@PathVariable("busName") String busName,
			@PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long orderId) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.findByOrderId(busName, locnNbr, orderId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error occured while getting next pick task"));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/picks/{id}")
	public ResponseEntity confirmPick(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr,
			@PathVariable("id") Long id, @RequestBody PickConfirmRequestDTO pickReq) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.confirmPick(pickReq));
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Error Occured for confirmPick request busName:" + busName + ", id:" + id + " : " + e.getMessage()));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/orders/release/{orderNbr}")
	public ResponseEntity releasePicksForOrder(@PathVariable("busName") String busName,
			@PathVariable("locnNbr") Integer locnNbr, @PathVariable("orderNbr") String orderNbr) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.releasePicksforOrder(busName, locnNbr, orderNbr));
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Error Occured while processing create pick list, busName:" + busName + ", locnNbr:"
									+ locnNbr + " : " + e.getMessage()));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/orders/release/batch/{batchNbr}")
	public ResponseEntity releasePicksForBatch(@PathVariable("busName") String busName,
			@PathVariable("locnNbr") Integer locnNbr, @PathVariable("batchNbr") String batchNbr) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.releasePicksforBatch(busName, locnNbr, batchNbr));
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Error Occured while processing create pick list, busName:" + busName + ", locnNbr:"
									+ locnNbr + " : " + e.getMessage()));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/picks/search")
	public ResponseEntity confirmPick(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr,
			@RequestBody PickSearchRequestDTO pickSearchReq) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.searchPicks(busName, locnNbr, pickSearchReq));
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new ErrorResourceDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Error Occured for pick search request busName:" + busName + ", pickSearchReq:" + pickSearchReq
									+ " : " + e.getMessage()));
		}
	}
}
