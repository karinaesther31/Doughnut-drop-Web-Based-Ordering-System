package finalProject.doughnut_drop.service; // Package ng mga service class

import finalProject.doughnut_drop.dto.DoughnutRequestDTO;
import finalProject.doughnut_drop.dto.DoughnutResponseDTO;
import finalProject.doughnut_drop.entity.DoughnutItem;
import finalProject.doughnut_drop.repository.DoughnutRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DoughnutService {

    public static final BigDecimal BOX_PRICE = new BigDecimal("125.00");

    private final DoughnutRepository repository;

    public DoughnutService(DoughnutRepository repository) {
        this.repository = repository;
    }

    public List<DoughnutResponseDTO> getAll(String flavor) {
        List<DoughnutItem> items = (flavor == null || flavor.isBlank())
                ? repository.findAll()
                : repository.findByFlavorNameIgnoreCase(flavor.trim());
        return items.stream().map(this::toDTO).toList();
    }

    public DoughnutResponseDTO getById(Long id) {
        DoughnutItem item = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doughnut not found: " + id));
        return toDTO(item);
    }

    public DoughnutResponseDTO create(DoughnutRequestDTO req) {
        String formattedFlavor = formatFlavorName(req.flavorName());

        if (!repository.findByFlavorNameIgnoreCase(formattedFlavor).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Flavor already exists: " + formattedFlavor);
        }

        validatePriceIs125(req.price());

        DoughnutItem item = new DoughnutItem(
                formattedFlavor,
                BOX_PRICE,
                req.stockQuantity()
        );

        return toDTO(repository.save(item));
    }

    public DoughnutResponseDTO update(Long id, DoughnutRequestDTO req) {
        DoughnutItem item = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doughnut not found: " + id));

        String formattedFlavor = formatFlavorName(req.flavorName());
        validatePriceIs125(req.price());

        item.setFlavorName(formattedFlavor);
        item.setPrice(BOX_PRICE);
        item.setStockQuantity(req.stockQuantity());

        return toDTO(repository.save(item));
    }

    public DoughnutResponseDTO restock(Long id, int addedStock) {
        if (addedStock <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restock quantity must be at least 1");
        }

        DoughnutItem item = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doughnut not found: " + id));

        item.setStockQuantity(item.getStockQuantity() + addedStock);
        return toDTO(repository.save(item));
    }

    public void delete(Long id) {
        DoughnutItem item = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doughnut not found: " + id));
        repository.delete(item);
    }

    private void validatePriceIs125(BigDecimal price) {
        if (price == null || price.compareTo(BOX_PRICE) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must be 125.00 per order (1 box)");
        }
    }

    private String formatFlavorName(String rawFlavor) {
        String clean = rawFlavor.trim().toLowerCase();
        return switch (clean) {
            case "chocolate" -> "Chocolate";
            case "white chocolate" -> "White Chocolate";
            case "strawberry" -> "Strawberry";
            case "ube" -> "Ube";
            case "matcha" -> "Matcha";
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid flavor. Allowed: Chocolate, White Chocolate, Strawberry, Ube, Matcha"
            );
        };
    }

    private DoughnutResponseDTO toDTO(DoughnutItem item) {
        String status = item.getStockQuantity() <= 0
                ? "SOLD_OUT"
                : (item.getStockQuantity() <= 5 ? "LOW_STOCK" : "IN_STOCK");
        return new DoughnutResponseDTO(
                item.getId(),
                item.getFlavorName(),
                item.getPrice(),
                item.getStockQuantity(),
                status
        );
    }
}