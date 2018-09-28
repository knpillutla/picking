package com.example.picking.endpoint.rest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.example.picking.dto.requests.PickConfirmRequestDTO;
import com.example.picking.service.PickingService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
@Controller
@RequestMapping("/picking/v1")
@Api(value="Pick Service", description="Operations pertaining to picking")
@RefreshScope
@Slf4j
public class PickingRestEndPoint {

    @Value("${message: Picking Service - Config Server is not working..pelase check}")
    private String msg;
    
    @Autowired
	PickingService pickingService;
	Logger logger = LoggerFactory.getLogger(PickingRestEndPoint.class);
	
	@GetMapping("/")
	public ResponseEntity hello() throws Exception {
		return ResponseEntity.ok(msg);
	}
	
	@PostMapping("/{busName}/{locnNbr}/picks/next/{userId}")
	public ResponseEntity assignNextPick(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr, @PathVariable("userId") String userId) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.assignNextPick(busName, locnNbr, userId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while getting next pick task"));
		}
	}
	
	@PostMapping("/{busName}/{locnNbr}/picks/next/{batchNbr}/{userId}")
	public ResponseEntity assignNextPickByBatchNbr(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr, @PathVariable("batchNbr") String batchNbr, @PathVariable("userId") String userId) throws IOException {
		try {
			log.info("Received assign next pick by batch nbr:" + busName + "," + locnNbr + "," + batchNbr + "," + userId);
			return ResponseEntity.ok(pickingService.assignNextPick(busName, locnNbr, batchNbr, userId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while getting next pick task"));
		}
	}

	@GetMapping("/{busName}/{locnNbr}/picks/{id}")
	public ResponseEntity getByPickId(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long pickId) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.findByPickId(busName, locnNbr, pickId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while getting next pick task"));
		}
	}

	@GetMapping("/{busName}/{locnNbr}/picks/order/{id}")
	public ResponseEntity getPicksByOrderId(@PathVariable("busName") String busName, @PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long orderId) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.findByOrderId(busName, locnNbr, orderId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occured while getting next pick task"));
		}
	}

	@PostMapping("/{busName}/{locnNbr}/picks/{id}")
	public ResponseEntity confirmPick(@PathVariable("busName") String busName,@PathVariable("locnNbr") Integer locnNbr, @PathVariable("id") Long id, @RequestBody PickConfirmRequestDTO pickReq) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.confirmPick(pickReq));
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Occured for GET request busName:" + busName + ", id:" + id + " : " + e.getMessage()));
		}
	}	
	
	@PostMapping("/{busName}/{locnNbr}/orders/release/{orderNbr}")
	public ResponseEntity releasePicksForOrder(@PathVariable("busName") String busName,@PathVariable("locnNbr") Integer locnNbr, @PathVariable("orderNbr") String orderNbr) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.releasePicksforOrder(busName, locnNbr, orderNbr));
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Occured while processing create pick list, busName:" + busName + ", locnNbr:" + locnNbr + " : " + e.getMessage()));
		}
	}	
	
	@PostMapping("/{busName}/{locnNbr}/orders/release/batch/{batchNbr}")
	public ResponseEntity releasePicksForBatch(@PathVariable("busName") String busName,@PathVariable("locnNbr") Integer locnNbr, @PathVariable("batchNbr") String batchNbr) throws IOException {
		try {
			return ResponseEntity.ok(pickingService.releasePicksforBatch(busName, locnNbr, batchNbr));
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Occured while processing create pick list, busName:" + busName + ", locnNbr:" + locnNbr + " : " + e.getMessage()));
		}
	}	
}
