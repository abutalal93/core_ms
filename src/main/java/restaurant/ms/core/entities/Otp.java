package restaurant.ms.core.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restaurant.ms.core.dto.responses.LookupRs;
import restaurant.ms.core.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Table(name = "otp")
@Setter
@Getter
@NoArgsConstructor
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_seq")
    @SequenceGenerator(name="otp_seq", sequenceName = "otp_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "mobileNumber")
    private String mobileNumber;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
