package finalProject.doughnut_drop.controller;

import finalProject.doughnut_drop.dto.DoughnutRequestDTO;
import finalProject.doughnut_drop.dto.DoughnutResponseDTO;
import finalProject.doughnut_drop.service.DoughnutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doughnuts")
public class DoughnutController {

    private final DoughnutService service;

    public DoughnutController(DoughnutService service) {
        this.service = service;
    }

    @GetMapping
    public List<DoughnutResponseDTO> list(@RequestParam(required = false) String glaze) {
        return service.getAll(glaze);
    }

    @GetMapping("/{id}")
    public DoughnutResponseDTO get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<DoughnutResponseDTO> create(@Valid @RequestBody DoughnutRequestDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    public DoughnutResponseDTO update(@PathVariable Long id, @Valid @RequestBody DoughnutRequestDTO req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}