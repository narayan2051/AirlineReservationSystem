package edu.miu.ars.controller;

import edu.miu.ars.constant.ResponseConstant;
import edu.miu.ars.domain.Flight;
import edu.miu.ars.domain.FlightInfo;
import edu.miu.ars.domain.Reservation;
import edu.miu.ars.domain.Ticket;
import edu.miu.ars.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id){
        Reservation reservation = reservationService.findById(id);
        return reservation != null ? ResponseEntity.ok(reservation) :
                ResponseEntity.badRequest().body(ResponseConstant.NOT_FOUND);
    }

    @GetMapping
    public List<Reservation> findAll(){
        return reservationService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Reservation reservation){
        return reservationService.save(reservation) != null ? ResponseEntity.ok(ResponseConstant.SAVE_SUCCESS)
                : ResponseEntity.badRequest().body(ResponseConstant.SAVE_FAILED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Reservation reservation){
        if(id.equals(reservation.getId())){
            return reservationService.update(reservation, id) ? ResponseEntity.ok(ResponseConstant.UPDATE_SUCCESS)
                    : ResponseEntity.badRequest().body(ResponseConstant.UPDATE_FAILED);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id){
       return reservationService.deleteById(id) ? ResponseEntity.ok(ResponseConstant.DELETE_SUCCESS)
               : ResponseEntity.badRequest().body(ResponseConstant.DELETE_FAILED);
    }



}
