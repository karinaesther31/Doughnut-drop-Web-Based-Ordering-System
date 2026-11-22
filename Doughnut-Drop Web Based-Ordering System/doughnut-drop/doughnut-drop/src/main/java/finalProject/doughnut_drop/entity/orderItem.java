package finalProject.doughnut_drop.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class orderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private customerOrder order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doughnut_item_id", nullable = false)
    private DoughnutItem doughnutItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal subtotal;

    public orderItem() {}

    public orderItem(customerOrder order, DoughnutItem doughnutItem, Integer quantity, BigDecimal subtotal){
        this.order = order;
        this.doughnutItem = doughnutItem;
        this.quantity = quantity;
        this.subtotal = subtotal;

    }
    public Long getId() { return id;}
    public customerOrder getOrder() {return order;}
    public void setOrder(customerOrder order) {this.order =  order;}
    public DoughnutItem getDoughnutItem() {return doughnutItem;}
    public void setDoughnutItem(DoughnutItem doughnutItem) {this.doughnutItem = doughnutItem;}
    public Integer getQuantity() {return quantity;}
    public void setQuantity(Integer quantity) {this.quantity = quantity;}
    public BigDecimal getSubtotal() { return subtotal;}
    public void setSubtotal (BigDecimal subtotal) {this.subtotal = subtotal;}


}
