package cl.duoc.api_compras.repository;

import cl.duoc.api_compras.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepository extends JpaRepository <Orden, Long > {
}
