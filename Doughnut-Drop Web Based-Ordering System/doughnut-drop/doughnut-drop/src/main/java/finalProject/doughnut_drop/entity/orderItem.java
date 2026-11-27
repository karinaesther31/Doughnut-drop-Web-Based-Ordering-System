package finalProject.doughnut_drop.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class orderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private customerOrder order;

    @Column(nullable = false)
    private Long doughnutId;

    @Column(nullable = false)
    private String flavorName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;

    public orderItem() {}

    public orderItem(Long doughnutId, String flavorName, Integer quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        this.flavorName = flavorName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    // Mga getters at setters
    public Long getId() { return id; }
    public customerOrder getOrder() { return order; }
    public void setOrder(customerOrder order) { this.order = order; }
    public Long getDoughnutId() { return doughnutId; }
    public void setDoughnutId(Long doughnutId) { this.doughnutId = doughnutId; }
    public String getFlavorName() { return flavorName; }
    public void setFlavorName(String flavorName) { this.flavorName = flavorName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}