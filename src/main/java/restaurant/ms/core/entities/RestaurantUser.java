package restaurant.ms.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import restaurant.ms.core.dto.responses.CategorySearchRs;
import restaurant.ms.core.dto.responses.RestUserSearchRs;
import restaurant.ms.core.enums.RestaurantUserType;
import restaurant.ms.core.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_unique", columnNames = {"username"})})
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

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @JsonIgnore
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

    public RestUserSearchRs toRestUserSearchRs(){
        RestUserSearchRs restUserSearchRs = new RestUserSearchRs();
        restUserSearchRs.setId(this.id);
        restUserSearchRs.setFirstName(this.firstName);
        restUserSearchRs.setSecondName(this.secondName);
        restUserSearchRs.setThirdName(this.thirdName);
        restUserSearchRs.setLastName(this.lastName);
        restUserSearchRs.setStatus(this.status.name());
        restUserSearchRs.setUserType(this.restaurantUserType.name());
        restUserSearchRs.setUsername(this.username);
        restUserSearchRs.setEmail(this.email);
        restUserSearchRs.setMobileNumber(this.mobileNumber);

        return restUserSearchRs;
    }
}
