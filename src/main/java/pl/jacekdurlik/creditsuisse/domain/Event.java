package pl.jacekdurlik.creditsuisse.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String eventId;
    @Column
    private Long duration;
    @Column
    private String type;
    @Column
    private String host;
    @Column
    private Boolean alert;
}
