package finalProject.doughnut_drop.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_accounts")
public class userAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String defaultDeliveryAddress;

    @Column(nullable = false)
    private String defaultFulfillmentType;

    @Column(nullable = false)
    private String defaultPaymentMethod;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String role;

    public userAccount() {}

    public userAccount(
                        String fullName,
                        String username,
                        String email,
                        String mobileNumber,
                        String defaultDeliveryAddress,
                        String defaultFulfillmentType,
                        String defaultPaymentMethod,
                        String role
    ) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.defaultDeliveryAddress = defaultDeliveryAddress;
        this.defaultFulfillmentType = defaultFulfillmentType;
        this.defaultPaymentMethod = defaultPaymentMethod;
        this.password = null;
        this.role = role;
    }


    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; } // Naayos: mobileNumber na ang naka-assign, hindi email
    public String getDefaultDeliveryAddress() { return defaultDeliveryAddress; }
    public void setDefaultDeliveryAddress(String defaultDeliveryAddress) { this.defaultDeliveryAddress = defaultDeliveryAddress; }
    public String getDefaultFulfillmentType() { return defaultFulfillmentType; }
    public void setDefaultFulfillmentType(String defaultFulfillmentType) { this.defaultFulfillmentType = defaultFulfillmentType; }
    public String getDefaultPaymentMethod() { return defaultPaymentMethod; }
    public void setDefaultPaymentMethod(String defaultPaymentMethod) { this.defaultPaymentMethod = defaultPaymentMethod; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}