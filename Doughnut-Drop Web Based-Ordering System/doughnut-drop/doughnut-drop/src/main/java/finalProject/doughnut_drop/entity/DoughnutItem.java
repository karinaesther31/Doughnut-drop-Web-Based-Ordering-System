package finalProject.doughnut_drop.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "doughnut_items")
public class DoughnutItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String flavorName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;
    public DoughnutItem() {}
    public DoughnutItem(String flavorName, BigDecimal price, Integer stockQuantity) {
        this.flavorName = flavorName;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }


    public Long getId() { return id; }
    public String getFlavorName() { return flavorName; }
    public void setFlavorName(String flavorName) { this.flavorName = flavorName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}