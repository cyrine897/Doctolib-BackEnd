package com.example.sahtyapp1.Repository;
import com.example.sahtyapp1.Entity.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MedicamentRepo extends JpaRepository<Medicament, Long>  {
}
