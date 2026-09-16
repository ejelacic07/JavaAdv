package hr.java.jpa.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ProgramObrazovanja")
@NoArgsConstructor
@RequiredArgsConstructor
public class ProgramObrazovanja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "naziv", nullable = false, length = 100)
    @Getter
    @Setter
    @NonNull
    private String naziv;

    @Column(name = "csvet", nullable = false)
    @Getter
    @Setter
    @NonNull
    private int csvet;


    @ManyToMany
    @JoinTable(name = "polaznik_po",
            joinColumns = @JoinColumn(name = "po_id"),
            inverseJoinColumns = @JoinColumn(name = "polaznik_id"))
    @Getter
    @Setter
    Set<Polaznik> polaznici = new HashSet<>();

}
