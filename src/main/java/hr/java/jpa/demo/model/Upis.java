package hr.java.jpa.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Upis")
@NoArgsConstructor
@RequiredArgsConstructor
public class Upis {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Getter @Setter
     private Long id;

     @ManyToOne
     @JoinColumn(name = "program_obrazovanja_id", nullable = false)
     @Getter @Setter
     @NonNull
     private ProgramObrazovanja programObrazovanja;

     @ManyToOne
     @JoinColumn(name = "polaznik_id", nullable = false)
     @Getter @Setter
     @NonNull
     private Polaznik polaznik;



}
