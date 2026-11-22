package finalProject.doughnut_drop.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_orders")

public class customerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column (nullable = false)
    private String customerName;

    @Column (nullable = false)
    private BigDecimal totalAmount;

    @Column (nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<orderItem> items = new ArrayList<>();

    public customerOrder(){}

    public customerOrder(String customerName, BigDecimal totalAmount, LocalDateTime createdAt){
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;

    }
    public void addItem(orderItem item){
        item.setOrder(this);
        this.items.add(item);
    }
    public Long getId(){ return ID; }
    public String getCustomerName() {return customerName; }
    public void setCustomerName(String customerName) {this.customerName = customerName; }
    public BigDecimal getTotalAmount(){return totalAmount; }
    public void setTotalAmount (BigDecimal totalAmount) { this.totalAmount = totalAmount;}
    public LocalDateTime getCreatedAt () {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt; }
    public List<orderItem> getItems () { return items ;}

}
