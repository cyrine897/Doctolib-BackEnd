package com.example.sahtyapp1.Repository;

import com.example.sahtyapp1.Entity.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DossierMediRepo extends JpaRepository<DossierMedical , Long> {
}
