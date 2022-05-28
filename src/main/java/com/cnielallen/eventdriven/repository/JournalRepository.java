package com.cnielallen.eventdriven.repository;


import com.cnielallen.eventdriven.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository  extends JpaRepository<Journal, Long> {
}
