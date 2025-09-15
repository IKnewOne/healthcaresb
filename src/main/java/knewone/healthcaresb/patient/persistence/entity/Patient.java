package knewone.healthcaresb.patient.persistence.entity;

import jakarta.persistence.*;
import knewone.healthcaresb.visit.persistence.entity.Visit;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "patient")
    private List<Visit> visits;

    public Patient(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

    }
}
