package restaurant.ms.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import restaurant.ms.core.enums.RestaurantUserType;
import restaurant.ms.core.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_user")
@Setter
@Getter
@NoArgsConstructor
public class RestaurantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_user_seq")
    @SequenceGenerator(name="restaurant_user_seq", sequenceName = "restaurant_user_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "third_name")
    private String thirdName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private RestaurantUserType restaurantUserType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "create_date")
    private LocalDateTime createDate;

}
