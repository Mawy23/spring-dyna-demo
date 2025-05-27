package com.dyna.demo;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.GlobalOpenTelemetry;


@RestController
@SpringBootApplication
public class DemoApplication {

    private static final Tracer tracer = GlobalOpenTelemetry.getTracer("com.dyna.demo");

    @Autowired
    private EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "👋 Hola desde Spring con Dynatrace";
    }

    @PostMapping("/api")
    public String process(@RequestBody String payload) {
        return "Recibido: " + payload;
    }

    @GetMapping("/dbtest")
    public String dbTest() {
        Object result = entityManager
            .createNativeQuery("SELECT COUNT(*) FROM pg_stat_statements")
            .getSingleResult();
        return "Consultas monitorizadas: " + result.toString();
    }

    @Autowired
    private PetRepository petRepository;

    @PostMapping("/pets")
    public Pet createPet(@RequestBody Pet pet) {
        Span span = tracer.spanBuilder("Guardar nueva mascota").startSpan();
        try {
            return petRepository.save(pet);
        } finally {
            span.end();
        }
    }


    @GetMapping("/pets")
    public List<Pet> getAllPets() {
        Span span = tracer.spanBuilder("Obtener todas las mascotas").startSpan();
        try {
            return petRepository.findAll();
        } finally {
            span.end();
        }
    }


    @GetMapping("/pets/{id}")
    public Pet getPet(@PathVariable Long id) {
        Span span = tracer.spanBuilder("Buscar mascota por ID").startSpan();
        try {
            return petRepository.findById(id).orElseThrow();
        } finally {
            span.end();
        }
    }


    @PutMapping("/pets/{id}")
    public Pet updatePet(@PathVariable Long id, @RequestBody Pet petDetails) {
        Span span = tracer.spanBuilder("Actualizar mascota").startSpan();
        try {
            Pet pet = petRepository.findById(id).orElseThrow();
            pet.setName(petDetails.getName());
            pet.setType(petDetails.getType());
            return petRepository.save(pet);
        } finally {
            span.end();
        }
    }


    @DeleteMapping("/pets/{id}")
    public void deletePet(@PathVariable Long id) {
        Span span = tracer.spanBuilder("Eliminar mascota").startSpan();
        try {
            petRepository.deleteById(id);
        } finally {
            span.end();
        }
    }


}
