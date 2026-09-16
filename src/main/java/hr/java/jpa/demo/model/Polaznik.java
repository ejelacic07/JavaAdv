package hr.java.jpa.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Polaznik")
@NoArgsConstructor
@RequiredArgsConstructor
public class Polaznik {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(name = "ime", nullable = false, length = 100)
    @Getter
    @Setter
    @NonNull
    private String ime;

    @Column(name = "prezime", nullable = false, length = 100)
    @Getter
    @Setter
    @NonNull
    private String prezime;

    @ManyToMany(mappedBy = "polaznici")
    Set<ProgramObrazovanja> pos = new HashSet<>();

    public void addPO(ProgramObrazovanja po) {
        pos.add(po);
        po.getPolaznici().add(this);
    }


}
