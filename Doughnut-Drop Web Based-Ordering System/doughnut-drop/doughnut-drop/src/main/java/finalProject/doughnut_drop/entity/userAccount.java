package finalProject.doughnut_drop.entity; // Package ng mga JPA entity class

import jakarta.persistence.*; // Annotations para sa @Entity, @Table, @Id, @GeneratedValue, at @Column

@Entity // Tinutukoy na ang class na ito ay magiging table sa database
@Table(name = "user_accounts") // Pangalan ng table sa database
public class userAccount {

    @Id // Primary key ng bawat user account
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ang ID sa database
    private Long id; // Natatanging ID ng user

    @Column(nullable = false) // Bawal blangko ang buong pangalan
    private String fullName; // Buong pangalan ng customer o staff/admin

    @Column(nullable = false, unique = true) // Bawal blangko at dapat unique ang email
    private String email; // Email address ng user

    @Column(nullable = false, unique = true) // Bawal blangko at dapat unique ang username (gamit sa customer login)
    private String username; // Username na gagamitin sa pag-log in

    @Column(nullable = false) // Bawal blangko ang mobile number
    private String mobileNumber; // Mobile number ng user

    @Column(nullable = false) // Bawal blangko ang default delivery address
    private String defaultDeliveryAddress; // Default na address kapag nagpa-deliver

    @Column(nullable = false) // Bawal blangko ang default fulfillment type
    private String defaultFulfillmentType; // "PICK_UP" o "DELIVERY"

    @Column(nullable = false) // Bawal blangko ang default payment method
    private String defaultPaymentMethod; // "CASH" o "ONLINE_PAYMENT"

    @Column(nullable = true) // Puwedeng null para sa customer (walang password), pero may value sa staff/admin
    private String password; // Naka-hash na password para sa staff/admin

    @Column(nullable = false) // Bawal blangko ang role
    private String role; // Halimbawa: "ROLE_USER" para sa customer, "ROLE_ADMIN" para sa staff/admin

    public userAccount() {} // Default constructor na kailangan ng JPA/Hibernate

    public userAccount( // 8-parameter constructor para sa bagong customer (walang password)
                        String fullName,
                        String username,
                        String email,
                        String mobileNumber,
                        String defaultDeliveryAddress,
                        String defaultFulfillmentType,
                        String defaultPaymentMethod,
                        String role
    ) {
        this.fullName = fullName; // Sine-set ang buong pangalan
        this.username = username; // Sine-set ang username
        this.email = email; // Sine-set ang email
        this.mobileNumber = mobileNumber; // Sine-set ang mobile number
        this.defaultDeliveryAddress = defaultDeliveryAddress; // Sine-set ang default delivery address
        this.defaultFulfillmentType = defaultFulfillmentType; // Sine-set ang PICK_UP o DELIVERY
        this.defaultPaymentMethod = defaultPaymentMethod; // Sine-set ang CASH o ONLINE_PAYMENT
        this.password = null; // Walang password ang customer account
        this.role = role; // Sine-set ang role
    }

    // Mga getters at setters para mabasa at mabago ng service/repository ang bawat field
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