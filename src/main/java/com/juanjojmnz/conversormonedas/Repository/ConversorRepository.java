package com.juanjojmnz.conversormonedas.Repository;

import com.juanjojmnz.conversormonedas.Entities.Conversor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversorRepository extends JpaRepository<Conversor, Long> {

}
