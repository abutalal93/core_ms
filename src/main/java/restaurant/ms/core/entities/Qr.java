package restaurant.ms.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import restaurant.ms.core.dto.responses.QrSearchRs;
import restaurant.ms.core.dto.responses.RestaurantSearchRs;
import restaurant.ms.core.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qr")
@Setter
@Getter
@NoArgsConstructor
public class Qr {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qr_seq")
    @SequenceGenerator(name="qr_seq", sequenceName = "qr_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "alias")
    private String alias;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    public QrSearchRs toQrSearchRs(){
        QrSearchRs qrSearchRs = new QrSearchRs();

        qrSearchRs.setCreateDate(this.createDate);
        qrSearchRs.setAlias(this.alias);
        qrSearchRs.setCode(this.code);
        qrSearchRs.setStatus(this.status.name());
        qrSearchRs.setId(this.id);

        return qrSearchRs;
    }

}
