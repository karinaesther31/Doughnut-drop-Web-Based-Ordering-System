package finalProject.doughnut_drop.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "doughnut_items")
public class DoughnutItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String flavorName;

    @Column(nullable = false)
    private String glazeType;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    public DoughnutItem() {}

    public DoughnutItem(String code, String flavorName, String glazeType,
                        BigDecimal price, Integer stockQuantity) {
        this.code = code;
        this.flavorName = flavorName;
        this.glazeType = glazeType;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getFlavorName() { return flavorName; }
    public void setFlavorName(String flavorName) { this.flavorName = flavorName; }
    public String getGlazeType() { return glazeType; }
    public void setGlazeType(String glazeType) { this.glazeType = glazeType; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}