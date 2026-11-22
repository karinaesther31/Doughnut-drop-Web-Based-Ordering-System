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
    private String password;

    @Column(nullable = false)
    private String role;



    public userAccount(String fullName, String username, String email,String mobileNumber, String defaultDeliveryAddress,
                       String password, String role){
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.defaultDeliveryAddress = defaultDeliveryAddress;
        this.password = password;
        this.role = role;

    }
    public Long getId(){return id; }
    public String getFullName(){return fullName; }
    public void setFullName(String fullname) {this.fullName = fullname;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public String getEmail(){return email; }
    public void setEmail(String email){this.email =  email;}
    public String getMobileNumber(){return mobileNumber;}
    public void setMobileNumber(String mobileNumber){this.mobileNumber = email;}
    public String getDefaultDeliveryAddress(){return defaultDeliveryAddress;}
    public void setDefaultDeliveryAddress(String defaultDeliveryAddress){this.defaultDeliveryAddress = defaultDeliveryAddress;}
    public String getPassword() {return password;}
    public void setPassword (String password){this.password = password;}
    public String getRole(){return role; }
    public void setRole(String role) {this.role = role;}
}
