package knewone.healthcaresb.doctor.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import knewone.healthcaresb.visit.persistence.entity.Visit;
import lombok.*;

import java.time.ZoneId;
import java.util.List;

@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private ZoneId timezone;

    private long totalPatients;

    @OneToMany(mappedBy = "doctor")
    @JsonManagedReference
    private List<Visit> visits;

    public Doctor(String firstName, String lastName, ZoneId timezone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.timezone = timezone;
    }
}
