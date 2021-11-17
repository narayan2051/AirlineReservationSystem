package edu.miu.ars.controller;

import edu.miu.ars.DTO.ReservationDTO;
import edu.miu.ars.constant.AppConstant;
import edu.miu.ars.constant.ResponseConstant;
import edu.miu.ars.domain.Reservation;
import edu.miu.ars.service.ReservationService;
import edu.miu.ars.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final AppUtil appUtil;

    @Autowired
    public ReservationController(ReservationService reservationService, AppUtil appUtil) {
        this.reservationService = reservationService;
        this.appUtil = appUtil;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_AGENT')")
    public ResponseEntity<?> findById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(reservationService.findDetailOfReservation(appUtil.getFromAuthentication(authentication).getUser().getId(), id));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_AGENT')")
    public List<Reservation> findAll(Authentication authentication) {
        return reservationService.findListOfReservationOfCurrentLoggedInUser(appUtil.getFromAuthentication(authentication).getUser().getId());
    }

    @PostMapping("/by-agent")
    @PreAuthorize("hasAuthority('ROLE_AGENT')")
    public ResponseEntity<?> saveByAgent(@RequestBody @Valid ReservationDTO reservation, Authentication authentication) {
        System.out.println(authentication.getAuthorities().toString());
        reservation.setAgentId(appUtil.getFromAuthentication(authentication).getUser().getId());
        reservation.setUserId(appUtil.getFromAuthentication(authentication).getUser().getId());
        return reservationService.saveReservation(reservation, true) != null ? ResponseEntity.ok(ResponseConstant.SAVE_SUCCESS)
                : ResponseEntity.badRequest().body(ResponseConstant.SAVE_FAILED);
    }

    @PostMapping("/by-passenger")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> saveByPassenger(@RequestBody ReservationDTO reservation, Authentication authentication) {
        reservation.setUserId(appUtil.getFromAuthentication(authentication).getUser().getId());
        return reservationService.saveReservation(reservation, false) != null ? ResponseEntity.ok(ResponseConstant.SAVE_SUCCESS)
                : ResponseEntity.badRequest().body(ResponseConstant.SAVE_FAILED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Reservation reservation, Authentication authentication) {
        if (appUtil.getFromAuthentication(authentication).getUser().getId().equals(reservation.getCreatedBy().getId())) {

            if (id.equals(reservation.getId())) {
                return reservationService.update(reservation, id) ? ResponseEntity.ok(ResponseConstant.UPDATE_SUCCESS)
                        : ResponseEntity.badRequest().body(ResponseConstant.UPDATE_FAILED);
            }
        }
        return ResponseEntity.badRequest().build();
    }


    @PatchMapping("/confirm-by-agent/{id}")
    @PreAuthorize("hasAuthority('ROLE_AGENT')")
    public ResponseEntity<?> confirmByAgent(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirmByAgent(appUtil.getFromAuthentication(authentication).getUser().getId(), id));
    }

    @PatchMapping("/confirm-by-passenger/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> confirmByPassenger(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirmByPassenger(appUtil.getFromAuthentication(authentication).getUser().getId(), id));
    }

    @PatchMapping("/cancelled/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_AGENT')")
    public ResponseEntity<?> cancelledByPassenger(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelledReservation(appUtil.getFromAuthentication(authentication).getUser().getId(), id));
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<?> getAllTicketsOfReservation(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getAllTicketsOfReservation(appUtil.getFromAuthentication(authentication).getUser().getId(), id));
    }

}
