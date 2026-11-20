package finalProject.doughnut_drop.service;

import finalProject.doughnut_drop.dto.DoughnutRequestDTO;
import finalProject.doughnut_drop.dto.DoughnutResponseDTO;
import finalProject.doughnut_drop.entity.DoughnutItem;
import finalProject.doughnut_drop.repository.DoughnutRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DoughnutService {

    private final DoughnutRepository repository;

    public DoughnutService(DoughnutRepository repository) {
        this.repository = repository;
    }

    public List<DoughnutResponseDTO> getAll(String glaze) {
        List<DoughnutItem> items = (glaze == null || glaze.isBlank())
                ? repository.findAll()
                : repository.findByGlazeTypeIgnoreCase(glaze.trim());
        return items.stream().map(this::toDTO).toList();
    }

    public DoughnutResponseDTO getById(Long id) {
        DoughnutItem item = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doughnut not found: " + id));
        return toDTO(item);
    }

    public DoughnutResponseDTO create(DoughnutRequestDTO req) {
        String normalizedCode = req.code().trim().toUpperCase();
        repository.findByCodeIgnoreCase(normalizedCode).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Code already exists: " + normalizedCode);
        });

        DoughnutItem item = new DoughnutItem(
                normalizedCode,
                req.flavorName().trim(),
                req.glazeType().trim(),
                req.price(),
                req.stockQuantity()
        );
        return toDTO(repository.save(item));
    }

    public DoughnutResponseDTO update(Long id, DoughnutRequestDTO req) {
        DoughnutItem item = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doughnut not found: " + id));

        String normalizedCode = req.code().trim().toUpperCase();
        if (!item.getCode().equalsIgnoreCase(normalizedCode)) {
            repository.findByCodeIgnoreCase(normalizedCode).ifPresent(existing -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Code already exists: " + normalizedCode);
            });
        }

        item.setCode(normalizedCode);
        item.setFlavorName(req.flavorName().trim());
        item.setGlazeType(req.glazeType().trim());
        item.setPrice(req.price());
        item.setStockQuantity(req.stockQuantity());
        return toDTO(repository.save(item));
    }

    public void delete(Long id) {
        DoughnutItem item = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doughnut not found: " + id));
        repository.delete(item);
    }

    private DoughnutResponseDTO toDTO(DoughnutItem item) {
        String status = item.getStockQuantity() <= 0
                ? "SOLD_OUT"
                : (item.getStockQuantity() <= 5 ? "LOW_STOCK" : "IN_STOCK");
        return new DoughnutResponseDTO(
                item.getId(),
                item.getCode(),
                item.getFlavorName(),
                item.getGlazeType(),
                item.getPrice(),
                item.getStockQuantity(),
                status
        );
    }
}