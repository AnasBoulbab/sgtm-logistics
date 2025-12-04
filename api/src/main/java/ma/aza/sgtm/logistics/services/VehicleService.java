package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.entities.Vehicle;
import ma.aza.sgtm.logistics.repositories.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Vehicle create(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Long id, Vehicle vehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(id).orElseThrow();
        // TODO : use mapper to update existing vehicle fields
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id).orElseThrow();
    }

    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    public Page<Vehicle> search(Specification<Vehicle> specification, Pageable pageable) {
        return vehicleRepository.findAll(specification, pageable);
    }

    public void delete(Long id) {
        Vehicle existingVehicle = vehicleRepository.findById(id).orElseThrow();
        vehicleRepository.delete(existingVehicle);
    }

}
