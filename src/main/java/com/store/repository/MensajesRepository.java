package com.store.repository;

import com.store.entity.Mensajes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajesRepository  extends JpaRepository<Mensajes, Long> {
}
