package edu.miu.ars.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 20,nullable = false)
    private String number;
    @Temporal(TemporalType.DATE)
    private Date flightDate;

    @ManyToOne
    private Reservation reservation;


    public Ticket(String number, Date flightDate) {
        this.number = number;
        this.flightDate = flightDate;
    }
}
